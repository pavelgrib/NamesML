package def;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class MisspellingGenerator {
	
	private static final int NUM_MISSPELLINGS = 5;
	private static final char[] SYMBOLS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', ',', '@', '#', '$', '%', '^', '&', '*', '(', ')', '{', '}', '[', ']', '\'', '\"', '-', '=', '<', '>', '/', '?', '\\', ' ', '\t'};
	private Random _generator;
	private FileProcessor _fp;
	
	public MisspellingGenerator(String filepath, int ncol) {
		_generator = new Random();
		_fp = new FileProcessor(filepath, ncol);
	}
	
	public void generateMisspelledFile(String filepath, String gender) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
			Iterator<NameItem> ni_it = _fp.nameItemIterator();
			String nextName, nextPostcode, nextGender, nextDOB;
			while ( ni_it.hasNext() ) {
				nextName = ni_it.next().get_name();
				nextName = generateMisspelledName(nextName);
				if ( _fp.get_ncol() == 1 ) {
					bw.write(nextName + "\n");
				} else {
					nextPostcode = randomPostcode();
					if ( gender != "M" || gender != "F" ) {
						nextGender = randomGender();
					} else {
						nextGender = gender;
					}
					nextDOB = randomDOB();
					bw.write("\"" + nextName + "\"\t" + nextGender + "\"\t" + nextPostcode + "\"\t" + nextDOB + "\n");
				}
			}
			bw.flush();
			bw.close();
			System.out.println("misspellings written out to " + filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedList<String> generateMisspelledList(String gender) {
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
	
	private String generateMisspelledName(String fromName) {
		String misspelled = null;
		if ( _generator.nextBoolean() ) {
			int misspelling = _generator.nextInt(NUM_MISSPELLINGS);
			switch ( misspelling ) {
				case 0: misspelled = transposeNeighborLetters(fromName, _generator.nextInt(fromName.length() - 2)); break;
				case 1: misspelled = insertSymbol(fromName, SYMBOLS[_generator.nextInt(SYMBOLS.length-1)], _generator.nextInt(fromName.length() - 2)); break;
				case 2: misspelled = omitLetter(fromName, _generator.nextInt(fromName.length() - 2)); break;
				case 3: misspelled = repeatLetter(fromName, _generator.nextInt(fromName.length() - 2)); break;
				case 4: misspelled = takeOutRepeatingLetter(fromName); break;
				default: misspelled = fromName;
			}
		}
		return misspelled;
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

	private String randomPostcode() {
		int num = _generator.nextInt(10);
		String str = String.valueOf(num);
		str += String.valueOf((char)(_generator.nextInt(26) + 'a')).toUpperCase();
		if ( _generator.nextBoolean() ) {
			str += String.valueOf((char)(_generator.nextInt(26) + 'a')).toUpperCase();
		}
		return str;
	}
	
	private String randomGender() {
		return ( _generator.nextBoolean() ? "M" : "F");
	}
	
	private String randomDOB() {
		return String.valueOf((int) Math.round(1975.0 + 20.0*_generator.nextGaussian()));
	}
}
