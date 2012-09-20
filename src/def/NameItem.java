package def;

import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * Container class for an item in the input file
 *
 * @author paul.
 */
public class NameItem {
	private String _name;
	private String _lastname;
	private String _gender;
	private String _postcode;
	private String _dob;
	private boolean _initiallyMisspelled;
	
	// TODO might need error handling for poorly formatted lines, i.e. no tab delimiters, etc.
	public NameItem(String line, int ncol) {
		String[] components = line.split("\t");
		if ( ncol == 1 ) {
			set_name(components[0]);
			set_gender(null);
			set_postcode(null);
			set_dob(null);
		} else {
			_initiallyMisspelled = false;
			set_names(components[0].split("\"")[1]);
			set_gender(components[1]);
			set_postcode(components[2]);
			set_dob(components[3]);
		}
	}

	public NameItem(String name, String gender, String postcode, String dob) {
		set_names(name);
		set_gender(gender);
		set_postcode(postcode);
		set_dob(dob);
	}
	
	public int[] vowelConsonantArray() {
		char[] nameChar = _name.toCharArray();
		int[] array = new int[nameChar.length];
		for ( int i = 0; i < array.length; i++ ) {
			array[i] = isVowel(nameChar[i]);
		}
		return array;
	}
	
	public int isVowel(char c) {
		if ( c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y' ) {
			return 1;
		} else {
			return 0;
		}
	}
		
	// wasteland of getters and setters
	public String get_name() {
		return this._name;
	}

	public void set_name(String name) {
		_name = name;
	}
	
	public void set_names(String name) {
		name.trim();
		char[] in = name.toCharArray();
		char[] out = new char[in.length];
//		char[] first = new char[in.length];
//		char[] last = new char[in.length];
		ArrayIterator out_it = new ArrayIterator(in);
//		int idx = 0;
		char next;
		for ( int i = 0; i < in.length; i++ ) {
			next = (Character) out_it.next();
			if ( !(Character.isLetter(next) || next == '\'' || next == '-' || next == ' ') ) {
				_initiallyMisspelled = true;
			} else if ( i < out.length - 1 ) {
				if ( !(in[i] == ' ' && in[i+1] == ' ') ) {
					out[i] = in[i];
				}
			} else {
				out[i] = in[i];
			}
		}
		
//		while ( out[idx] != '' ) {
//			first[idx] = out[idx];
//			idx++;
//		}
//		idx++;
//		while ( idx < out.length ) {
//			last[idx - first.length] = out[idx];
//		}
		String[] temp = String.valueOf(out).split(" ");
		set_name(temp[0].trim());
		if ( temp.length > 1 ) {
			set_lastname(temp[1].trim());
		} else {
			set_lastname(temp[0].trim());
		}
	}

	public String get_gender() {
		return this._gender;
	}

	public void set_gender(String gender) {
		this._gender = gender;
	}

	public String get_postcode() {
		return this._postcode;
	}

	public void set_postcode(String postcode) {
		this._postcode = postcode;
	}
	
	public String get_dob() {
		return this._dob;
	}


	public void set_dob(String dob) {
		this._dob = dob;
	}

	public String get_lastname() {
		return _lastname;
	}

	public void set_lastname(String _lastname) {
		this._lastname = _lastname;
	}
	
	public String toString() {
		return HelperFunctions.surroundWithQuotes( this.get_name() + " " + this.get_lastname() ); 
	}
	
	public boolean initially_misspelled() {
		return _initiallyMisspelled;
	}
}
