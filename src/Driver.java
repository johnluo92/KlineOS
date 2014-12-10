import java.util.ArrayList;

public class Driver {

	static ArrayList<Event> eventQ = new ArrayList<Event>();
	static ArrayList<Process> readyQ = new ArrayList<Process>();
	static ArrayList<Process> CPU = new ArrayList<Process>();
	
	static int clock = 0;
	static int simulationTime = 10000;
	static int lastID = 0;
	static int quantum = 10;
	
	public static void main(String[] args) {
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
	
	public static void doEvent() {
		if (clock >= simulationTime) {
			//Simulation is done
			report();
			
			// Empty every queue
			eventQ.clear();
			readyQ.clear();
			CPU.clear();
			
			return;
		}
		
		if (eventQ.size() == 0) {
			return;
		}
		
		Event event = eventQ.remove(0);
		
		//System.out.println(eventQ.size());
		System.out.println("Event type: " + event.type + ", Event stamp: " + event.timestamp);
		
		switch (event.type) {
		
		// Create a new Process
		case 0: 
			createProcess();
		break;
		// Move ready process to CPU
		case 1:
			System.out.print("\ncase: 1");
			if (readyQ.size() == 0) {
				break;
			}
			CPU.add(readyQ.remove(0));
			System.out.print(" (Moved process #" + CPU.get(0).id + " to CPU)\n");
		break;
		
		// Quantum Switch
		case 2:
			System.out.println("\ncase: 2 (Quantum Switch)");
				// move CPU process back to queue
				// move ready process to cpu
				
				if (CPU.size() == 0 && readyQ.size() == 0) {
					
					break;
				}
			
				System.out.println("event stamp is: " + event.timestamp);
				Event moveback = new Event(3,clock);
				Event movetoCPU = new Event(1,clock);
				Event run = new Event(4,clock);
				eventQ.add(moveback);
				eventQ.add(movetoCPU);
				eventQ.add(run);
				
		break;
		
		// Move from CPU to readyQ
		case 3: 
			System.out.print("\ncase: 3 (Moving process from CPU to readyQ)\n");
			if (CPU.size() == 0) {
				break;
			}
			readyQ.add(CPU.remove(0));
		break;
		
		// Actually run process
		case 4:
			System.out.print("\ncase: 4");
			runProcess();
			System.out.println(" (Running process #" + CPU.get(0).id);
		break;
		case 5:
			System.out.println("\ncase: 5");
			//Process Done
			System.out.println("Process " + CPU.get(0).id +  " done");
			CPU.remove(0);
			
			// Switch Process
		break;
		}
		doEvent();
	}
	
	public static void runProcess() {
		// Decrease remaining time of process in the CPU
		Process tempP = CPU.get(0);
		
		// Check if simulation is done before process is done running
		
		
		if (tempP.remainingTime <= quantum) {
			// Advance Clock by remaining time
			clock += tempP.remainingTime;
			
			tempP.remainingTime = 0;
			
			// Create a Process Done Event, and add it to eventQ
			Event doneEvent = new Event(5,clock);
			eventQ.add(doneEvent);
		} else {
			// Advance Clock by quantum
			clock += quantum;
			tempP.remainingTime -= quantum;
		}
		
		//Create a Quantum Switch Event
		Event switchEvent = new Event(2,clock);
		eventQ.add(switchEvent);
		//doEvent();
	}
	
	public static void createProcess() {
		Process p = new Process(clock,lastID);
		readyQ.add(p);
		lastID ++;
		
		System.out.println("Created Process #" + p.id);
		
		// Create a new Create Process Event at a future time
		int futuretime = 1000; // Random Number
		Event newProcess = new Event(0,clock + futuretime);
		
		//eventQ.add(newProcess);
	}
	
	public static void report() {
		// Print out all the statistics
		System.out.println("It's all done");
	}
	
}