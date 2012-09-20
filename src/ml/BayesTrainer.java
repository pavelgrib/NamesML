package ml;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import def.FileProcessor;
import def.HelperFunctions;
import def.NameItem;

/**
 * TODO Class to run the training and store results; once that's done, class can run through file and output CandidateOutput1.txt
 * @author paul.
 */

public class BayesTrainer {
	
	private static final String TRAININGPATH = "/Users/" + System.getProperty("user.name") + "/github/local/NamesML/names/";
	private static final String OUTPUTPATH = "/Users/" + System.getProperty("user.name") + "/github/local/NamesML/results/";
	private String _inputFile;
	private double _prior;
	private LetterNeighbor _ln_male;
	private LetterNeighbor _ln_female;
	private LetterNeighbor _ln_surnames;
	
	public BayesTrainer() {
		_prior = 0.5;
		File namesFolder = new File(TRAININGPATH);
		HashMap<String, File> files = new HashMap<String, File>();
		for ( File f: namesFolder.listFiles() ) {
			files.put(f.getName().split(".")[0], f);
		}
		
		try {
			this.train();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void train() throws Exception {
		_ln_male = new LetterNeighbor(TRAININGPATH + "male.txt");
		_ln_female = new LetterNeighbor(TRAININGPATH + "female.txt");
		_ln_surnames = new LetterNeighbor(TRAININGPATH + "surnames.txt");
	}

	// this value extracted from a small sample of input file to get approximate rate of misspelling via visual observation (pyNamesHelper.randomEntries)
	public void set_prior(double p) {
		_prior = p;
	}
	
	public void setInputFile(String path) {
		_inputFile = path;
	}
	
	// classifies everything in the input file according to the LetterNeighbor instance passed in
	public void bayesClassify() {
		FileProcessor inputFP = new FileProcessor(_inputFile, 4);
		LinkedList<String> misspelled = new LinkedList<String>();					// list of misspelled words
		TreeMap<String,String> corrections = new TreeMap<String,String>();			// map from misspelled word to suggested spellin
		Iterator<NameItem> name_it = inputFP.nameItemIterator();
		NameItem next;
		String[] nextName;
		LetterNeighbor data;
		char[] charArray, charArraySearch;
		char[] twochar = new char[2];
		double[] oneLetterP, twoLetterP, notOneLetterP, notTwoLetterP, prior;
//		SimpleStats tempSS1 = new SimpleStats(), tempSS2 = new SimpleStats(), tempSS3 = new SimpleStats();
		double lhoodCorrect = 0, lhoodIncorrect = 0;
		while ( name_it.hasNext() ) {
			next = name_it.next();
			// TODO: need some processing to deal with first and last names leading/trailing symbols, numbers, whitespace
			
			nextName = next.get_name().split(" ");
			
			charArray = next.get_name().toLowerCase().toCharArray();
			oneLetterP = new double[charArray.length-1];
			twoLetterP = new double[charArray.length-2];
			notOneLetterP = new double[charArray.length-1];
			notTwoLetterP = new double[charArray.length-2];
			if ( next.get_gender() == "M" ) {
				data = _ln_male;
			} else {
				data = _ln_female;
			}
			for ( int i = 0 ; i < charArray.length-1; i++ ) {
				oneLetterP[i] = data.conditionalProbability( String.valueOf(charArray[i+1]), String.valueOf(charArray[i]) );
				notOneLetterP[i] = 1 - oneLetterP[i];
			}
			
			for ( int i = 0; i < charArray.length - 2; i++ ) {
				twochar[0] = charArray[i];
				twochar[1] = charArray[i+1];
				twoLetterP[i] = data.conditionalProbability( String.valueOf(charArray[i+2]), String.valueOf(twochar) );
				notTwoLetterP[i] = 1 - twoLetterP[i];
			}
			lhoodCorrect = HelperFunctions.product(oneLetterP) * HelperFunctions.product(twoLetterP);
			lhoodIncorrect = HelperFunctions.product(notOneLetterP) * HelperFunctions.product(notTwoLetterP);
			if ( lhoodCorrect > lhoodIncorrect ) {
				misspelled.add(next.get_name());
				/* TODO search for corrections: will be any modifications of the word that can increase product above the other one
				 potential solution: find the smallest probability causing the classification, then find the largest one such that there is proximity between
				 the current misspelled word and the correct one... need to define notion of proximity between strings
				*/
//				tempSS1.add(oneLetterP);
//				charArray[tempSS1.get_minAtCount()-1];
			}
		}


		HelperFunctions.writeListToFile(misspelled, OUTPUTPATH + "CandidateOutput1.txt");
	}
}
