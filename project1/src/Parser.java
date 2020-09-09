import MinMax.GlassDataMinMax;
import MinMax.IrisDataMinMax;
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

	// Special parser that uses the Discretizer class to change the data values into integers representing bins
	public ArrayList<Node> discreteParser(Discretizer discretizer, ArrayList<Node> nodes, double[][] minmax){
		for(int i = 0; i<nodes.size(); i++){
			for(int j = 0; j<nodes.get(i).getData().length; j++){
				float min = (float) minmax[j][0];
				float max = (float) minmax[j][1];
				float datapoint = nodes.get(i).getData()[j];
				int th = discretizer.getBin(datapoint, min, max);
				nodes.get(i).getData()[j] = th;
			}
		}
		return nodes;
	}
	
	//The below functions are all slightly different but all parse the data out from their respective files.
	public ArrayList<Node> beanParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			float type = Integer.parseInt(data[data.length-1].substring(1));
			float[] dataPoints = new float[data.length-1];
			for(int i = 0; i < data.length - 1; i++)
			{
				dataPoints[i] = Integer.parseInt(data[i]);
			}
			nodes.add(new Node(type, dataPoints));
		}
		return nodes;
	}
	
	public ArrayList<Node> irisParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			String typeString = data[data.length-1].substring(5);
			float type;
			switch(typeString)
			{
				case "setosa" -> type = 1;
				case "versicolor" -> type = 2;
				case "virginica" -> type = 3;
				
				default -> { //unknown type or an error happened
					type = (int) (Math.random() * 3) + 1;
					System.out.println("iris " + type);
				}
			}
			float[] dataPoints = new float[data.length-1];
			for(int i = 0; i < data.length-1; i++)
			{
				dataPoints[i] = Float.parseFloat(data[i]);
			}
			nodes.add(new Node(type, dataPoints));
		}
		Discretizer irisDiscretizer = new Discretizer(10);
		IrisDataMinMax irisstats = new IrisDataMinMax();
		double [][] minmax = irisstats.getGlassMinMaxList();
		nodes = discreteParser(irisDiscretizer, nodes, minmax);
		return nodes;
	}
	
	public ArrayList<Node> glassParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			float type = 0;
			try
			{
				type = Integer.parseInt(data[data.length-1]);
			}
			catch(NumberFormatException ignored){}
			
			float[] dataPoints = new float[data.length-2];
			for(int i = 1; i < data.length-1; i++)
			{
				dataPoints[i-1] = Float.parseFloat(data[i]);
			}
			nodes.add(new Node(type, dataPoints));
		}

		Discretizer glassDiscretizer = new Discretizer(10);
		GlassDataMinMax glassstats = new GlassDataMinMax();
		double [][] minmax = glassstats.getGlassMinMaxList();
		nodes = discreteParser(glassDiscretizer, nodes, minmax);
		return nodes;
	}
	
	public ArrayList<Node> cancerParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			float id = Integer.parseInt(data[data.length-1]);
			
			float[] dataPoints = new float[data.length-2];
			for(int i = 1; i < data.length-1; i++)
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
			nodes.add(new Node(id, dataPoints));
		}

		return nodes;
	}
	
	public ArrayList<Node> votesParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			//republican = 0
			//democrat = 1
			float dr = 0;
			if(data[0].equals("democrat"))
				dr = 1;
			
			float[] votes = new float[data.length-1];
			for(int i = 1; i < data.length; i++)
			{
				switch(data[i])
				{
					case ("y") -> votes[i - 1] = 1;
					case ("n") -> votes[i - 1] = 2;
					default -> votes[i - 1] = (int)(Math.random()*2)+1;//randomly generating unknown values
				}
			}
			nodes.add(new Node(dr, votes));
		}
		return nodes;
	}
}