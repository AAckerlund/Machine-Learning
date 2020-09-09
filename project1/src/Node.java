public class Node
{
	private float id;
	private float[] data;
	public Node(float id, float[] data)
	{
		this.id = id;
		this.data = data;
	}
	
	public float getId()
	{
		return id;
	}
	
	public float[] getData()
	{
		return data;
	}
}
