import java.util.*;

public class Process2 {
	
	private int round_Robin, burstTime, startTime, number, capacity;
	private Process[] queue;
	

public Process()
{
	number = 0;
	burstTime = 0;
	startTime = 0;
	queue = new Process[capacity];
}

public void addToQueue(int pnumber, int burst, int start, int quantity)
{
	if (queue[capacity-1] !=null)
	increaseSize();

queue[number] = new Process[pnumber, burst, start];

}

	Random RandomGenerator = new Random();
	
//		Decide when next process will be created (input)
//		Determine total CPU time (input)
//		Determine wether IO bound or CPU bound
//		Determine Length of First CPU Burst in mcs 
//			If IO bound cpuBurst = 2,000 - 4,000mcs
//			If CPU bound cpuBurst = 10,000 - 20,000mcs
	
	int IO = RandomGenerator.nextInt(4000) + 2000;
	int CPU = RandomGenerator.nextInt(20000) + 10000;
	int IOPercent = RandomGenerator.nextInt(100);
		
	if (IOPercent <= 25)
	{
		int process = IO;
	}
	
	private void increaseSize()
	{
		//fill in your code here to declare a new array with larger size
		capacity +=3;
		Process[]temp=new Process[capacity];
		
		for (int i = 0; i < queue.length; i++){
			temp[i]=Process[i];
		}
		
		queue = temp;
			
	}

}
}