public class Node
{
	// Represents each entry in a dataset and contains the class number and an array of attributes
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
	
	public void setId(float id)
	{
		this.id = id;
	}
	
	public float[] getData()
	{
		return data;
	}

	public void setData(float[] data) {
		this.data = data;
	}
}
