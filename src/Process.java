// Scheduling Simulator
// Barak Michaely , John Luo, Alvin Lawson

import java.util.Random;


public class Process {
	
	public int id;
	public long startTime, endTime;
	public long waitingTime=0, turnaroundTime=0;
	public long totalLength = 100;
	public long remainingTime = totalLength;
	public boolean iobound = false;
	public int currentBurst = 0;
	
	
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
	
	public void burst() {
		Random RandomGenerator = new Random();
		
		if (iobound) {
			// Range of 2k to 4k
			currentBurst = RandomGenerator.nextInt(2000) + 2000;
		} else {
			// Range of 6k to 12k
			currentBurst = RandomGenerator.nextInt(6000) + 6000;
		}
		
		if (currentBurst > remainingTime ) {
			currentBurst = (int) remainingTime;
		}
	}
	
	public void done(long end) {
		endTime = end;
		turnaroundTime = endTime - startTime;
		waitingTime = turnaroundTime - totalLength;
		if (turnaroundTime < 0) {
			turnaroundTime = 0;
		}
		if (waitingTime < 0) {
			waitingTime = 0;
		}
	}
}
