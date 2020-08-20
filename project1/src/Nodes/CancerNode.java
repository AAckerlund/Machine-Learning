package Nodes;

public class CancerNode
{
	int id;
	int[] data;
	public CancerNode(int id, int[] data)
	{
		this.id = id;
		this.data = data;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int[] getData()
	{
		return data;
	}
}
