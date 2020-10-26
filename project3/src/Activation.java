import java.util.ArrayList;

public class Activation
{
	public static double Sigmoidal(ArrayList<Double> weights, ArrayList<Double> values)
	{
		double exponent = Dot(weights, values);
		return 1/(1+Math.exp(-1*exponent));
	}
	
	public static double Dot(ArrayList<Double> weights, ArrayList<Double> values)
	{	// simply returns weighted sum, useful for net input calculation for neurons
		double sum = 0;
		for(int i = 0; i < weights.size(); i++)
		{
			sum += weights.get(i)*values.get(i);
		}
		return sum;
	}

	public static ArrayList<Double> Softmax(ArrayList<Double> values)	// used on output nodes to produce an array of probabilities
	{
		ArrayList<Double> output = new ArrayList<>();
		double sumExponents = 0;
		for (Double value : values) {	// For denominator
			sumExponents += Math.exp(value);
		}
		for (int i = 0; i < values.size(); i++) {
			output.set(i, Math.exp(values.get(i))/sumExponents);
		}
		return output;
	}
}
