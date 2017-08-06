package vladi_yaki_project;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Test {
	private static int[] resultsArray = new int[5];
	private static int winner;
	
	public static void proccessReults(double[] results) {
		double max = -1;
		int index = -1;
		for (int j=1; j <= results.length; j++) {
			for (int i = 0; i < results.length; i++) {
				if (results[i] > max) {
					max = results[i];
					index = i;
				}
			}
			resultsArray[index] = j;
			results[index]=-1;
			max=-1;
			if (j==1)
				winner=index;
		}
	}
	public static void main(String[] args) {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
		//System.out.println(randomNum);
		String[] sizes = {"small","meduim","big"};
		String str = sizes[randomNum];
		System.out.println(str);
	}

}
