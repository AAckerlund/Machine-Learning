import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class VisualizeData extends Canvas
{
	ArrayList<Node> graph;
	int scale;
	
	static HashMap<Integer, Color> colorMap = new HashMap<>();
	
	public VisualizeData(ArrayList<Node> graph, int scale)
	{
		this.graph = graph;
		this.scale = scale;
	}
	
	public void paint(Graphics g)//This method just knows when it is needed and calls itself. Currently it gets called once the graph has been completely generated
	{
		int r = 10;//sets a radius for the nodes
		//System.out.println("Size of data set: " + graph.size());
		for(float i = 0; i < (int)(getWidth()/scale); i += (float)1/scale)
		{
			for(float j = 0; j < (int)(getHeight()/scale); j += (float)1/scale)
			{
				if(colorMap.get((int) closestNode(i, j).getId()) == null)
				{
					int red = (int)(Math.random()*255);
					int green = (int)(Math.random()*255);
					int blue = (int)(Math.random()*255);
					colorMap.put((int) closestNode(i, j).getId(), new Color(red, green, blue));
				}
				g.setColor(colorMap.get((int) closestNode(i, j).getId()));
				
				/*switch((int) closestNode(i, j).getId())
				{
					case 0 -> g.setColor(Color.yellow);
					case 1 -> g.setColor(Color.red);
					case 2 -> g.setColor(Color.green);
					case 3 -> g.setColor(Color.blue);
					//should something go wrong a magenta colored node will appear
					default -> g.setColor(Color.magenta);
				}*/
				g.fillRect((int)(i * scale), (int)(j * scale), 1, 1);//paint each pixel
				
			}
			
		}
		for(Node n : graph)
		{
			//scale the nodes location by [scale] to make it more easily viewable
			int vX = (int) (n.getData()[0] * scale);
			int vY = (int) (n.getData()[1] * scale);
			
			//change the color of the node based on its given color
			//System.out.println(n.getId());
			/*switch((int) n.getId())
			{
				case 1 -> g.setColor(Color.red);
				case 2 -> g.setColor(Color.green);
				case 3 -> g.setColor(Color.blue);
				//should something go wrong a magenta colored node will appear
				default -> g.setColor(Color.magenta);
			}*/
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
		KMeansClustering calc = new KMeansClustering();
		
		for(Node n : graph)
		{
			if(calc.dist(new float[] {x, y}, n.getData()) < minDist)
			{
				closest = n;
				minDist = calc.dist(new float[] {x, y}, n.getData());
			}
		}
		return closest;
	}
}