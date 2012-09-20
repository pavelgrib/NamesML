package ml;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import def.*;

/**
 * This class is used to create histograms representing probabilities that certain letters or strings of letters are followed by another letter
 * Valid non-characters in names are - and '
 * lots of Sting comparisons everywhere... awful
 * @author paul
 */
public class LetterNeighbor {

	public static final int NCHARS = 28;
	private String _gender;
//	private Histogram _lettersPrior;
//	private Histogram _twoLettersPrior;
//	private Histogram _threeLettersPrior;
	private Histogram[] _histOne;
	private Histogram[] _histTwo;
	private Histogram[] _histThree;
	private Histogram[] _histFour;
	private TreeMap<String,Integer> _letters;
	private TreeMap<String, Integer> _twoLetterSequences;
	private TreeMap<String, Integer> _threeLetterSequences;
	
	public LetterNeighbor() {
		mapAll();
	}		// dummy, won't be used
	
	public LetterNeighbor(FileProcessor fp, String gender) throws Exception {
		set_gender(gender);
		init();
		this.analyze(fp);
//		this.sampleCorrect();
	}
	
	public LetterNeighbor(String filepath, String gender) throws Exception {
		FileProcessor fp = new FileProcessor(filepath, 1);
		init();
		this.analyze(fp);
//		this.sampleCorrect();
	}
	
	private void init() {
		mapAll();
		try {
//			_lettersPrior = new Histogram( NCHARS, 0, NCHARS );
//			_lettersPrior.setBinNames( _letters.keySet() );
//			_twoLettersPrior = new Histogram( NCHARS*NCHARS, 0, NCHARS*NCHARS );
//			_twoLettersPrior.setBinNames( _twoLetterSequences.keySet() );
//			_threeLettersPrior = new Histogram( NCHARS*NCHARS*NCHARS, 0, NCHARS*NCHARS*NCHARS );
			
			_histOne = new Histogram[NCHARS];					// p(a1 | b1)
			_histTwo = new Histogram[NCHARS*NCHARS];			// p(a1 | b2)
			_histThree = new Histogram[NCHARS];					// p(a2 | b1)
			_histFour = new Histogram[NCHARS*NCHARS];			// p(a2 | b2)
			char[] c2 = new char[2];
			for ( int i = 0; i < NCHARS; i++ ) {
				c2[0] = HelperFunctions.getKeyByValue(_letters, i).charAt(0);
				_histOne[i] = new Histogram(NCHARS, 0, NCHARS, false);
				_histOne[i].set_histname( String.valueOf(c2[0]) );
				_histOne[i].setBinNames( _letters.keySet() );
				
				_histThree[i] = new Histogram(NCHARS*NCHARS, 0, NCHARS*NCHARS, false);
				_histThree[i].set_histname( HelperFunctions.getKeyByValue(_letters, i) );
				_histThree[i].setBinNames( _twoLetterSequences.keySet() );
				for ( int j = 0; j < NCHARS; j++ ) {
					c2[1] = HelperFunctions.getKeyByValue(_letters, i).charAt(0);
					_histTwo[i*NCHARS + j] = new Histogram(NCHARS, 0, NCHARS, false);
					_histTwo[i*NCHARS + j].set_histname( String.valueOf(c2) );
					_histTwo[i*NCHARS + j].setBinNames(_letters.keySet() );
					
					_histFour[i*NCHARS + j] = new Histogram(NCHARS*NCHARS, 0, NCHARS, false);
					_histFour[i*NCHARS + j].set_histname( String.valueOf(c2) );
					_histFour[i*NCHARS + j].setBinNames( _twoLetterSequences.keySet() );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void analyze(FileProcessor fp) {
		Iterator<NameItem> it = fp.nameItemIterator();
		char[] charArray;
		char[] twoLetterArray = new char[2];
		char[] twoLetterArray2 = new char[2];
		int idx1, idxnext1, idx2, idxnext2, idxnext3, idxnext4;
		while ( it.hasNext() ) {
			charArray = it.next().get_name().toCharArray();
			for ( int i = 0; i < charArray.length-1; i++ ) {
				twoLetterArray[0] = charArray[i];
				
				idx1 = _letters.get(String.valueOf(charArray[i]).toLowerCase());						// index into histogram array for current letter
				idxnext1  = _letters.get(String.valueOf(charArray[i+1]).toLowerCase());					// index into bin in that histogram for following letter
				_histOne[idx1].add(idxnext1);															// adding instance
				if ( i < charArray.length-2 ) {
				
					twoLetterArray[1] = charArray[i+1];
					idxnext3 = _twoLetterSequences.get( String.valueOf(charArray[i]).toLowerCase() );	// index into histogram bins for current 2-string
					_histThree[idx1].add(idxnext3);														// adding letter conditional on 2-string
					
					idx2 = _twoLetterSequences.get( String.valueOf(twoLetterArray).toLowerCase() );		// index into histogram array for current 2-string
					idxnext2 = _letters.get( String.valueOf(charArray[i+2]).toLowerCase() );			// index into bin in that histogram
					_histTwo[idx2].add( idxnext2 );														// adding instance
					
					if ( i < charArray.length-3 ) {
						twoLetterArray2[0] = charArray[i+2];
						twoLetterArray2[1] = charArray[i+3];
						idxnext4 = _twoLetterSequences.get( String.valueOf(twoLetterArray2));			// index into bins in 2-string histogram array
						_histFour[idx2].add(idxnext4);													// adding instance
					}
				}
				
			}
		}
		
//		for ( Histogram h: _histOne ) {
//			System.out.println(h.get_histname());
//			h.printHist(false, true);
//			System.out.print("\n\n");
//		}

//		_twoLettersPrior.printHist(false, true);
//		for ( Histogram h: _histTwo ) {
//			System.out.println(h.get_histname());
//			h.printHist(false, true);
//			System.out.print("\n\n");
//		}
	}
	
	// goes through all the histograms and adds counts to take care of the zero probability problem
	public void sampleCorrect() {
		/* TODO add an observation to each bin of every histogram so long as there are not too many empty bins in that histogram
		 if lots of empty bins, then leave it alone, means that sequence isn't meant to be
		*/
		for ( Histogram h1: _histOne ) {
			for ( int i = 1; i < NCHARS; i++ ) {
				h1.add(i);
			}
		}
		for ( Histogram h3: _histThree ) {
			for ( int i = 1; i < NCHARS; i++ ) {
				h3.add(i);
			}
		}
		for ( Histogram h2: _histTwo ) {
			for ( int i = 1; i < NCHARS; i++ ) {
				for ( int j = 1; j < NCHARS; j++ ) {
					h2.add( i*NCHARS + j );
				}
			}
		}
		for ( Histogram h4: _histFour ) {
			for ( int i = 1; i < NCHARS; i++ ) {
				for ( int j = 1; j < NCHARS; j++ ) {
					h4.add( i*NCHARS + j );
				}
			}
		}
	}
	
//	public double priorProbability(String event) {
//		if ( event.length() == 1 ) {
//			return _lettersPrior.probability(event);
//		} else if ( event.length() == 2 ) {
//			return _twoLettersPrior.probability(event);
//		} else {
//			return 0;
//		}
//	}
	
	public double conditionalProbability(String event, String conditionalOn) {
		if ( conditionalOn.length() == 1 ) {
			return _histOne[_letters.get(String.valueOf(conditionalOn).toLowerCase())].probability(event);
		} else if ( conditionalOn.length() == 2 ) {
			return _histTwo[_twoLetterSequences.get(String.valueOf(conditionalOn).toLowerCase())].probability(event);
		} else {
			return 0;
		}
	}

	private void mapAll() {
		_letters = new TreeMap<String, Integer>();
		_twoLetterSequences = new TreeMap<String, Integer>();
		_threeLetterSequences = new TreeMap<String, Integer>();
		char[] chars2 = new char[2];
		char[] chars3 = new char[3];
		
		for ( char c1 = 'a'; c1 < ('a' + NCHARS-2); c1++ ) {
			_letters.put(String.valueOf(c1), (int) (c1 - 'a' + 2));
		}

		_letters.put(String.valueOf('-'), 0);			// this one only used for last names
		_letters.put(String.valueOf('\''), 1);

		char c1, c2, c3;
		int v1, v2, v3;
		Set<String> keys = _letters.keySet();
		for ( String s1: keys ) {
			c1 = s1.charAt(0);
			v1 = _letters.get(s1);
			chars2[0] = c1;
			chars3[0] = c1;
			for ( String s2: keys ) {
				c2 = s2.charAt(0);
				v2 = _letters.get(s2);
				chars2[1] = c2;
				chars2[1] = c2;
				_twoLetterSequences.put(String.valueOf(chars2), v1*NCHARS + v2);
				for ( String s3: keys ) {
					c3 = s3.charAt(0);
					v3 = _letters.get(s3);
					chars3[2] = c3;
					_threeLetterSequences.put(String.valueOf(chars3), v1*NCHARS*NCHARS + v2*NCHARS + v3);
				}
			}
		}
	}
	
	public void printStringValues() {
		for ( String s: _threeLetterSequences.keySet() ) {
			System.out.println(_threeLetterSequences.get(s));
		}
	}
	
	// returns likeliest 'len'-length string to follow 'str' excluding any entries in 'excluding' list
	public String likeliestFollowing(String str, int len, List<String> excluding) {
		String out = null;
		Histogram[] histArray;
		TreeMap<String, Integer> strMap = null;
		if ( len == 1 ) {
			strMap = _letters;
			if ( str.length() == 1 ) {
				histArray = _histOne;
			} else {
				histArray = _histThree;
			}
		} else {
			if ( str.length() == 1 ) {
				histArray = _histTwo;
			} else {
				histArray = _histFour;
			}
		}
		out = histArray[strMap.get(str)].maxCountAtName();
		out = excluding.contains(out) ? histArray[strMap.get(str)].maxCountAt(excluding) : histArray[strMap.get(str)].maxCountAtName();
		return out;
 	}
	
	public void set_gender(String gender) {
		_gender = gender;
	}
	
	public String get_gender() {
		return _gender;
	}
}