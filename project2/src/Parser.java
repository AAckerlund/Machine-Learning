import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser
{
	float[] minList, maxList;
	Driver d;
	public Parser(Driver d)
	{
		this.d = d;
	}
	/**
	 * initializes a scanner for the given file
	 *
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
	
	public ArrayList<Node> normData(ArrayList<Node> list)
	{
		//initialize variables for normalization
		minList = new float[list.get(0).getData().length];
		maxList = new float[list.get(0).getData().length];
		for(int i = 0; i < list.get(0).getData().length; i++)
		{
			minList[i] = Float.MAX_VALUE;
			maxList[i] = Float.MIN_VALUE;
		}
		
		//find min and max values for each attribute
		for(Node node : list)
		{
			//find min/max for attributes
			for(int j = 0; j < node.getData().length; j++)
			{
				if(minList[j] > node.getData()[j])
				{
					minList[j] = node.getData()[j];
				}
				if(maxList[j] < node.getData()[j])
				{
					maxList[j] = node.getData()[j];
				}
			}
		}
		//scale the values of each attribute to be between 0 and 1
		for(Node node : list)
		{
			for(int j = 0; j < node.getData().length; j++)
			{
				node.getData()[j] = (node.getData()[j] - minList[j]) / (maxList[j] - minList[j]);
			}
		}
		return list;
	}
	
	public float deNormAttr(float value, int attr)
	{
		return value*(maxList[attr] - minList[attr]) + minList[attr];
	}
	
	//The below functions are all slightly different but all parse the data out from their respective files.
	public ArrayList<Node> glassParser(String filePath) throws FileNotFoundException
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			//read in one element of the data set
			String line = in.next();
			String[] data = line.split(",");
			
			//try and set the id. If we cant then set it to 0.
			float type = 0;
			try
			{
				type = Integer.parseInt(data[data.length - 1]);
			}
			catch(NumberFormatException ignored){}
			
			
			float[] dataPoints = new float[data.length - 2];
			for(int i = 1; i < data.length - 1; i++)
			{
				dataPoints[i - 1] = Float.parseFloat(data[i]);
			}
			nodes.add(new Node(type, dataPoints, -1));
		}
		return normData(nodes);
	}
	
	public ArrayList<Node> votesParser(String filePath)  throws FileNotFoundException
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
			
			float[] votes = new float[data.length - 1];
			for(int i = 1; i < data.length; i++)
			{
				switch(data[i])//attempt to set a value for each vote. If not possible, randomize the value
				{
					case ("y") -> votes[i - 1] = 1;
					case ("n") -> votes[i - 1] = 2;
					default -> votes[i - 1] = (int) (Math.random() * 2) + 1;//randomly generating unknown values
				}
			}
			nodes.add(new Node(dr, votes, -1));
		}
		return normData(nodes);
	}
	
	public ArrayList<Node> abaloneParser(String filePath) throws FileNotFoundException
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			
			float[] attributes = new float[data.length - 1];
			for(int i = 0; i < attributes.length; i++)
			{
				attributes[i] = Float.parseFloat(data[i + 1]);
			}
			
			nodes.add(new Node(attributes[attributes.length - 1], attributes, attributes.length - 1));
		}
		return normData(nodes);
	}
	
	public ArrayList<Node> firesParser(String filePath) throws FileNotFoundException
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		String line = in.next();//the first line is not data so skip
		
		//setting up some hashmaps for fast data parsing
		HashMap<String, Integer> months = new HashMap<>();
		months.put("jan", 0);
		months.put("feb", 1);
		months.put("mar", 2);
		months.put("apr", 3);
		months.put("may", 4);
		months.put("jun", 5);
		months.put("jul", 6);
		months.put("aug", 7);
		months.put("sep", 8);
		months.put("oct", 9);
		months.put("nov", 10);
		months.put("dec", 11);
		
		HashMap<String, Integer> days = new HashMap<>();
		days.put("mon", 0);
		days.put("tue", 1);
		days.put("wed", 2);
		days.put("thu", 3);
		days.put("fri", 4);
		days.put("sat", 5);
		days.put("sun", 6);
		
		while(in.hasNext())
		{
			line = in.next();
			String[] data = line.split(",");
			
			float[] attributes = new float[data.length];
			
			attributes[2] = months.get(data[2]);
			attributes[3] = days.get(data[3]);
			
			attributes[0] = Float.parseFloat(data[0]);
			attributes[1] = Float.parseFloat(data[1]);
			for(int i = 4; i < attributes.length; i++)
			{
				attributes[i] = Float.parseFloat(data[i]);
			}
			nodes.add(new Node(attributes[attributes.length - 1], attributes, attributes.length - 1));
		}
		return normData(nodes);
	}
	
	public ArrayList<Node> machineParser(String filePath) throws FileNotFoundException
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		
		HashMap<String, Integer> vendor = new HashMap<>();
		HashMap<String, Integer> model = new HashMap<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			float[] attributes = new float[data.length];
			for(int i = 2; i < attributes.length; i++)
			{
				attributes[i] = Float.parseFloat(data[i]);
			}
			
			//due to the lengthy list of possible values we dynamically add them to a hashmap
			if(vendor.get(data[0]) == null)
				vendor.put(data[0], vendor.size());
			attributes[0] = vendor.get(data[0]);
			
			if(model.get(data[1]) == null)
				model.put(data[1], model.size());
			attributes[1] = model.get(data[1]);
			
			nodes.add(new Node(attributes[attributes.length - 2], attributes, attributes.length - 2));
		}
		return normData(nodes);
	}
	
	public ArrayList<Node> segmentationParser(String filePath)
	{
		Scanner in = initScanner(filePath);
		ArrayList<Node> nodes = new ArrayList<>();
		
		for(int i = 0; i < 4; i++)//first few lines are not data, so skip them
		{
			in.nextLine();
		}
		
		HashMap<String, Integer> classes = new HashMap<>();
		while(in.hasNext())
		{
			String line = in.next();
			String[] data = line.split(",");
			float id;
			float[] attributes = new float[data.length - 1];
			
			if(classes.get(data[0]) == null)
				classes.put(data[0], classes.size());
			id = classes.get(data[0]);
			
			for(int i = 1; i < data.length; i++)
			{
				attributes[i - 1] = Float.parseFloat(data[i]);
			}
			nodes.add(new Node(id, attributes, -1));
		}
		return normData(nodes);
	}
}