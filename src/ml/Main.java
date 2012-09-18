package ml;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Random;
import def.*;

/**
 * @author paul.
 */

public class Main {
	
	// Mac OS X setups only
	private static final String WORKINGDIR = "/Users/" + System.getProperty("user.name") + "/github/local/NamesML/";
	
	public static void main(String[] args) {
//		FileProcessor fp = new FileProcessor("/Users/paul/Documents/Imperial/NamesML/TestNames.txt");
//		fp.printAllNames();
//		fp.printeFile();
//		Random generator = new Random();
//		double mean = 5, variance = 1;
//		Histogram h = new Histogram(100, 0.0, 10.0);
//		for ( int i = 0; i < 100000; i++ ) {
//			h.add(mean + variance * generator.nextGaussian());
//		}
//		h.printHist(true, true);
		try {
			LetterNeighbor lh = new LetterNeighbor(WORKINGDIR + "names/male_names.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// test to make sure TreeMap.keySet() returns them in order
//		TreeMap<Character, Double> tm = new TreeMap<Character, Double>();
//		for ( int i = 0; i < 26; i++ ) {
//			tm.put((char) ('a' + i), generator.nextGaussian());
//		}
//		
//		Set<Character> cs = tm.keySet();
//		Iterator<Character> cs_it = cs.iterator();
//		while ( cs_it.hasNext() ) {
//			System.out.println(cs_it.next());
//		}
//		HelperFunctions.printDirectoryContents(WORKINGDIR);
//		MisspellingGenerator spell = new MisspellingGenerator(WORKINGDIR + "names/male_names.txt");
	}
}
