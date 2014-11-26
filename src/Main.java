import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

<<<<<<< HEAD
	// Values in Microseconds
			int simulationTime=0; // 
			int quantum=0; //
			int contextSwitchTime=0;
			int avgProcessLength=0;
			int avgTimeBetweenProcesses=0;
			int avgIOInterruptLength=0;
			
			// Decimal representing percentage
			float ioBoundPercentage=0;
=======
	// Process Ready Queue
	ArrayList<Process> readyQ = new ArrayList<Process>();
	// Event Queue
	// CPU 'Queue' (holds only one process)
			
	//Values in seconds
	int simulationTime=0;
			
	// Values in Microseconds
	int quantum=0; //
	int contextSwitchTime=0;
	int avgProcessLength=0;
	int avgTimeBetweenProcesses=0;
	int avgIOInterruptLength=0;
			
	// Decimal representing percentage
	float ioBoundPercentage=0;
>>>>>>> e811bd02e9d35fb3c181f2508464cfca2b8bdf80
	
	public static void main(String[] args) {
		
		
		
		// Parse Input File
		File file = new File("input.txt");
		Scanner scan = null;
		
		try {
			scan = new Scanner(file);
		}	
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		int count = 0;
		while (scan.hasNextLine()) {
			String line = scan.nextLine();

			int input = 0;
			if (count != 0) {
				input = Integer.parseInt(line);
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
				System.out.println("Avg IO Interrupt: " + input);
				avgIOInterruptLength = input;
			} else if (count == 7) {
				System.out.println("io percentage: " + input);
				ioBoundPercentage = ((float)input)/100;
			}
			
			count++;
		}
		
		//
	}
	
<<<<<<< HEAD
	void createProcess(){
		Process process = new Process(5);
		
	}
	
=======
	// Faux Event. Process Instance calls this method when it's done
	public void processDone() {
		// Remove current process from Queue and Nullify it
		// Reset Quantum if necessary
		// Switch Process
	}
	
	public void flow() {
		
	}
>>>>>>> e811bd02e9d35fb3c181f2508464cfca2b8bdf80

}
