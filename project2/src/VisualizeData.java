import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class VisualizeData extends Canvas
{
	ArrayList<Node> graph;
	float[] minAttrValue, maxAttrValue;
	float minIDValue, maxIDValue;
	
	static HashMap<Integer, Color> colorMap = new HashMap<>();
	
	public VisualizeData(ArrayList<Node> graph)
	{
		this.graph = graph;
		minAttrValue = new float[2];
		maxAttrValue = new float[2];
		findMinMax(graph);
	}
	
	public void paint(Graphics g)//This method just knows when it is needed and calls itself. Currently it gets called once the graph has been completely generated
	{
		int r = 5;//sets a radius for the nodes
		for(float i = 0; i < (maxAttrValue[0] - minAttrValue[0]) + 20; i += (maxAttrValue[0] - minAttrValue[0])/getWidth())
		{
			for(float j = 0; j < (maxAttrValue[1] - minAttrValue[1]) + 20; j += (maxAttrValue[1] - minAttrValue[1])/getHeight())
			{
				if(colorMap.get((int) closestNode(i, j).getId()) == null)
				{
					int red = (int)(Math.random()*255);
					int green = (int)(Math.random()*255);
					int blue = (int)(Math.random()*255);
					colorMap.put((int) closestNode(i, j).getId(), new Color(red, green, blue));
				}
				g.setColor(colorMap.get((int) closestNode(i, j).getId()));
				int x = (int)((i - minAttrValue[0])/(maxAttrValue[0] - minAttrValue[0]) * getWidth());
				int y = (int)((j - minAttrValue[1])/(maxAttrValue[1] - minAttrValue[1]) * getHeight());
				g.fillRect(x, y, 1, 1);//paint each pixel
			}
		}
		for(Node n : graph)
		{
			//scale the nodes location by [scale] to make it more easily viewable
			int vX = (int)((n.getData()[0] - minAttrValue[0])/(maxAttrValue[0] - minAttrValue[0]) * getWidth());
			int vY = (int)((n.getData()[1] - minAttrValue[1])/(maxAttrValue[1] - minAttrValue[1]) * getHeight());
			
			//change the color of the node based on its given color
			g.setColor(colorMap.get((int) n.getId()));
			g.fillOval(vX-(r/2), vY-(r/2), r, r);//draws the nodes
			g.setColor(Color.black);
			g.drawOval(vX-(r/2), vY-(r/2), r, r);//draws a black outline so we can fill in the background and not lose the points
		}
	}
	
	public Node closestNode(float x, float y)
	{
		Node closest = null;
		float minDist = Float.MAX_VALUE;
		
		for(Node n : graph)
		{
			if(Calc.dist(new float[] {x, y}, n.getData()) < minDist)
			{
				closest = n;
				minDist = Calc.dist(new float[] {x, y}, n.getData());
			}
		}
		return closest;
	}
	
	//finds min and max values so the data can be better displayed
	public void findMinMax(ArrayList<Node> data)
	{
		minIDValue = Float.MAX_VALUE;
		maxIDValue = Float.MIN_VALUE;
		
		for(int i = 0; i < 2; i++)
		{
			minAttrValue[i] = Float.MAX_VALUE;
			maxAttrValue[i] = Float.MIN_VALUE;
		}
		
		for(Node datum : data)
		{
			if(datum.getId() < minIDValue)
				minIDValue = datum.getId();
			if(datum.getId() > maxIDValue)
				maxIDValue = datum.getId();
			
			for(int j = 0; j < 2; j++)
			{
				if(datum.getData()[j] < minAttrValue[j])
					minAttrValue[j] = datum.getData()[j];
				if(datum.getData()[j] > maxAttrValue[j])
					maxAttrValue[j] = datum.getData()[j];
			}
		}
	}
}