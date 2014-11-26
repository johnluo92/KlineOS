import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	static int time = 0;
	static int quantumLeft = 0;
	static Timer timer;
	// Values in Microseconds
	static int simulationTime=0; // 
	static int quantum=100; //
	static int contextSwitchTime=0;
	static int avgProcessLength=0;
	static int avgTimeBetweenProcesses=0;
	static int avgIOInterruptLength=0;
			
			// Decimal representing percentage
	static float ioBoundPercentage=0;
	// Process Ready Queue
	static ArrayList<Process> readyQ = new ArrayList<Process>();
	
	// Event Queue
	// CPU 'Queue' (holds only one process)
	static Process currentCPUProcess;
	static int currentProcessPosition = -1;

	
	public static void main(String[] args) {
		
		start();
		
//		// Parse Input File
//		File file = new File("input.txt");
//		Scanner scan = null;
//		
//		try {
//			scan = new Scanner(file);
//		}	
//		catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//		int count = 0;
//		while (scan.hasNextLine()) {
//			String line = scan.nextLine();
//
//			int input = 0;
//			if (count != 0) {
//				input = Integer.parseInt(line);
//			}
//			if (count == 1) {
//				System.out.println("Simulation Time: " + input);
//				simulationTime =  input;
//			} else if (count == 2) {
//				System.out.println("Quantum is: " + input);
//				quantum = input;
//			} else if (count == 3) {
//				System.out.println("Context switch: " + input);
//				contextSwitchTime = input;
//			} else if (count == 4) {
//				System.out.println("Avg p length: " + input);
//				avgProcessLength = input;
//			} else if (count == 5) {
//				System.out.println("Avg time between processes: " + input);
//				avgTimeBetweenProcesses = input;
//			} else if (count == 6) {
//				System.out.println("Avg IO Interrupt: " + input);
//				avgIOInterruptLength = input;
//			} else if (count == 7) {
//				System.out.println("io percentage: " + input);
//				ioBoundPercentage = ((float)input)/100;
//			}
//			
//			count++;
//		}
		
	}
	
	
	
	static void tick() {
		
		// Clock Method called every tick of the clock
		// Check if needs to create next process
		
		// Calculate Quantum time left
		quantumLeft = time % quantum;
		
		// Switch to CPU Process if necessary
		//System.out.println(currentCPUProcess.remainingTime);

		if (currentCPUProcess.remainingTime <= 0) {
			//System.out.println("Process " + currentProcessPosition + "is done.");
			readyQ.remove(currentProcessPosition);
			currentProcessPosition --;
			switchProcess();
			//timer.cancel();
		} else if (quantumLeft == 0) {
			switchProcess();
			//System.out.println("Switching Process");
		}
		
		// Update process time left
		currentCPUProcess.decrementTime();
		
		if (readyQ.size() == 0) {
			timer.cancel();
		}		
		
		time ++;
	}
	
	static void switchProcess() {
		//createProcess();
		
		if (readyQ.size() -1 >= currentProcessPosition) {
			currentProcessPosition ++;
		}
		
		if (currentProcessPosition > readyQ.size()-1) {
			currentProcessPosition = 0;
		}
		
		
		if (readyQ.size() != 0) {
			currentCPUProcess = readyQ.get(currentProcessPosition);
			System.out.println("Time Left for process " + currentCPUProcess.id + ": " + currentCPUProcess.remainingTime + "/" + currentCPUProcess.totalLength);
			System.out.println("Queue Size: " + readyQ.size());
		} else {
			System.out.println("No more");
		}
	}
	
	static void start(){
		createProcess();
		createProcess();
		createProcess();
		
		currentCPUProcess = readyQ.get(0);
		
		//Create a Timer
		RemindTask task = new RemindTask();
		
		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1);
		
		// Start of Program
		// Create a Process
		// Decide next Process Creation Time
	}
	
	static class RemindTask extends TimerTask {
        public void run() {
            tick();
        }
    }
	
	static void createProcess(){
		Process process = new Process(time,readyQ.size());
		readyQ.add(readyQ.size(), process);
	}

	
	
	
	// Faux Event. Process Instance calls this method when it's done
	public void processDone() {
		// Remove current process from Queue and Nullify it
		// Reset Quantum if necessary
		// Switch Process
	}
	
	public void flow() {
		
	}
}
