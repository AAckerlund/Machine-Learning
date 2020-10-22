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
}
