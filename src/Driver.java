import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Driver {

	// INPUT VALUES
	static int simulationTime = 10000;
	static int quantum=100; 
	static int contextSwitchTime=0;
	static int avgProcessLength=0;
	static int avgTimeBetweenProcesses=0;
	static int avgIOInterruptLength=0;
			
	// Decimal representing percentage
	static int ioBoundPercentage=0;
	
	static ArrayList<Event> eventQ = new ArrayList<Event>();
	static ArrayList<Process> readyQ = new ArrayList<Process>();
	static ArrayList<Process> CPU = new ArrayList<Process>();
	static ArrayList<Process> ioQ = new ArrayList<Process>();
	static ArrayList<Process> io = new ArrayList<Process>();
	
	static int clock = 0;
	
	static int lastClock = 0;
	static int avgTurnaround = 0,avgWaiting = 0,doneCount = 0;
	static float cpuUtilization = 0;
	
	static int lastID = 0;
	
	public static void main(String[] args) {
		//Read Input Parameters
		readInput();
		
		System.out.println("SIMULATION\n");
		// Create a Process
		Event createevent = new Event(0,clock);
		
		// Move that process to CPU
		Event movetoCPUevent = new Event(1,clock);

		// Run CPu Process
		Event startevent = new Event(4,clock);
		eventQ.add(createevent);
		eventQ.add(createevent);
		eventQ.add(movetoCPUevent);
		eventQ.add(startevent);
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
				simulationTime =  input;
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
				avgIOInterruptLength = input;
			}
			
			count++;
		}
		System.out.println("\n");
	}
	
	public static void doEvent() {
		Collections.sort(eventQ);
		
		if (eventQ.size() == 0 || clock >= simulationTime) {
			//Simulation is done
			clock = simulationTime;
			report();
			
			// Empty every queue
			eventQ.clear();
			readyQ.clear();
			CPU.clear();
			
			return;
		}
		
//		if (eventQ.size() == 0) {
//			return;
//		}
		
		Event event = eventQ.remove(0);
		
		//System.out.println(eventQ.size());
		//System.out.println("Event type: " + event.type + ", Event stamp: " + event.timestamp);
		
		switch (event.type) {
		
		// Create a new Process
		case 0: 
			createProcess();
		break;
		// Move ready process to CPU
		case 1:
			//System.out.println("Moving to CPU");
			//System.out.print("\ncase: 1");
			if (readyQ.size() == 0) {
				break;
			}
			CPU.add(readyQ.remove(0));
			System.out.println("Moved process #" + CPU.get(0).id + " to CPU" + "\t\t\t\t at time " + clock);
		break;
		
		// Quantum Switch
		case 2:
			//System.out.println("\ncase: 2 (Quantum Switch)");
			
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
				//System.out.println("event stamp is: " + event.timestamp);
				Event moveback = new Event(3,clock);
				Event movetoCPU = new Event(1,clock);
				
				clock += contextSwitchTime; // Context Switch Time
				
				Event run = new Event(4,clock);
				eventQ.add(moveback);
				eventQ.add(movetoCPU);
				eventQ.add(run);
				
		break;
		
		// Move from CPU to readyQ
		case 3: 
			//System.out.println("Emptying CPU");
			if (CPU.size() == 0) {
				//System.out.println("Empty CPU");
				break;
			}
			System.out.println("Moving process #" + CPU.get(0).id + " from CPU back to Ready Queue" + "\t\t\t\t at time " + clock);
			readyQ.add(CPU.remove(0));
			
		break;
		
		// Actually run process
		case 4:
			//System.out.print("\ncase: 4");
			
			System.out.println("Running process #" + CPU.get(0).id + "\t\t\t\t at time " + clock);
			runProcess();
			
		break;
		case 5:
			//System.out.println("\ncase: 5");
			//Process Done
			System.out.println("Process #" + CPU.get(0).id +  " done" + "\t\t\t\t at time " + clock);
			System.out.println("Process #" + CPU.get(0).id +  " Removed from CPU" + "\t\t\t\t at time " + clock);
			CPU.remove(0);
			
			doneCount ++;
			
			
		break;
		}
		doEvent();
	}
	
	public static void runProcess() {
		// Decrease remaining time of process in the CPU
		Process tempP = CPU.get(0);
		int preclock = clock;
		// Check if simulation is done before process is done running
		
		
		if (tempP.remainingTime <= quantum) {
			// Advance Clock by remaining time
			clock += tempP.remainingTime;
			
			tempP.remainingTime = 0;
			
			// Record Statistics
			tempP.done(clock);
			
			avgTurnaround += tempP.turnaroundTime;
			avgWaiting += tempP.waitingTime;
			
			// Create a Process Done Event, and add it to eventQ
			Event doneEvent = new Event(5,clock);
			eventQ.add(doneEvent);
		} else {
			// Advance Clock by quantum
			clock += quantum;
			tempP.remainingTime -= quantum;
		}
		
		cpuUtilization += (clock-preclock);
		
		//Create a Quantum Switch Event
		Event switchEvent = new Event(2,clock);
		eventQ.add(switchEvent);
		//doEvent();
	}
	
	public static void createProcess() {
		Process p = new Process(clock,lastID,ioBoundPercentage);
		readyQ.add(p);
		lastID ++;
		
		System.out.println("Created Process #" + p.id + " (Total Length: " + p.totalLength + ")" + "\t\t\t\t at time " + clock);
		
		// Create a new Create Process Event at a future time
		
		if (lastID < 1000) {
			int futuretime = 100; // Random Number
			Event newProcess = new Event(0,clock + futuretime);
			eventQ.add(newProcess);
		}
	}
	
	public static void report() {
		// Print out all the statistics
		System.out.println("\nSIMULATION REPORT \n");
		
		avgTurnaround /= lastID;
		avgWaiting /= lastID;
		cpuUtilization = Math.round( (cpuUtilization / (float)(simulationTime)) * 100 );
		
		System.out.println("Avg. Turnaround Time: " + avgTurnaround);
		System.out.println("Avg. Waiting Time: " + avgWaiting);
		System.out.println("CPU Utilization: " + cpuUtilization + "%");
		System.out.println(doneCount + " Out Of " + lastID + " Processes Completed");
	}	
	
}