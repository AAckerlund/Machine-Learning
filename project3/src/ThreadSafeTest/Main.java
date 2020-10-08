package ThreadSafeTest;

public class Main
{
	int i;
	public Main(int i)
	{
		this.i = i;
	}
	
	public void run()
	{
		Printer s = new Printer();
		s.getInst().print("test.txt", "" + i);
	}
	
	public static void main(String[] args)
	{
		for(int i = 0; i < 5; i++)
		{
			Main m = new Main(i);
			m.run();
		}
		System.err.println("done");
	}
}
