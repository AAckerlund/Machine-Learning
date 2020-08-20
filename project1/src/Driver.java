import Nodes.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Driver
{
	String fileStart = "dataSets/", fileEnd = ".data";
	
	public Driver(String filePath)
	{
		System.out.println(filePath);
		Parser p = new Parser();
		if(filePath.equals("house-votes-84"))
		{
			ArrayList<VoteNode> vNodes = p.votesParser(fileStart + filePath + fileEnd);
			/*for(VoteNode node : vNodes)
			{
				System.out.println(node.getDR() + " " + Arrays.toString(node.getVotes()));
			}*/
		}
		else if(filePath.equals("breast-cancer-wisconsin"))
		{
			ArrayList<CancerNode> cNodes = p.cancerParser(fileStart + filePath + fileEnd);
			/*for(CancerNode node : cNodes)
			{
				System.out.println(node.getId() + Arrays.toString(node.getData()));
			}*/
		}
		else if(filePath.equals("glass"))
		{
			ArrayList<GlassNode> gNodes = p.glassParser(fileStart + filePath + fileEnd);
			/*for(GlassNode node : gNodes)
			{
				System.out.println(node.getId() + Arrays.toString(node.getData()) + node.getGlassType());
			}*/
		}
		else if(filePath.equals("iris"))
		{
			ArrayList<IrisNode> iNodes = p.irisParser(fileStart + filePath + fileEnd);
			/*for(IrisNode node : iNodes)
			{
				System.out.println(node.getType() + Arrays.toString(node.getData()));
			}*/
		}
		else if(filePath.equals("soybean-small"))
		{
			ArrayList<BeanNode> bNodes = p.beanParser(fileStart + filePath + fileEnd);
			/*for(BeanNode node : bNodes)
			{
				System.out.println(node.getType() + Arrays.toString(node.getData()));
			}*/
		}
		
	}
	public static void main(String[] args)
	{
		String[] files = {"breast-cancer-wisconsin", "glass", "house-votes-84", "iris", "soybean-small"};
		
		for(String file : files)//create a new instance of the driver for each of the data sets. TODO multi thread this to cut down on runtime.
		{
			new Driver(file);
		}
	}
}
