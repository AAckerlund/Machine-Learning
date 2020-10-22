import java.util.ArrayList;
/*
edge weights are initialized to random
each node points to every node in the next layer
 */
public class Neuron
{
	private ArrayList<Neuron> outputs;
	private ArrayList<Double> weights;
	private double value;
	
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

	//adds a new output edge and gives a random weight to the edge
	public void addOutput(Neuron n)
	{
		outputs.add(n);
		weights.add((Math.random()*2)-1);
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
