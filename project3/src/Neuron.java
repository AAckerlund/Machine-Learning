import java.util.ArrayList;
/*
edge weights are initialized to random between -0.01 and 0.01
each node points to every node in the next layer
 */
public class Neuron
{
	private ArrayList<Neuron> outputs;		// Neurons this Neuron outputs to
	private ArrayList<Double> weights;		// Weights from this Neuron to the next layer
	private double value;					// Output value of neuron (after activation for hidden and output neurons)
	private double sumInputs;				// Input value of neuron (weighted sum of previous layer outputs(values))
	
	public Neuron()
	{
		outputs = new ArrayList<>();
		weights = new ArrayList<>();
		value = 0;
	}

	public Neuron(double value)
	{
		outputs = new ArrayList<>();
		weights = new ArrayList<>();
		this.value = value;
	}
	
	public void updateValue(double inputValue)
	{
		value = inputValue;
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
	
	public void pushWeightUpdate(ArrayList<Double> valueUpdates)
	{
		for(int i = 0; i < valueUpdates.size(); i++)
		{
			outputs.get(i).updateValue(valueUpdates.get(i));
		}
	}

	//adds a new output edge and gives a random weight to the edge between -0.01 and 0.01
	public void addOutput(Neuron n)
	{
		outputs.add(n);
		//weights.add((0.01 - Math.random()*0.02));	//TODO: Change this back to random after testing
		weights.add(0.01);	// Constant weight for testing feedforward
	}
	
	public ArrayList<Double> getWeights()
	{
		return weights;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public Neuron getOutputNeuron(int index)
	{
		return outputs.get(index);
	}
}
