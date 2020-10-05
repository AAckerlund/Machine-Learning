import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditedKNN
{
	public ArrayList<Node> editSet(ArrayList<Node> dataInput, int k)
	{
		boolean dataNotChanged = false;
		ArrayList<Node> data = new ArrayList<>(dataInput);	// copy input so original isn't modified
		while(!dataNotChanged)
		{
			dataNotChanged = true;

			Collections.shuffle(data);
			ArrayList<Node> nearest;
			
			for(int i = 0; i < data.size(); i++)
			{
				nearest = new ArrayList<>();
				for(int j = 0; j < data.size(); j++)
				{
					if(i == j)//don't want to check a node against itself
						continue;

					if(nearest.size() == 0)//since we can't calculate the distance to a null value we must have something in the nearest list
						nearest.add(data.get(j));

					if(nearest.size() > k)//error checking
					{
						System.out.println("nearest too big");
						nearest.remove(biggestDist(data.get(i), nearest));
					}
					//if the distance between the current node and one we are checking against is less than the distance between the current node and its current furthest neighbor
					if(Calc.dist(data.get(i).getData(), data.get(j).getData()) < Calc.dist(data.get(i).getData(), biggestDist(data.get(i), nearest).getData()))
					{

						nearest.add(data.get(j));
						if(nearest.size() > k)//error checking
						{
							nearest.remove(biggestDist(data.get(i), nearest));
						}
					}
				}
				if(nearest.size() == 0)//This should never happen.
				{
					System.err.println("No Nearest Neighbor detected while running Edited Nearest Neighbor on Node: " + data.get(i).getId() + Arrays.toString(data.get(i).getData()));
				}
				if(!sameValue(data.get(i), nearest))//differing values
				{
					System.out.println("Removed " + data.get(i).getId() + " At a distance of " + Calc.dist(data.get(i).getData(), biggestDist(data.get(i), nearest).getData()));
					data.remove(i);//remove the point
					i--;//subtract 1 from the iterator variable so we don't skip a data point.
					dataNotChanged = false;
				}
			}
			System.out.println("Data size " + data.size());
		}
		return data;
	}
	
	public Node biggestDist(Node curr, ArrayList<Node> data)
	{
		float furthest = -1;//no negative distances can occur
		int toRemove = 0;//tied to furthest
		for(int i = 0; i < data.size(); i++)
		{
			if(Calc.dist(curr.getData(), data.get(i).getData()) > furthest)
			{
				furthest = Calc.dist(curr.getData(), data.get(i).getData());
				toRemove = i;
			}
		}
		return data.get(toRemove);
	}

	public boolean sameValue(Node guess, ArrayList<Node> known)
	{
		ArrayList<Float> id = new ArrayList<>();//a list of all the ids in known
		ArrayList<Integer> frequency = new ArrayList<>();//the frequency of each id in known
		for(Node node : known)
		{
			if(!id.contains(node.getId()))//if this is the first occurrence of this id
			{
				id.add(node.getId());
				frequency.add(1);
			}
			else//find the id in id list and increase the frequency
			{
				for(int j = 0; j < id.size(); j++)
				{
					if(id.get(j) == node.getId())
					{
						frequency.set(j, frequency.get(j) + 1);//add 1 to the frequency
					}
				}
			}
		}

		int greatestFrequency = 0;//find the id(s) with the greatest frequency
		for(Integer integer : frequency)
		{
			if(integer > greatestFrequency)
				greatestFrequency = integer;
		}

		for(int i = 0; i < id.size(); i++)
		{
			if(frequency.get(i) == greatestFrequency)//this id has the greatest frequency
				if(id.get(i) == guess.getId())//they have the same id
					return true;
		}
		return false;
	}
}
