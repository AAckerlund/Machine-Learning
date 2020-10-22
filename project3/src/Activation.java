import java.util.ArrayList;

public class Activation
{
	public static double Sigmoidal(ArrayList<Double> weights, ArrayList<Double> values)
	{
		double exponent = 0;
		for(int i = 0; i < weights.size(); i++)
		{
			exponent += weights.get(i)*values.get(i);
		}
		return 1/(1+Math.exp(-1*exponent));
	}
	
	public static double Linear(ArrayList<Double> weights, ArrayList<Double> values)//TODO Implement me
	{
		return (Math.random()*2)-1;
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
