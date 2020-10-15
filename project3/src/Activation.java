import java.util.ArrayList;

public class Activation
{
	public double Sigmoidal(ArrayList<Double> weights, ArrayList<Double> values)
	{
		double exponent = 0;
		for(int i = 0; i < weights.size(); i++)
		{
			exponent += weights.get(i)*values.get(i);
		}
		return 1/(1+Math.exp(-1*exponent));
	}
}
