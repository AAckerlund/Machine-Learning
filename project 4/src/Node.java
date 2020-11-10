public class Node
{
	// Represents each entry in a dataset and contains the class number and an array of attributes
	private final double id;
	private final double[] data;
	private final int ignoredAttr;
	
	public Node(double id, double[] data, int ignoredAttr)//used for the following datasets: glass, house-votes, segmentation
	{
		this.id = id;
		this.data = data;
		this.ignoredAttr = ignoredAttr;
	}
	
	public int getIgnoredAttr()
	{
		return ignoredAttr;
	}
	
	public double getId()
	{
		return id;
	}
	
	public double[] getData()
	{
		return data;
	}
}