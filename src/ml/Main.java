package ml;

//import java.io.File;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.Random;
//import java.util.Set;
//import java.util.TreeMap;
import def.*;

/**
 * @author paul.
 */

public class Main {
	
	// Mac OS X setups only
	private static final String WORKINGDIR = "/Users/" + System.getProperty("user.name") + "/github/local/NamesML/";
	private static final String OUTPUTDIR = WORKINGDIR + "validation/";
	public static void main(String[] args) {
		FileProcessor fp = new FileProcessor("/Users/paul/Documents/Imperial/ImperialChallenge1.txt", 4);
		fp.printAllNames();
//		fp.printeFile();
//		Random generator = new Random();
//		double mean = 0.5, variance = 0.01;
//		// test to make sure Histogram class works
//		Histogram h = new Histogram(100, 0.0, 1.0);
//		for ( int i = 0; i < 10000; i++ ) {
//			h.add(mean + Math.sqrt(variance) * generator.nextGaussian());
//		}
//		h.printHist(true, true);
//		try {
//			LetterNeighbor lh = new LetterNeighbor(WORKINGDIR + "names/male_names.txt");
//			MisspellingGenerator spell = new MisspellingGenerator(WORKINGDIR + "names/male_names.txt", 1);
//			spell.generateMisspelledFile(OUTPUTDIR + "male_names_misspelled.txt", "M");
//			HelperFunctions.printFileLinesToConsole(OUTPUTDIR + "male_names_misspelled.txt");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		double prior = 0.3;
//		String[] files = ( new File(OUTPUTDIR) ).list();
//		FileProcessor fp;
//		String gender;
//		LetterNeighbor[] lh = new LetterNeighbor[files.length];
//		for ( int i = 0; i < files.length; i++ ) {
//			if ( files[i].contains("male") ) {
//				gender = "MALE";
//			} else if ( files[i].contains("female") ) {
//				gender = "FEMALE";
//			} else {
//				gender = "NONE";
//			}
//			try {
//				lh[i] = new LetterNeighbor(new FileProcessor(OUTPUTDIR + files[i], 1), gender);
//				lh[i] = 
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		LetterNeighbor l = new LetterNeighbor();
//		l.printStringValues();
	}
}
