// Scheduling Simulator
// Barak Michaely , John Luo, Alvin Lawson

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Driver {

	// INPUT VALUES
	static long simulationTime = 10000; // Seconds
	static int quantum=100; 
	static int contextSwitchTime=0;
	static int avgProcessLength=0;
	static int avgTimeBetweenProcesses=0;
	static int avgIOServiceTime=0;
	static int ioBoundPercentage=0;
	
	// Q's
	static ArrayList<Event> eventQ = new ArrayList<Event>();
	static ArrayList<Process> readyQ = new ArrayList<Process>();
	static ArrayList<Process> CPU = new ArrayList<Process>();
	static ArrayList<Process> ioQ = new ArrayList<Process>();
	
	static long clock = 0;
	
	static long lastClock = 0;
	static int CPUavgTurnaround = 0,IOavgTurnaround = 0,CPUavgWaiting = 0,IOavgWaiting = 0,doneCount = 0,totalContextSwitch=0,CPUavgService=0,IOavgService=0,CPUavgIO=0,IOavgIO=0;
	static int ioCount=0,cpuCount=0,CPUcpuTime=0,IOcpuTime=0,IOinterrupt=0,CPUinterrupt=0;
	static long cpuUtilization = 0;
	
	static int gc = 0;
	
	static int lastID = 0;
	
	public static void main(String[] args) {
		//Read Input Parameters
		readInput();
		
		//System.out.println("SIMULATION\n");
		// Create a Process
		Event createevent = new Event(0,clock);
		
		// Move that process to CPU
		Event movetoCPUevent = new Event(1,clock);

		// Run CPu Process
		Event startevent = new Event(4,clock);
		eventQ.add(createevent);
		//eventQ.add(createevent);
		eventQ.add(movetoCPUevent);
		//eventQ.add(startevent);
		doEvent();
		
	}
	
	public static void readInput() {
		// Parse Input File
		File file = new File("input.txt");
		Scanner scan = null;
		
		try {
			scan = new Scanner(file);
		}	
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("INPUT PARAMETERS\n");
		
		int count = 1;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			int input = 0;
			if (scan.hasNextInt()) {
				input =  scan.nextInt();
			} else {
				break;
			}
			if (count != 0) {
				//input = scan.nextInt();//Integer.parseInt(line);
			}
			if (count == 1) {
				System.out.println("Simulation Time: " + input);
				simulationTime = input * 1000 * 1000;
			} else if (count == 2) {
				System.out.println("Quantum is: " + input);
				quantum = input;
			} else if (count == 3) {
				System.out.println("Context switch: " + input);
				contextSwitchTime = input;
			} else if (count == 4) {
				System.out.println("Avg p length: " + input);
				avgProcessLength = input;
			} else if (count == 5) {
				System.out.println("Avg time between processes: " + input);
				avgTimeBetweenProcesses = input;
			} else if (count == 6) {
				System.out.println("io percentage: " + input);
				ioBoundPercentage = input;
			} else if (count == 7) {
				System.out.println("Avg IO Interrupt: " + input);
				avgIOServiceTime = input;
			}
			
			count++;
		}
		System.out.println("\n");
	}
	
	public static void doEvent() {
		Collections.sort(eventQ);
		
		boolean done = false;
		Event event = new Event(0,0);
		
		if (eventQ.size() == 0) {
			done = true;
		} else {
			event = eventQ.remove(0);
		}
		
		//System.out.println("Event Type:  " + event.type);
		
		// Advance Clock to next event
		if (clock < event.timestamp) {
			clock = event.timestamp;
		}
		
		if (clock >= simulationTime) {
			done = true;
		}
		
		if (done) {
			//Simulation is done
			clock = simulationTime;
			report();
			
			// Empty every queue
			eventQ.clear();
			readyQ.clear();
			CPU.clear();
			
			return;
		}
		
		
		
		
		////System.out.println(eventQ.size());
		////System.out.println("Event type: " + event.type + ", Event stamp: " + event.timestamp);
		
		switch (event.type) {
		
		// Create a new Process
		case 0: 
			createProcess();
		break;
		// Move ready process to CPU
		case 1:
			////System.out.println("Moving to CPU");
			////System.out.print("\ncase: 1");
			if (readyQ.size() == 0) {
				break;
			}
			CPU.add(readyQ.remove(0));
			//System.out.println("SIZE OF CPU:   " + CPU.size());
			System.out.println("Time: " + clock + "\t" + "Moved process #" + CPU.get(0).id + " to CPU");
		break;
		
		// Switch Processes
		case 2:
			////System.out.println("\ncase: 2 (Quantum Switch)");
			
//				if (avgCPUTime > 0) {
//					avgCPUTime += clock - lastClock;
//					avgCPUTime *= 0.5;
//				} else {
//					avgCPUTime += clock - lastClock;
//				}
//				lastClock = clock;
				// move CPU process back to queue
				// move ready process to cpu
				
				if (CPU.size() == 0 && readyQ.size() == 0) {
					break;
				}
				
				
				
				//System.out.println("Switching Processes");
				////System.out.println("event stamp is: " + event.timestamp);
				Event moveback = new Event(3,clock);
				Event movetoCPU = new Event(1,clock);
				
				clock += contextSwitchTime; // Context Switch Time
				totalContextSwitch += contextSwitchTime;
				
				Event run = new Event(4,clock);
				eventQ.add(moveback);
				eventQ.add(movetoCPU);
				eventQ.add(run);
				
		break;
		
		// Move from CPU to readyQ
		case 3: 
			////System.out.println("Emptying CPU");
			if (CPU.size() == 0) {
				////System.out.println("Empty CPU");
				break;
			}
			System.out.println("Time: " + clock + "\t" + "Moving process #" + CPU.get(0).id + " from CPU back to Ready Queue");
			readyQ.add(CPU.remove(0));
			
		break;
		
		// Actually run process
		case 4:
			////System.out.print("\ncase: 4");
			
			//System.out.println("Time: " + clock + "\t" + "Running process #" + CPU.get(0).id);
			runProcess();
			
		break;
		case 5:
			////System.out.println("\ncase: 5");
			//Process Done
			System.out.println("Time: " + clock + "\t" + "Process #" + CPU.get(0).id + " is Done!");
			System.out.println("Time: " + clock + "\t" + "Process #" + CPU.get(0).id + " removed from CPU");
			CPU.remove(0);
			
			doneCount ++;
			
			Event switchp2 = new Event(2,clock);
			eventQ.add(switchp2);
			
			
		break;
		case 6:
			// IO INTERRUPT
			
			// Set IO Completion time for this process
			int completion = Generator.randomRange(avgIOServiceTime);
			Event iocomplete = new Event(7,clock+completion);
			eventQ.add(iocomplete);
			
			// Stats
			if (CPU.get(0).iobound) {
				IOavgService += completion; 
				IOinterrupt ++;
			} else {
				CPUavgService += completion;
				CPUinterrupt ++;
			}
			
			// move from cpu to io queue
			System.out.println("Time: " + clock + " \t IO Interrupt \t id=" + CPU.get(0).id);
			ioQ.add(CPU.remove(0));
			
			// Switch Processes
			Event switchp = new Event(2,clock);
			eventQ.add(switchp);
			
		break;
		case 7:
			// IO Complete
			System.out.println("Time: " + clock + " \t IO Completed! \t id=" + ioQ.get(0).id);
			
			// Move back to ready queue
			readyQ.add(ioQ.remove(0));
			
			if (CPU.size() == 0 && readyQ.size() == 1) {
				Event pswitch = new Event(2,clock);
				eventQ.add(pswitch);
			}
			
		break;
		}
		
//		Thread.currentThread();
//		try {
//			Thread.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if (clock > 10000 * gc) {
////			Thread.currentThread();
////			try {
////				Thread.sleep(10);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
//			gc ++;
//			System.out.println("GARBAGE");
//			System.gc();
//		}

		doEvent();
	}
	
	public static void runProcess() {
		// Decrease remaining time of process in the CPU
		Process tempP = CPU.get(0);
		tempP.burst();
		long preclock = clock;
		
		System.out.println("Time: " + clock + "\t" + "Running Process #" + tempP.id + "(burst: " + tempP.currentBurst + ")");
		
		// Check if simulation is done before process is done running
		
		if (tempP.currentBurst <= quantum) {
			if (tempP.remainingTime <= quantum) {
				// Process Done
				if (tempP.iobound) {
					ioCount++;
				} else {
					cpuCount++;
				}
				
				// Advance Clock by remaining time
				clock += tempP.remainingTime;
				
				tempP.remainingTime = 0;
				
				
				
				// Record Statistics
				if (tempP.iobound) {
					IOcpuTime += tempP.remainingTime;
				} else {
					CPUcpuTime += tempP.remainingTime;
				}
				
				tempP.done(clock);
				
				if (tempP.iobound) {
					IOavgTurnaround += tempP.turnaroundTime;
					IOavgWaiting += tempP.waitingTime;
				} else {
					CPUavgTurnaround += tempP.turnaroundTime;
					CPUavgWaiting += tempP.waitingTime;
				}
				
				// Create a Process Done Event, and add it to eventQ
				Event doneEvent = new Event(5,clock);
				eventQ.add(doneEvent);
			} else {
				// Advance clock by burst
				clock += tempP.currentBurst;
				tempP.remainingTime -= tempP.currentBurst;
				
				//Stats
				if (tempP.iobound) {
					IOcpuTime += tempP.currentBurst;
				} else {
					CPUcpuTime += tempP.currentBurst;
				}
				
				// Do IO stuff
				Event iointerrupt = new Event(6,clock);
				eventQ.add(iointerrupt);
				
			}
		} else {
			// Advance Clock by quantum
			clock += quantum;
			tempP.remainingTime -= quantum;
			
			//Stats
			if (tempP.iobound) {
				IOcpuTime += quantum;
			} else {
				CPUcpuTime += quantum;
			}
			
			//Create a Quantum Switch Event
			Event switchEvent = new Event(2,clock);
			eventQ.add(switchEvent);
		}
		
		cpuUtilization += (clock-preclock);

	}
	
	public static void createProcess() {
		
		Process p = new Process(clock,lastID,ioBoundPercentage,avgProcessLength);
		
		// If there are no others in the system, Move to CPU and Run
		if (CPU.size() == 0 && readyQ.size() == 0) {
			System.out.println("S'all empty");
			readyQ.add(p);
			
			Event runProcess = new Event(4,clock);
			Event movetocpu = new Event(1,clock);
			
			eventQ.add(movetocpu);
			eventQ.add(runProcess);
			//System.out.println("LAST ONE STANDING");
		} else {
			readyQ.add(p);
		}
		
		lastID ++;
		
		
		System.out.println("Time: " + clock + " \t" + "Created Process #" + p.id + "  (Total Length: " + p.totalLength + ", ioBound=" + p.iobound+")");
		
		// Create a new Create Process Event at a future time
		
		if (lastID < 1000) {
			int futuretime =  Generator.randomRange(avgTimeBetweenProcesses); // Random Number
			Event newProcess = new Event(0,clock + futuretime);
			eventQ.add(newProcess);
		}
	}
	
	public static void report() {
		// Print out all the statistics
		System.out.println("\nSIMULATION REPORT \n");
		
		
		CPUavgTurnaround = Math.abs(CPUavgTurnaround);
		IOavgTurnaround = Math.abs(IOavgTurnaround);
		CPUavgWaiting = Math.abs(CPUavgWaiting);
		IOavgWaiting = Math.abs(IOavgWaiting);
		
		CPUavgTurnaround /= cpuCount;
		CPUavgWaiting /= cpuCount;
		CPUavgService /= cpuCount;
		CPUcpuTime /= cpuCount;
		
		IOavgTurnaround =  (int) ((float)IOavgTurnaround/(float)ioCount);
		IOavgWaiting = (int) ((float)IOavgWaiting/(float)ioCount);;
		IOavgService /= cpuCount;
		IOcpuTime /= ioCount;
		
		cpuUtilization = (long) ((float)((double)cpuUtilization/(double)simulationTime)*100);//Math.round( ((double)cpuUtilization / (double)(simulationTime)) * 100 );
		
		System.out.println("Total Simulation Time: " + (float)((float)clock/1000000.0) + "s");
		System.out.println("Total Context Switch Time: " + (float)((float)totalContextSwitch/1000000.0) + "s");
		System.out.println("CPU Utilization: " + cpuUtilization + "%");
		System.out.println(doneCount + " Out Of " + lastID + " Processes Completed");
		
		System.out.println("\nTotal # of Processes Completed = " + (ioCount + cpuCount));
		System.out.println("Ratio of I/O bound completed: " + (((float)ioCount/(float)(cpuCount+ioCount))*100) + "%");
		System.out.println("Avg. CPU Time: " + (((float)((float)CPUcpuTime/1000000.0) + (float)((float)IOcpuTime/1000000.0))/2)  + "s");
		System.out.println("Avg. Turnaround Time: " + (((float)((float)CPUavgTurnaround/1000000.0) + (float)((float)IOavgTurnaround/1000000.0))/2)  + "s");
		System.out.println("Avg. Waiting Time: " + (((float)((float)CPUavgWaiting/1000000.0) + (float)((float)IOavgWaiting/1000000.0))/2)  + "s");
		
		System.out.println("\nI/O Bound Processes Completed = " + ioCount);
		System.out.println("Avg. CPU Time: " + (float)((float)IOcpuTime/1000000.0) + "s");
		System.out.println("Avg. Turnaround Time: " + (float)((float)IOavgTurnaround/1000000.0) + "s");
		System.out.println("Avg. Waiting Time: " + (float)((float)IOavgWaiting/1000000.0) + "s");
		System.out.println("Avg. I/O Service Time: " + (double)((double)IOavgService/1000000.0) + "s");
		System.out.println("Avg. I/O Interrupts per Process: " + (IOinterrupt/ioCount));
		
		System.out.println("\nCPU Bound Processes Completed = " + cpuCount);
		System.out.println("Avg. CPU Time: " + (float)((float)CPUcpuTime/1000000.0) + "s");
		System.out.println("Avg. Turnaround Time: " + (float)((float)CPUavgTurnaround/1000000.0) + "s");
		System.out.println("Avg. Waiting Time: " + (float)((float)CPUavgWaiting/1000000.0) + "s");
		System.out.println("Avg. I/O Service Time: " + (double)((double)CPUavgService/1000000.0) + "s");
		System.out.println("Avg. I/O Interrupts per Process: " + (CPUinterrupt/cpuCount));
		
		
	}	
	
}