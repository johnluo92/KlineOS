import java.util.Random;


public class Process {
	
	public int id;
	public int startTime, endTime;
	public int waitingTime, turnaroundTime;
	public int totalLength = 100;
	public int remainingTime = totalLength;
	public boolean iobound = false;
	
	
	public Process(int tm, int identification,float iochance) {
		startTime = tm;
		id = identification;
		
		// Decide IO Bound
		Random RandomGenerator = new Random();
		int ioboundpercentage = RandomGenerator.nextInt(100);
		if (ioboundpercentage < iochance) {
			iobound = true;
		}
		
	}
	
	public void done(int end) {
		endTime = end;
		turnaroundTime = endTime - startTime;
		waitingTime = turnaroundTime - totalLength;
	}
}
