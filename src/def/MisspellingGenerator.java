package def;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class MisspellingGenerator {
	
	private static final int NUM_MISSPELLINGS = 5;
	private static final char[] SYMBOLS = {'!', ',', '@', '#', '$', '%', '^', '&', '*', '(', ')', '{', '}', '[', ']', '\'', '\"', '-', '=', '<', '>', '/', '?', '\\'};
	private Random _generator;
	private FileProcessor _fp;
	
	public MisspellingGenerator(String filepath) {
		_generator = new Random();
		_fp = new FileProcessor(filepath, 1);
	}
	
	public void generateMisspelledFile(String filepath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
			Iterator<NameItem> ni_it = _fp.nameItemIterator();
			String next;
			while ( ni_it.hasNext() ) {
				next = ni_it.next().get_name();
				next = generateMisspelledName(next);
				bw.write(next + "\n");
			}
			bw.flush();
			bw.close();
			System.out.println("misspellings written out to " + filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedList<String> generateMisspelledList() {
		LinkedList<String> misspelledList = new LinkedList<String>();
		Iterator<NameItem> ni_it = _fp.nameItemIterator();
		String next = "";
		while ( ni_it.hasNext() ) {
			next = ni_it.next().get_name();
			next = generateMisspelledName(next);
			misspelledList.add(next);
		}
		return misspelledList;
	}
	
	private String generateMisspelledName(String next) {
		if ( _generator.nextBoolean() ) {
			int misspelling = _generator.nextInt(NUM_MISSPELLINGS);
			switch ( misspelling ) {
				case 0: next = transposeNeighborLetters(next, _generator.nextInt(next.length() - 2)); break;
				case 1: next = insertSymbol(next, SYMBOLS[_generator.nextInt(SYMBOLS.length-1)], _generator.nextInt(next.length() - 2)); break;
				case 2: next = omitLetter(next, _generator.nextInt(next.length() - 2)); break;
				case 3: next = repeatLetter(next, _generator.nextInt(next.length() - 2)); break;
				case 4: next = takeOutRepeatingLetter(next); break;
			}
		}
		return next;
	}
	
	// transposes letters at index and index+1 in the string name
	private String transposeNeighborLetters(String name, int index) {
		int l = name.length();
		if ( l < 2 ) {
			return name;
		} else {
			String str1 = (index > 0 ? name.substring(0, index) : "");
			String str2 = (index < name.length() ? name.substring(index+1, name.length()) : "");
			return str1 + name.charAt(index+1) + name.charAt(index) + str2;
		}
	}
	
	// inserts symbol into the name after index
	private String insertSymbol(String name, char symbol, int index) {
		int l = name.length();
		if ( l < 1 ) {
			return name;
		} else {
			String str1 = name.substring(0, index-1);
			String str2 = name.substring(index+1);
			return str1 + symbol + str2;
		}
	}
		
	// takes out letter at index in name
	private String omitLetter(String name, int index) {
		int l = name.length();
		if ( l < 1 ) {
			return name;
		} else {
			String str1 = name.substring(0, index-1);
			String str2 = name.substring(index+1);
			return str1 + str2;
		}
	}
	
	// repeats a letter one more time
	private String repeatLetter(String name, int index) {
		int l = name.length();
		if ( l < 1 ) {
			return name;
		} else {
			String str1 = name.substring(0, index);
			String str2 = name.substring(index+1);
			return str1 + name.charAt(index) + str2;
		}
	}
	
	// Williams --> Wiliams
	private String takeOutRepeatingLetter(String name) {
		char[] nameArray = name.toCharArray();
		boolean containsRepeating = false;
		int i;
		for ( i = 0; i < nameArray.length; i++ ) {
			if ( nameArray[i] == nameArray[i+1] ) {
				containsRepeating = true;
				break;
			}
		}
		if ( containsRepeating ) {
			return omitLetter(name, i);
		} else {
			return name;
		}
	}
}
