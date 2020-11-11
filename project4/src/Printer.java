import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//This class allows for Thread safe, multi-file writing
//Call Printer.println() or Printer.print() to use. It will do the rest!
public class Printer
{
	static void println(String file, String str)
	{
		print(file, str + "\n");
	}
	
	static synchronized void print(String file, String str)
	{
		File f = new File(file + ".txt");
		FileWriter out = null;
		try
		{
			out = new FileWriter(f, true);
		}
		catch(IOException ex)
		{
			System.err.println("Error creating File Writer on thread " + file);
		}
		
		try
		{
			out.write(str);
		}
		catch(IOException ex)
		{
			System.err.println("Error writing message \"" + str + "\" to file on thread " + file);
		}
		
		try
		{
			out.close();
		}
		catch(IOException ex)
		{
			System.err.println("Error closing File Writer on thread " + file);
		}
	}
}
