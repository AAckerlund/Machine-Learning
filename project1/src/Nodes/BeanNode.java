package Nodes;

public class BeanNode
{
	int type;
	int[] data;
	
	public BeanNode(int type, int[] data)
	{
		this.type = type;
		this.data = data;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int[] getData()
	{
		return data;
	}
}
