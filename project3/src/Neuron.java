import java.util.ArrayList;
/*
edge weights are initialized to random
each node points to every node in the next layer
 */
public class Neuron
{
	private ArrayList<Neuron> outputs;
	private ArrayList<Double> weights;
	private double biasWeight;
	private double value;
	
	public Neuron()
	{
		outputs = new ArrayList<>();
		weights = new ArrayList<>();
		biasWeight = 0;
		value = 0;
	}
	
	public void updateValue(double inputValue)
	{
		value += inputValue;
	}
	
	public ArrayList<Double> calcOutput()
	{
		ArrayList<Double> outputValues = new ArrayList<>();
		value = Activation.Sigmoidal(weights, outputValues);
		for(int i = 0; i < outputs.size(); i++)
		{
			outputValues.add(value*weights.get(i));
		}
		return outputValues;
	}
	
	public void pushUpdate(ArrayList<Double> valueUpdates)
	{
		for(int i = 0; i < valueUpdates.size(); i++)
		{
			outputs.get(i).updateValue(valueUpdates.get(i));
		}
	}
}
