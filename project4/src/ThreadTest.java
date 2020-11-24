import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest extends Thread
{
	int num;
	public ThreadTest(int num)
	{
		this.num = num;
	}
	
	public void run()
	{
		System.out.println(num);
	}
	
	public static void main(String[] args)
	{
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		Thread[] t = new Thread[10];
		int num = 0;
		for(int i = 0; i < 6; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				for(int k = 0; k < 3; k++)
				{
					for(int l = 0; l < 10; l++)
					{
						pool.execute(new ThreadTest(num));
						num++;
					}
					num++;
				}
				num++;
			}
			num++;
		}
		pool.shutdown();
		
		/*int threadCount = 90;
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < threadCount; j++)
			{
				t[j] = new Thread(new ThreadTest(i*100 + j));
				t[j].start();
			}
			for(int j = 0; j < t.length; j++)
			{
				t[j].join();
			}
		}*/
	}
}
