// Scheduling Simulator
// Barak Michaely , John Luo, Alvin Lawson

import java.util.Random;


public class Generator {

	public static int randomRange(int input) {
		Random RandomGenerator = new Random();
		int rn = RandomGenerator.nextInt(input);
		rn -= (int) (input*0.6);
		
		return input + rn;
	}
	
}
