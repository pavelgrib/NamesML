package ml;

import java.util.Iterator;
import java.util.TreeMap;

import def.*;

/**
 * TODO This class is used to create histograms representing probabilities that certain letters or strings of letters are followed by another letter
 *
 * @author paul.
 */
public class LetterNeighbor {

	public static final int NCHARS = 26;
	private Histogram _lettersPrior;
	private Histogram[] _histOne;
	private Histogram[] _histTwo;
	private TreeMap<String,Integer> _letters;
	private TreeMap<String, Integer> _twoLetterSequences;
	
	public LetterNeighbor(String filepath) throws Exception {
		mapLetters();
		mapTwoLetterSequences();
		FileProcessor fp = new FileProcessor(filepath, 1);
		_lettersPrior = new Histogram(NCHARS, 0, NCHARS);
		_histOne = new Histogram[NCHARS];
		_histTwo = new Histogram[NCHARS*NCHARS];
		char[] c2 = new char[2];
		for ( int i = 0; i < NCHARS; i++ ) {
			// initializing one-letter analysis
			_histOne[i] = new Histogram(NCHARS, 0, NCHARS);
			_histOne[i].set_histname(String.valueOf((char) ('a' + i)));
			_histOne[i].setBinNames(_letters.keySet());
			
			// initializing two-letter analysis
			for ( int j = 0; j < NCHARS; j++ ) {
				_histTwo[i*NCHARS + j] = new Histogram(NCHARS, 0, NCHARS);
				c2 = new char[] {(char)('a' + i), (char)('a' + j)};
				_histTwo[i*NCHARS + j].set_histname(String.valueOf(c2));
				_histTwo[i*NCHARS + j].setBinNames(_letters.keySet());
			}
		}
		this.analyze(fp);
	}
		
	private void analyze(FileProcessor fp) {
		Iterator<NameItem> it = fp.nameItemIterator();
		char[] charArray;
		char[] twoLetterArray = new char[2];
		int idx, idxnext, idx2, idxnext2;
		while ( it.hasNext() ) {
			charArray = it.next().get_name().toCharArray();
			for ( int i = 0; i < charArray.length-1; i++ ) {
				_lettersPrior.add(_letters.get(String.valueOf(charArray[i]).toLowerCase()));	// adding to prior to calculate prior letter probabilities
				idx = _letters.get(String.valueOf(charArray[i]).toLowerCase());
				idxnext  = _letters.get(String.valueOf(charArray[i+1]).toLowerCase());
				_histOne[idx].add(idxnext);												// adding to 1-letter conditional probabilities
				
				if ( i < charArray.length-2 ) {
					twoLetterArray[0] = charArray[i];
					twoLetterArray[1] = charArray[i+1];
					idx2 = _twoLetterSequences.get(String.valueOf(twoLetterArray).toLowerCase());
					idxnext2 = _letters.get(String.valueOf(charArray[i+2]).toLowerCase());
					_histTwo[idx2].add(idxnext2);										// adding to 2-letter conditional probabilities
				}
				
			}
			
			for ( int i = 0; i < charArray.length - 2; i++ ) {
				
			}
		}

//		for ( Histogram h: _hist ) {
//			System.out.println(h.get_histname());
//			h.printHist(false, true);
//			System.out.print("\n\n");
//		}
	}
	
	private void mapLetters() {
		_letters = new TreeMap<String, Integer>();
		for ( char c = 'a'; c < ('a' + NCHARS); c++ ) {
			_letters.put(String.valueOf(c), c - 'a');
		}
	}
	
	private void mapTwoLetterSequences() {
		_twoLetterSequences = new TreeMap<String, Integer>();
		char[] tempChars = new char[2];
		for ( char c1 = 'a'; c1 < ('a' + NCHARS); c1++ ) {
			tempChars[0] = c1;
			for ( char c2 = 'a'; c2 < ('a' + NCHARS); c2++ ) {
				tempChars[1] = c2;
				_twoLetterSequences.put(String.valueOf(tempChars), (c1-'a')*NCHARS + (c2 - 'a'));
			}
		}
	}
}

