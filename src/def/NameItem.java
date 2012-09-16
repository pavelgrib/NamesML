package def;

/**
 * Container class for an item in the input file
 *
 * @author paul.
 */
public class NameItem {
	private String _name;
	private String _gender;
	private String _postcode;
	private String _dob;
	
	// TODO might need error handling for poorly formatted lines, i.e. no tab delimiters, etc.
	public NameItem(String line, int ncol) {
		String[] components = line.split("\t");
		if ( ncol == 1 ) {
			set_name(components[0]);
			set_gender(null);
			set_postcode(null);
			set_dob(null);
		} else {
			set_name(components[0].split("\"")[1]);
			set_gender(components[1]);
			set_postcode(components[2]);
			set_dob(components[3]);
		}
	}

	public NameItem(String name, String gender, String postcode, String dob) {
		set_name(name);
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

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_gender() {
		return this._gender;
	}

	public void set_gender(String _gender) {
		this._gender = _gender;
	}

	public String get_postcode() {
		return this._postcode;
	}

	public void set_postcode(String _postcode) {
		this._postcode = _postcode;
	}
	
	public String get_dob() {
		return this._dob;
	}


	public void set_dob(String _dob) {
		this._dob = _dob;
	}
}
