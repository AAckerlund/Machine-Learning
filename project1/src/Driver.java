import Nodes.*;

import java.util.ArrayList;
import java.util.Arrays;//used in printing out the parsed data

public class Driver extends Thread//extending Thread allows for multithreading
{
	String fileStart = "dataSets/", fileEnd = ".data", filePath;
	
	public Driver(String filePath)
	{
		this.filePath = filePath;
	}
		
	public void run()//the method that is called when a Thread starts
	{
		//parse out the data in the file
		System.out.println(filePath);
		Parser p = new Parser();
		switch(filePath)
		{
			case "house-votes-84":
				ArrayList<VoteNode> vNodes = p.votesParser(fileStart + filePath + fileEnd);
				/*System.out.println("Done 1");
				for(VoteNode node : vNodes)
				{
					System.out.println(node.getDR() + " " + Arrays.toString(node.getVotes()));
				}*/
				break;
			case "breast-cancer-wisconsin":
				ArrayList<CancerNode> cNodes = p.cancerParser(fileStart + filePath + fileEnd);
				/*System.out.println("Done 2");
				for(CancerNode node : cNodes)
				{
					System.out.println(node.getId() + Arrays.toString(node.getData()));
				}*/
				break;
			case "glass":
				ArrayList<GlassNode> gNodes = p.glassParser(fileStart + filePath + fileEnd);
				/*System.out.println("Done 3");
				for(GlassNode node : gNodes)
				{
					System.out.println(node.getId() + Arrays.toString(node.getData()) + node.getGlassType());
				}*/
				break;
			case "iris":
				ArrayList<IrisNode> iNodes = p.irisParser(fileStart + filePath + fileEnd);
				/*System.out.println("Done 4");
				for(IrisNode node : iNodes)
				{
					System.out.println(node.getType() + Arrays.toString(node.getData()));
				}*/
				break;
			case "soybean-small":
				ArrayList<BeanNode> bNodes = p.beanParser(fileStart + filePath + fileEnd);
				/*System.out.println("Done 5");
				for(BeanNode node : bNodes)
				{
					System.out.println(node.getType() + Arrays.toString(node.getData()));
				}*/
				break;
			default:
				System.out.println("Bad file path: " + filePath);
		}
	}
	
	public static void main(String[] args)
	{
		String[] files = {"breast-cancer-wisconsin", "glass", "house-votes-84", "iris", "soybean-small"};
		
		for(String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			d.start();//Starts a new thread
		}
	}
}
