import Nodes.VoteNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser
{
	/**
	 * initializes a scanner for the given file
	 * @param filePath the path to the file the scanner is attached to.
	 * @return an initialized scanner, or null if the scanner failed to initialize.
	 */
	public Scanner initScanner(String filePath)
	{
		File file = new File(filePath);
		try
		{
			return new Scanner(file);
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("Bad file path. The path given was " + filePath);
		}
		return null;
	}
	
	public ArrayList<VoteNode> votesParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<VoteNode> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			boolean dr = false;
			if(data[0].equals("democrat"))
				dr = true;
			
			int[] votes = new int[data.length-1];
			for(int i = 1; i < data.length; i++)
			{
				switch(data[i])
				{
					case ("y") -> votes[i - 1] = 1;
					case ("n") -> votes[i - 1] = 2;
					default -> votes[i - 1] = 0;
				}
			}
			nodes.add(new VoteNode(dr, votes));
		}
		return nodes;
	}
}
