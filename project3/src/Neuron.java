import java.util.ArrayList;
import java.util.HashMap;

/*
edge weights are initialized to random between -0.01 and 0.01
each node points to every node in the next layer
 */
public class Neuron
{
	private ArrayList<Neuron> outputs;		// Neurons this Neuron outputs to
	private ArrayList<Neuron> inputs;		// Neurons that output to this Neuron
	private HashMap<Neuron,Double> weights;		// Weights from this Neuron to the next layer (downstream), stored in
											// HashMap with output neurons as a key, easier to program backprop
	private double value;					// Output value of neuron (after activation for hidden and output neurons)
	private double sumInput;				// Input value of neuron (weighted sum of previous layer outputs(values))

	private double delta;						// Stores current delta value for this neuron to be used in backpropagation
	private HashMap<Neuron,Double> weightUpdates;	// Stores weight updates to be used in backpropagation later, again
													// as output Neuron-based hashmap
	public Neuron()
	{
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		weights = new HashMap<>();
		weightUpdates = new HashMap<>();
		value = 0;
		delta = 0;
	}

	public Neuron(double value)
	{
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		weights = new HashMap<>();
		weightUpdates = new HashMap<>();
		this.value = value;
		delta = 0;
	}
	
	public void updateValue(double inputValue)
	{
		value = inputValue;
	}

	public void updateSumInputs(double newSumInput)
	{
		sumInput = newSumInput;
	}
	
	public ArrayList<Double> calcOutput()
	{
		ArrayList<Double> outputValues = new ArrayList<>();
		for(int i = 0; i < outputs.size(); i++)
		{
			outputValues.add(value*weights.get(i));
		}
		return outputValues;
	}

	// save update of weight to downstream neuron n, but do not change any weights yet
	public void saveWeightUpdate(Neuron n, double weightChange)
	{
		this.weightUpdates.put(n, weightChange);
	}

	// change weights
	public void pushWeightUpdate()
	{
		for(Neuron n : weights.keySet())
		{
			weights.put(n , weights.get(n) + weightUpdates.get(n));
		}
	}

	// get previous weight update towards a certain neuron
	public Double getWeightUpdate(Neuron n) {
		return weightUpdates.get(n);
	}

	//adds a new output edge and gives a random weight to the edge between -0.01 and 0.01
	//also assigns saves this node as input to the next node for easy weight access in backpropagation
	public void connectOutput(Neuron n)
	{
		outputs.add(n);
		weights.put(n, (0.01 - Math.random()*0.02));
		//weights.put(n, 0.01);	// Constant weight for testing feedforward
		n.connectInput(this);	// save this node as input to complete double linked list

	}

	public void connectInput(Neuron n) {
		inputs.add(n);
	}
	
	public ArrayList<Double> getWeights()
	{
		return new ArrayList<>(weights.values());
	}
	
	public double getValue()
	{
		return value;
	}

	public ArrayList<Neuron> getDownstream() {
		return outputs;
	}

	public ArrayList<Neuron> getUpstream() {
		return inputs;
	}
	
	public Neuron getOutputNeuron(int index)
	{
		return outputs.get(index);
	}

	public Neuron getInputNeuron(int index)
	{
		return inputs.get(index);
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
