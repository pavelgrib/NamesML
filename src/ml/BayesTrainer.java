package ml;

import java.io.File;
import java.util.HashMap;

import def.FileProcessor;

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
	public void bayesClassify(FileProcessor fp, LetterNeighbor ln) {
		
	}
}
