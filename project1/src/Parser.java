import Nodes.*;

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
	
	//The below functions are all slightly different but all parse the data out from their respective files.
	public ArrayList<BeanNode> beanParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<BeanNode> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			int type = Integer.parseInt(data[data.length-1].substring(1));
			int[] dataPoints = new int[data.length-1];
			for(int i = 0; i < data.length - 1; i++)
			{
				dataPoints[i] = Integer.parseInt(data[i]);
			}
			nodes.add(new BeanNode(type, dataPoints));
		}
		return nodes;
	}
	
	public ArrayList<IrisNode> irisParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<IrisNode> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			String type = data[data.length-1].substring(5);
			double[] dataPoints = new double[data.length-1];
			for(int i = 0; i < data.length-1; i++)
			{
				dataPoints[i] = Double.parseDouble(data[i]);
			}
			nodes.add(new IrisNode(type, dataPoints));
		}
		return nodes;
	}
	
	public ArrayList<GlassNode> glassParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<GlassNode> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			int id = Integer.parseInt(data[0]);
			int type = 0;
			try
			{
				type = Integer.parseInt(data[data.length-1]);
			}
			catch(NumberFormatException ignored){}
			
			double[] dataPoints = new double[data.length-2];
			for(int i = 1; i < data.length-1; i++)
			{
				dataPoints[i-1] = Double.parseDouble(data[i]);
			}
			nodes.add(new GlassNode(id, type, dataPoints));
		}
		return nodes;
	}
	
	public ArrayList<CancerNode> cancerParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<CancerNode> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			int id = Integer.parseInt(data[0]);
			
			int[] dataPoints = new int[data.length-1];
			for(int i = 1; i < data.length; i++)
			{
				try
				{
					dataPoints[i - 1] = Integer.parseInt(data[i]);
				}
				catch(NumberFormatException ex)
				{
					dataPoints[i-1] = (int)(Math.random()*10) + 1;
				}
			}
			nodes.add(new CancerNode(id, dataPoints));
		}
		return nodes;
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
					default -> votes[i - 1] = (int)(Math.random()*2)+1;//randomly generating unknown values
				}
			}
			nodes.add(new VoteNode(dr, votes));
		}
		return nodes;
	}
}
