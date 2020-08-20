package Nodes;

public class VoteNode
{
	boolean dr;	//true: democrat
				//false: republican
	int[] votes;//0: ?
				//1: y
				//2: n
	public VoteNode(boolean dr, int[] votes)
	{
		this.dr = dr;
		this.votes = votes;
	}
	
	public boolean getDR()
	{
		return dr;
	}
	
	public int[] getVotes()
	{
		return votes;
	}
}
