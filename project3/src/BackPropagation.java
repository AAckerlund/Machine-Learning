import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BackPropagation
{
	// Contains the backpropagation training algorithms for both regression and classification
	// Assuming using logistic functions as activation functions
	private final int maxIterations;
	private final boolean isClassification;
	private final double learningRate;    // How quickly gradient descent changes the function values
	private final double momentumScale;
	private final Network nn;             // The network that is being trained
	private final HashMap<Neuron, Double> outputToClass;  // HashMap for mapping an output neuron to its corresponding class
	private final String outFile;
	
	public BackPropagation(Network nn, int maxIterations, double learningRate, double momentumScale, String outFile)
	{
		// Constructor that only saves arguments about the type of problem and training method
		this.nn = nn;
		// How many outputs to have on a multiclass problem
		this.maxIterations = maxIterations;
		this.isClassification = nn.isClassification();
		this.learningRate = learningRate;
		this.momentumScale = momentumScale;
		this.outputToClass = nn.getOutputToClass();
		this.outFile = outFile;
	}
	
	// trains neural net on a shuffled training set until error does not improve (or until a set max)
	public double trainNetwork(ArrayList<Node> trainingSet)
	{
		double bestError = Double.MAX_VALUE;
		double prevError = calculateMSError(trainingSet);
		
		int iteration = 0;
		
		// train using MSError
		// train using Gradient Descent repeatedly until either error does not improve or we reach a specified max iteration
		ArrayList<Node> shuffledSet = new ArrayList<>(trainingSet);
		while(((prevError < bestError) && (iteration < maxIterations)) || iteration < 5)
		{ // do at least 5 iterations
			if(iteration % 50 == 0)
			{
				Printer.println(outFile, "Iteration: " + iteration + " | New Error: " + prevError);
			}
			bestError = prevError;
			Collections.shuffle(shuffledSet);   // randomize order of training set every time
			
			for(Node example : shuffledSet)
			{
				trainExample(example);
			}
			prevError = calculateMSError(trainingSet);
			iteration++;
		}
		Printer.println(outFile, "\nIteration: " + iteration + " | Best Mean-Squared-Error: " + bestError + "\n");
		return bestError;
	}
	
	public double calculateMSError(ArrayList<Node> trainingSet)
	{
		// Calculates squared error for regression for a training set
		double error = 0;
		if(isClassification)
		{
			for(Node example : trainingSet)
			{
				ArrayList<Neuron> output = nn.feedForward(example.getData());
				double correctClass = example.getId();
				for(Neuron n : output)
				{
					double target = 0;
					if(outputToClass.get(n) == correctClass)
					{ // set target to 1 for output neuron with correct class
						target = 1;
					}
					error += Math.pow(n.getValue() - target, 2);
				}
			}
		}
		else
		{  // For regression
			for(Node example : trainingSet)
			{
				double output = nn.feedForward(example.getData()).get(0).getValue();    // Should be one output for regression
				error += Math.pow(output - example.getId(), 2);         // add up squared errors
			}
		}
		error /= trainingSet.size();    // calculate mean
		
		return error;
	}
	
	private void updateWeights()
	{
		nn.pushWeightUpdates();
	}
	
	// updates neural net on an example using backprop
	public void trainExample(Node example)
	{
		double origtarget = example.getId();    // stores the true value to be guessed
		double target = origtarget;             // Copy of target in case it gets modified
		
		for(Neuron outputNeuron : nn.feedForward(example.getData()))
		{ //start at outputs, this should work for multiple outputs
			double output = outputNeuron.getValue();
			
			if(isClassification)
			{ // For classification, there's multiple outputs that need to be checked against a map
				if(outputToClass.get(outputNeuron) == origtarget)
				{
					target = 1; // if the output neuron corresponds to the correct class, it should be 1
				}
				else
				{
					target = 0; // else it should be 0
				}
			}
			// For regression, there's only one output, and the target remains unchanged
			//save delta value for calculating the delta rule in hidden nodes
			outputNeuron.setDelta(calculateDelta(true, output, target, outputNeuron));
			
			for(Neuron prev : outputNeuron.getUpstream())
			{
				double update = calculateWeightUpdate(true, output, target, outputNeuron, prev);
				prev.saveWeightUpdate(outputNeuron, update);    // save weight update to upstream neuron
			}
		}
		
		ArrayList<ArrayList<Neuron>> hiddenLayers = nn.getHiddenLayers();
		//check if there are any hidden layers
		if(hiddenLayers != null)
		{
			//work backwards along the hidden layers to the beginning
			for(int i = hiddenLayers.size() - 1; i > 0; i--)
			{
				for(Neuron currentNeuron : hiddenLayers.get(i))
				{
					double output = currentNeuron.getValue();
					
					//save delta value for calculating the delta rule in hidden nodes
					currentNeuron.setDelta(calculateDelta(true, output, target, currentNeuron));
					
					for(Neuron prev : currentNeuron.getUpstream())
					{
						double update = calculateWeightUpdate(true, output, target, currentNeuron, prev);
						prev.saveWeightUpdate(currentNeuron, update);    // save weight update to upstream neuron
					}
				}
			}
			
			//first hidden layer receives from the input layer
			for(Neuron currentNeuron : hiddenLayers.get(0))
			{
				double output = currentNeuron.getValue();
				
				//save delta value for calculating the delta rule in hidden nodes
				currentNeuron.setDelta(calculateDelta(true, output, target, currentNeuron));
				
				for(Neuron prev : currentNeuron.getUpstream())
				{
					double update = calculateWeightUpdate(true, output, target, currentNeuron, prev);
					prev.saveWeightUpdate(currentNeuron, update);    // save weight update to upstream neuron
				}
			}
		}
		updateWeights();    // Finally push the weights to the functional weight arrays in each Neuron
	}
	
	private double calculateWeightUpdate(boolean outputLayer, double output, double target, Neuron n, Neuron precNeuron)
	{
		if(precNeuron.getWeightUpdate(n) != null)
		{  // add an extra momentum term to the weight update
			return -learningRate * calculateGradient(outputLayer, output, target, n, precNeuron) + momentumScale * precNeuron.getWeightUpdate(n);
		}
		else
		{
			return -learningRate * calculateGradient(outputLayer, output, target, n, precNeuron);
		}
	}
	
	private double calculateGradient(boolean outputLayer, double output, double target, Neuron n, Neuron precNeuron)
	{
		// helper function to calculate "error" term delta, effective multiplies by the input
		return calculateDelta(outputLayer, output, target, n) * precNeuron.getValue();
	}
	
	private double calculateDelta(boolean outputLayer, double output, double target, Neuron n)
	{
		// helper function to calculate delta, the error term using input, output, error, and other deltas
		// use a different function for output layer, since they dont have weights in front
		if(outputLayer)
		{
			if(isClassification)
			{
				return -(target - output) * output * (1 - output);  // error term for output neuron using logistic activation and MSE
			}
			else
			{
				return -(target - output);  // error term for output neuron using linear activation and MSE
			}
		}
		else
		{  // calculate delta for hidden layers, always logistic
			ArrayList<Double> downstreamWeights = n.getWeights();
			ArrayList<Double> deltas = new ArrayList<>();
			for(Neuron neuron : n.getDownstream())
			{
				// collect deltas from neurons in next layer
				deltas.add(neuron.getDelta());
			}
			
			// sum products of weights and deltas
			double sum = 0;
			for(int i = 0; i < downstreamWeights.size(); i++)
			{
				sum += downstreamWeights.get(i) * deltas.get(i);
			}
			
			return output * (1 - output) * sum;
		}
	}
}