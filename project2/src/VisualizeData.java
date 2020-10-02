import java.awt.*;
import java.util.ArrayList;

public class VisualizeData extends Canvas
{
	ArrayList<Node> graph;
	int scale;
	public VisualizeData(ArrayList<Node> graph, int scale)
	{
		this.graph = graph;
		this.scale = scale;
	}
	
	public void paint(Graphics g)//This method just knows when it is needed and calls itself. Currently it gets called once the graph has been completely generated
	{
		int r = 10;//sets a radius for the nodes
		System.out.println("Size of data set: " + graph.size());
		for(Node n : graph)
		{
			//scale the nodes location by [scale] to make it more easily viewable
			int vX = (int) (n.getData()[0] * scale);
			int vY = (int) (n.getData()[1] * scale);
			
			//change the color of the node based on its given color
			//System.out.println(n.getId());
			switch((int) n.getId())
			{
				case 1 -> g.setColor(Color.red);
				case 2 -> g.setColor(Color.green);
				case 3 -> g.setColor(Color.blue);
				//should something go wrong a magenta colored node will appear
				default -> g.setColor(Color.magenta);
			}
			g.fillOval(vX-(r/2), vY-(r/2), r, r);//draws the nodes
		}
	}
}