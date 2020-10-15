public class Node
{
	// Represents each entry in a dataset and contains the class number and an array of attributes
	private float id;
	private float[] data;
	private final int ignoredAttr;
	
	public Node(float id, float[] data, int ignoredAttr)//used for the following datasets: glass, house-votes, segmentation
	{
		this.id = id;
		this.data = data;
		this.ignoredAttr = ignoredAttr;
	}
	
	public int getIgnoredAttr()
	{
		return ignoredAttr;
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
