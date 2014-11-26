import java.util.*;

public class Process {

	Random RandomGenerator = new Random();
	
//		Decide when next process will be created (input)
//		Determine total CPU time (input)
//		Determine wether IO bound or CPU bound
//		Determine Length of First CPU Burst in mcs 
//			If IO bound cpuBurst = 2,000 - 4,000mcs
//			If CPU bound cpuBurst = 10,000 - 20,000mcs
	
	int IO = RandomGenerator.nextInt(4000) + 2000;
	int CPU = RandomGenerator.nextInt(20000) + 10000;
	int IOPercent = RandomGenerator.nextInt(100);{
		
	
	
	if (IOPercent <= 25)
	{
		//do something
	}
	
}}
