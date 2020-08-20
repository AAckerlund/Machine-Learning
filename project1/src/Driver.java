import Nodes.VoteNode;

import java.util.ArrayList;
import java.util.Arrays;

public class Driver
{
	public Driver(String filePath)
	{
		System.out.println(filePath);
		Parser p = new Parser();
		if(filePath.contains("house-votes"))
		{
			ArrayList<VoteNode> nodes = p.votesParser(filePath);
			for(VoteNode node : nodes)
			{
				System.out.println(node.getDR() + " " + Arrays.toString(node.getVotes()));
			}
		}
		
	}
	public static void main(String[] args)
	{
		String fileStart = "dataSets/", fileEnd = ".data";
		String[] files = {"breast-cancer-wisconsin", "glass", "house-votes-84", "iris", "soybean-small"};
		
		for(String file : files)//create a new instance of the driver for each of the data sets. TODO multi thread this to cut down on runtime.
		{
			new Driver(fileStart + file + fileEnd);
		}
	}
}
