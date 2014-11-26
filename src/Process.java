import java.util.Random;


public class Process {
	
	public int id;
	public int startTime;
	public int totalLength = 500;
	public int remainingTime = totalLength;
	
	public Process(int tm, int identification) {
		startTime = tm;
		id = identification;
		
		Random RandomGenerator = new Random();
		//totalLength = RandomGenerator.nextInt(1000);
		//remainingTime = totalLength;
	}
	
	public void decrementTime() {
		remainingTime--;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
