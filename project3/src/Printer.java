import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Printer
{
	private static final Printer inst = new Printer();
	
	Printer()
	{
		super();
	}
	
	void println(String file, String str)
	{
		print(file, str + "\n");
	}
	synchronized void print(String file, String str)
	{
		File f = new File(file);
		FileWriter out = null;
		try
		{
			out = new FileWriter(f, true);
		}
		catch(IOException ex)
		{
			System.out.println("Error creating File Writer");
		}
		
		try
		{
			out.write(str);
		}
		catch(IOException ex)
		{
			System.out.println("Error writing to file");
		}
		
		try
		{
			out.close();
		}
		catch(IOException ex)
		{
			System.out.println("Error closing File Writer");
		}
	}
	
	public Printer getInst()
	{
		return inst;
	}
}
