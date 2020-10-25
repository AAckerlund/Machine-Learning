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
		Printer.println("test"+i+".txt", "" + i);
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
