package ml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public BayesTrainer() {
		File namesFolder = new File(TRAININGPATH);
		HashMap<String, File> files = new HashMap<String, File>();
		for ( File f: namesFolder.listFiles() ) {
			files.put(f.getName().split(".")[0], f);
		}
	}
	
	public void setInputFile(String path) {
		_inputFile = path;
	}
	
	// classifies everything in the input file according to the LetterNeighbor instance passed in
	public void bayesClassify(LetterNeighbor[] ln) {
		FileProcessor inputFP = new FileProcessor(_inputFile, 4);
		LinkedList<String> misspelled = new LinkedList<String>();					// list of misspelled words
		TreeMap<String,String> corrections = new TreeMap<String,String>();			// map from misspelled word to suggested spellin
		Iterator<NameItem> name_it = inputFP.nameItemIterator();
		NameItem next;
		LetterNeighbor data;
		char[] charArray;
		char[] twochar = new char[2];
		double[] oneLetterP, twoLetterP, notOneLetterP, notTwoLetterP, prior;
		boolean isMisspelled;			// this is between 0 and 1, with some reasonable cutoff forcing the classification
		while ( name_it.hasNext() ) {
			next = name_it.next();
			// TODO: need some processing to deal with first and last names leading/trailing symbols, numbers, whitespace
			charArray = next.get_name().toLowerCase().toCharArray();
			oneLetterP = new double[charArray.length-1];
			twoLetterP = new double[charArray.length-2];
			notOneLetterP = new double[charArray.length-1];
			notTwoLetterP = new double[charArray.length-2];
			prior = new double[charArray.length];
			if ( next.get_gender() == "M" ) {
				data = ln[0];
			} else {
				data = ln[1];
			}
			for ( int i = 0 ; i < charArray.length-1; i++ ) {
				prior[i] = data.priorProbability(String.valueOf(charArray[i]));
				oneLetterP[i] = data.conditionalProbability( String.valueOf(charArray[i+1]), String.valueOf(charArray[i]) );
				notOneLetterP[i] = 1 - oneLetterP[i];
			}
			
			for ( int i = 0; i < charArray.length - 2; i++ ) {
				twochar[0] = charArray[i];
				twochar[1] = charArray[i+1];
				prior[i] = data.priorProbability(String.valueOf(twochar));
				twoLetterP[i] = data.conditionalProbability( String.valueOf(charArray[i+2]), String.valueOf(twochar) );
				notTwoLetterP[i] = 1 - twoLetterP[i];
			}
			isMisspelled = HelperFunctions.product(oneLetterP) > HelperFunctions.product(notOneLetterP) ? false : true;
			if ( isMisspelled ) {
				misspelled.add(next.get_name());
				/* TODO search for corrections: will be any modifications of the word that can increase product above the other one
				 potential solution: find the smallest probability causing the classification, then find the largest one such that there is proximity between
				 the current misspelled word and the correct one... need to define notion of proximity between strings
				*/
			}
		}


		HelperFunctions.writeListToFile(misspelled, OUTPUTPATH + "CandidateOutput1.txt");
	}
}
