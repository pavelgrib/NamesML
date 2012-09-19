package ml;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import def.*;

/**
 * @author paul.
 */

public class Main {
	
	// Mac OS X setups only
	private static final String WORKINGDIR = "/Users/" + System.getProperty("user.name") + "/github/local/NamesML/";
	private static final String OUTPUTDIR = WORKINGDIR + "validation/";
	public static void main(String[] args) {
//		FileProcessor fp = new FileProcessor("/Users/paul/Documents/Imperial/NamesML/TestNames.txt");
//		fp.printAllNames();
//		fp.printeFile();
		Random generator = new Random();
		double mean = 0, variance = 10;
		// test to make sure Histogram class works
		Histogram h = new Histogram(100, 0.0, 10.0);
		for ( int i = 0; i < 1000000; i++ ) {
			h.add(mean + variance * generator.nextDouble());
		}
		h.printHist(true, true);
//		try {
//			LetterNeighbor lh = new LetterNeighbor(WORKINGDIR + "names/male_names.txt");
//			MisspellingGenerator spell = new MisspellingGenerator(WORKINGDIR + "names/male_names.txt", 1);
//			spell.generateMisspelledFile(OUTPUTDIR + "male_names_misspelled.txt", "M");
//			HelperFunctions.printFileLinesToConsole(OUTPUTDIR + "male_names_misspelled.txt");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
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

		Histogram testhist = new Histogram(100, 0, 10);
//		for ( int i = 0; i < 5000; i++ ) {
//			testhist.add(0);
//		}
//		for ( int i = 0; i < 10000; i++ ) {
//			testhist.add(mean + Math.sqrt(variance)*generator.nextGaussian());
//		}
//		
//		System.out.println(testhist.maxBinCount());
	}
}
