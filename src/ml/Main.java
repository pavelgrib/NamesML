package ml;

import java.util.Random;
import def.FileProcessor;

/**
 * @author paul.
 */

public class Main {

	private static final String workingDir = "/Users/paul/Documents/Imperial/NamesML/";
	
	public static void main(String[] args) {
//		FileProcessor fp = new FileProcessor("/Users/paul/Documents/Imperial/NamesML/TestNames.txt");
//		fp.printAllNames();
//		fp.printeFile();
//		Random generator = new Random();
//		double mean = 5, variance = 1;
//		Histogram h = new Histogram(100, 0, 10);
//		for ( int i = 0; i < 1000000; i++ ) {
//			h.add(mean + variance * generator.nextGaussian());
//		}
//		h.printHist(true);
		try {
			LetterNeighbor lh = new LetterNeighbor(workingDir + "names/male_names.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
