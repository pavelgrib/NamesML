package ml;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
	private static final String OUTPUTDIR = "/Users/" + System.getProperty("user.name") + "/Documents/Imperial/ImperialChallenge/output/";
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
			files.put(f.getName().split("\\.")[0], f);
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
		LinkedList<NameItem> misspelled = new LinkedList<NameItem>();					// list of misspelled words
		TreeMap<String,String> corrections = new TreeMap<String,String>();			// map from misspelled word to suggested spelling
		Iterator<NameItem> name_it = inputFP.nameItemIterator();
		NameItem next;
		LetterNeighbor data;
		char[] charArray;
		double[] p1StrGiven1Str, p1StrGiven2Str, p2StrGiven1Str, p2StrGiven2Str, pNot1StrGiven1Str, pNot1StrGiven2Str, pNot2StrGiven1Str, pNot2StrGiven2Str, p1Prior1, pNot1Prior1;
		double lhoodCorrectFirst = 0, lhoodIncorrectFirst = 0, lhoodCorrectLast = 0, lhoodIncorrectLast = 0;
		while ( name_it.hasNext() ) {
			next = name_it.next();
			if ( next.get_name().length() == 1 || next.initially_misspelled() ) {
				misspelled.add(next);
			} else {
				// classifying first name
				if ( next.get_gender() == "M" ) {
					data = _ln_male;
				} else {
					data = _ln_female;
				}
				charArray = next.get_name().toLowerCase().toCharArray();
				p1StrGiven1Str = new double[charArray.length-1];
				pNot1StrGiven1Str = new double[charArray.length-1];
				p1Prior1 = new double[charArray.length - 1];
				pNot1Prior1 = new double[charArray.length - 1];

				for ( int i = 0 ; i < charArray.length-1; i++ ) {
					p1StrGiven1Str[i] = data.conditionalProbability( String.valueOf(charArray[i+1]), String.valueOf(charArray[i]) );
					pNot1StrGiven1Str[i] = 1 - p1StrGiven1Str[i];
				}
				lhoodCorrectFirst = (1 - _prior) * HelperFunctions.product(p1StrGiven1Str) * HelperFunctions.product(p1Prior1);
				lhoodIncorrectFirst = _prior * HelperFunctions.product(pNot1StrGiven1Str) * HelperFunctions.product(pNot1Prior1);
				
				if ( charArray.length > 2 ) {
					p1StrGiven2Str = new double[charArray.length-2];
					p2StrGiven1Str = new double[charArray.length-2];
					pNot1StrGiven2Str = new double[charArray.length-2];
					pNot2StrGiven1Str = new double[charArray.length-2];
					char[] twochar = new char[2];
					char[] twochar2 = new char[2];
					for ( int i = 0; i < charArray.length - 2; i++ ) {
						twochar[0] = charArray[i];
						twochar[1] = charArray[i+1];
						p1StrGiven2Str[i] = data.conditionalProbability( String.valueOf(charArray[i+2]), String.valueOf(twochar) );
						pNot1StrGiven2Str[i] = 1 - p1StrGiven2Str[i];
						
						twochar[0] = charArray[i+1];
						twochar[1] = charArray[i+2];
						p2StrGiven1Str[i] = data.conditionalProbability( String.valueOf(charArray[i]), String.valueOf(twochar) );
						pNot2StrGiven1Str[i] = 1 - p2StrGiven1Str[i];
					}
					lhoodCorrectFirst *= HelperFunctions.product(p1StrGiven2Str) * HelperFunctions.product(p1StrGiven2Str);
					lhoodIncorrectFirst *= HelperFunctions.product(pNot1StrGiven2Str) * HelperFunctions.product(pNot1StrGiven2Str);
					if ( charArray.length > 3 ) {
						p2StrGiven2Str = new double[charArray.length-3];
						pNot2StrGiven2Str = new double[charArray.length-3];
						for ( int i = 0; i < charArray.length-3; i++ ) {
							twochar[0] = charArray[i+2];
							twochar[1] = charArray[i+3];
							twochar2[0] = charArray[i];
							twochar2[1] = charArray[i+1];
							p2StrGiven2Str[i] = data.conditionalProbability( String.valueOf(twochar2), String.valueOf(twochar) );
							pNot2StrGiven2Str[i] = 1 - p2StrGiven2Str[i];
						}
						lhoodCorrectFirst *= HelperFunctions.product(p2StrGiven2Str);
						lhoodIncorrectFirst *= HelperFunctions.product(pNot2StrGiven2Str);
					}
					if ( lhoodCorrectFirst < lhoodIncorrectFirst ) {
						misspelled.add(next);
						corrections.put(next.toString(), HelperFunctions.surroundWithQuotes(replacement(p1StrGiven1Str, charArray)) ); 
					}
				}
				
				// classifying last name
				charArray = next.get_lastname().toLowerCase().toCharArray();
				data = _ln_surnames;
				p1StrGiven1Str = new double[charArray.length-1];
				pNot1StrGiven1Str = new double[charArray.length-1];
				p1Prior1 = new double[charArray.length - 1];
				pNot1Prior1 = new double[charArray.length - 1];
				
				for ( int i = 0 ; i < charArray.length-1; i++ ) {
					p1StrGiven1Str[i] = data.conditionalProbability( String.valueOf(charArray[i+1]), String.valueOf(charArray[i]) );
					pNot1StrGiven1Str[i] = 1 - p1StrGiven1Str[i];
				}
				lhoodCorrectLast = (1 - _prior) * HelperFunctions.product(p1StrGiven1Str) * HelperFunctions.product(p1Prior1);
				lhoodIncorrectLast = _prior * HelperFunctions.product(pNot1StrGiven1Str) * HelperFunctions.product(pNot1Prior1);
				
				if ( charArray.length > 2 ) {
					p1StrGiven2Str = new double[charArray.length-2];
					p2StrGiven1Str = new double[charArray.length-2];
					pNot1StrGiven2Str = new double[charArray.length-2];
					pNot2StrGiven1Str = new double[charArray.length-2];
					char[] twochar = new char[2];
					char[] twochar2 = new char[2];
					for ( int i = 0; i < charArray.length - 2; i++ ) {
						twochar[0] = charArray[i];
						twochar[1] = charArray[i+1];
						p1StrGiven2Str[i] = data.conditionalProbability( String.valueOf(charArray[i+2]), String.valueOf(twochar) );
						pNot1StrGiven2Str[i] = 1 - p1StrGiven2Str[i];
						
						twochar[0] = charArray[i+1];
						twochar[1] = charArray[i+2];
						p2StrGiven1Str[i] = data.conditionalProbability( String.valueOf(charArray[i]), String.valueOf(twochar) );
						pNot2StrGiven1Str[i] = 1 - p2StrGiven1Str[i];
					}
					lhoodCorrectLast *= HelperFunctions.product(p1StrGiven2Str) * HelperFunctions.product(p1StrGiven2Str);
					lhoodIncorrectLast *= HelperFunctions.product(pNot1StrGiven2Str) * HelperFunctions.product(pNot1StrGiven2Str);
					if ( charArray.length > 3 ) {
						p2StrGiven2Str = new double[charArray.length-3];
						pNot2StrGiven2Str = new double[charArray.length-3];
						for ( int i = 0; i < charArray.length-3; i++ ) {
							twochar[0] = charArray[i+2];
							twochar[1] = charArray[i+3];
							twochar2[0] = charArray[i];
							twochar2[1] = charArray[i+1];
							p2StrGiven2Str[i] = data.conditionalProbability( String.valueOf(twochar2), String.valueOf(twochar) );
							pNot2StrGiven2Str[i] = 1 - p2StrGiven2Str[i];
						}
						lhoodCorrectLast *= HelperFunctions.product(p2StrGiven2Str);
						lhoodIncorrectLast *= HelperFunctions.product(pNot2StrGiven2Str);
					}
				}
				if ( lhoodCorrectLast < lhoodIncorrectLast ) {
					misspelled.add(next);
					corrections.put(next.toString(), HelperFunctions.surroundWithQuotes(replacement(p1StrGiven1Str, charArray)) ); 
				}
			}
		}
		HelperFunctions.writeListToFile(misspelled, OUTPUTDIR + "CandidateOutput1_" + HelperFunctions.fileNumber(_inputFile) + ".txt");
		HelperFunctions.writeMapToFile(corrections, OUTPUTDIR + "CandidateOutput2_" + HelperFunctions.fileNumber(_inputFile) + ".txt");
	}
	
	private String replacement(double[] pArray, char[] charArray) {
		int minIdx1 = HelperFunctions.minAt(pArray);
		char[] replacement = charArray;
		List<String> exclude = new LinkedList<String>();
		char c = _ln_surnames.likeliestFollowing(String.valueOf(charArray[minIdx1]), 1, exclude).charAt(0);
		replacement[minIdx1] = c;
		return String.valueOf(replacement);
	}
}