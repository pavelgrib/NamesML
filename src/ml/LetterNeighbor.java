package ml;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import def.*;

/**
 * TODO Put here a description of what this class does.
 *
 * @author paul.
 */
public class LetterNeighbor {

	public static final int NCHARS = 26;
	private Histogram[] _hist;
	private LinkedHashMap<Character,Integer> _letters;
	
	public LetterNeighbor(String filepath) throws Exception {
		mapLetters();
		FileProcessor fp = new FileProcessor(filepath, 1);
		_hist = new Histogram[NCHARS];
		LinkedList<Character> lettersExcludeOne = (LinkedList<Character>)_letters.keySet();
		for ( int i = 0; i < NCHARS; i++ ) {
			lettersExcludeOne.remove('a' + i);
			_hist[i] = new Histogram(25, 0, 25);
			Character c = (char) ('a' + i);
			_hist[i].set_histname(c.toString());
			_hist[i].setBinNames(lettersExcludeOne);
		}
		this.analyze(fp);
	}
	
	private void analyze(FileProcessor fp) {
		Iterator<NameItem> it = fp.nameItemIterator();
		char[] charArray;
		while ( it.hasNext() ) {
			charArray = it.next().get_name().toCharArray();
			for ( int i = 0; i < charArray.length-1; i++ ) {
				int idx = _letters.get(Character.toLowerCase(charArray[i]));
				int idxnext  = _letters.get(Character.toLowerCase(charArray[i+1]));
				_hist[idx].add(idxnext);
			}
		}
		
		for ( Histogram h: _hist ) {
			System.out.println(h.get_histname());
			h.printHist(false, true);
			System.out.print("\n\n");
		}
	}
	
	private void mapLetters() {
		_letters = new LinkedHashMap<Character, Integer>();
		for ( char c = 'a'; c < ('a' + NCHARS); c++ ) {
			_letters.put(c, c - 'a');
		}
	}
}

