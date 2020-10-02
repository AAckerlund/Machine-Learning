import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditedKNN
{
	public ArrayList<Node> editSet(ArrayList<Node> data)//TODO: make this work with k nearest neighbors, not just 1 nearest neighbor
	{
		boolean dataNotChanged = false;
		while(!dataNotChanged)
		{
			dataNotChanged = true;
			Collections.shuffle(data);//randomize the order of the data each time to avoid predictable elimination
			float minDist;
			Node nearest;
			KMeansClustering calc = new KMeansClustering();
			
			for(int i = 0; i < data.size(); i++)
			{
				//reset these values for each node we look at.
				minDist = Float.MAX_VALUE;
				nearest = null;
				for(int j = 0; j < data.size(); j++)
				{
					if(i == j)
						continue;
					if(Calc.dist(data.get(i).getData(), data.get(j).getData()) < minDist)//if we found a closer data point to the one we are looking at
					{
						minDist = Calc.dist(data.get(i).getData(), data.get(j).getData());
						nearest = data.get(j);
					}
				}
				//System.out.println("Nearest node is: " + Arrays.toString(nearest.getData()) + "\nIt is " + minDist + " away.");
				if(nearest == null)//This should never happen.
				{
					System.err.println("No Nearest Neighbor detected while running Edited Nearest Neighbor on Node: " + data.get(i).getId() + Arrays.toString(data.get(i).getData()));
				}
				if(!sameValue(data.get(i), nearest))//differing values
				{
					System.out.println("Removed " + data.get(i).getId() + " When compared to " + nearest.getId() + " At a distance of " + minDist);
					data.remove(i);//remove the point
					i--;//subtract 1 from the iterator variable so we don't skip a data point.
					dataNotChanged = false;
				}
			}
		}
		return data;
	}
	
	public boolean sameValue(Node guess, Node known)
	{
		return guess.getId() == known.getId();
	}
}
