package Nodes;

public class GlassNode
{
	int id;
	int glassType;//valid values are 1-7, 0 if unknown
	double[] data;
	
	public GlassNode(int id, int glassType, double[] data)
	{
		this.id = id;
		this.glassType = glassType;
		this.data = data;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getGlassType()
	{
		return glassType;
	}
	
	public double[] getData()
	{
		return data;
	}
}
