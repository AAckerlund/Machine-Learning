import java.util.ArrayList;

public class Neuron
{
	private ArrayList<Neuron> outputs;
	private ArrayList<Double> weights;
	double biasWeight;
	
	public Neuron()
	{
		outputs = new ArrayList<>();
		weights = new ArrayList<>();
	}
}
