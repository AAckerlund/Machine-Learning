package Nodes;

public class IrisNode
{
	String type;
	double[] data;
	
	public IrisNode(String type, double[] data)
	{
		this.type = type;
		this.data = data;
	}
	
	public String getType()
	{
		return type;
	}
	
	public double[] getData()
	{
		return data;
	}
}
