import java.util.Random;


public class Process {
	
	public int id;
	public long startTime, endTime;
	public long waitingTime, turnaroundTime;
	public long totalLength = 100;
	public long remainingTime = totalLength;
	public boolean iobound = false;
	
	
	public Process(long tm, int identification,float iochance,int length) {
		startTime = tm;
		id = identification;
		
		// Decide IO Bound
		Random RandomGenerator = new Random();
		int ioboundpercentage = RandomGenerator.nextInt(100);
		if (ioboundpercentage < iochance) {
			iobound = true;
		}
		
		// Random Length
		totalLength = Generator.randomRange(length);
		remainingTime = totalLength;
		
		
	}
	
	public void done(long end) {
		endTime = end;
		turnaroundTime = endTime - startTime;
		waitingTime = turnaroundTime - totalLength;
	}
}
