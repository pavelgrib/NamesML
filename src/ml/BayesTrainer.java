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
		FileProcessor inputFP = new FileProcessor(_inputFile, 4);
	}
	
	// classifies everything in the input file according to the LetterNeighbor instance passed in
	public void bayesClassify(FileProcessor fp, LetterNeighbor[] ln) {
		LinkedList<String> misspelled = new LinkedList<String>();					// list of misspelled words
		TreeMap<String,String> corrections = new TreeMap<String,String>();			// map from misspelled word to suggested spellin
		Iterator<NameItem> name_it = fp.nameItemIterator();
		NameItem next;
		LetterNeighbor data;
		char[] charArray;
		char[] twochar = new char[2];
		double[] oneLetterP, twoLetterP, prior;
		double isMisspelled;			// this is between 0 and 1, with some reasonable cutoff forcing the classification
		while ( name_it.hasNext() ) {
			isMisspelled = 0;
			next = name_it.next();
			charArray = next.get_name().toLowerCase().toCharArray();
			oneLetterP = new double[charArray.length-1];
			twoLetterP = new double[charArray.length-2];
			prior = new double[charArray.length];
			if ( next.get_gender() == "M" ) {
				data = ln[0];
			} else {
				data = ln[1];
			}
			for ( int i = 0 ; i < charArray.length-1; i++ ) {
				prior[i] = data.priorProbability(String.valueOf(charArray[i]));
				oneLetterP[i] = data.conditionalProbability( String.valueOf(charArray[i+1]), String.valueOf(charArray[i]) );
				if ( oneLetterP[i] == 0 ) {
					isMisspelled = 1;
				} else if ( oneLetterP[i] < 0.05 ) {
					isMisspelled += 0.25;
				}
			}
			
			for ( int i = 0; i < charArray.length - 2; i++ ) {
				twochar[0] = charArray[i];
				twochar[1] = charArray[i+1];
				prior[i] = data.priorProbability(String.valueOf(twochar));
				twoLetterP[i] = data.conditionalProbability( String.valueOf(charArray[i+2]), String.valueOf(twochar) );
			}
		}
		// TODO decision time

		HelperFunctions.writeListToFile(misspelled, OUTPUTPATH + "CandidateOutput1.txt");
	}
}
