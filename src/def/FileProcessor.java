package def;

/**
 * Processes input from the input file; _inputs LinkedList contains the data
 * @author paul.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class FileProcessor {

	private String _path;
	private LinkedList<NameItem> _inputs;
	private int _ncol;
	
	public FileProcessor(final String path, int ncol) {
		_ncol = ncol;
		_inputs = new LinkedList<NameItem>();
		_path = path;
		try {
		 	BufferedReader br = new BufferedReader(new FileReader(this._path));
			this.process(br);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void printAllNames() {
		ListIterator<NameItem> i = this._inputs.listIterator();
		while ( i.hasNext() ) {
			System.out.println(i.next().get_name());
		}
	 	
	}
	
	public Iterator<NameItem> nameItemIterator() {
		return _inputs.iterator();
	}
	
	public void printeFile() {
		ListIterator<NameItem> i = this._inputs.listIterator();
		NameItem ni = null;
		while ( i.hasNext() ) {
			ni = i.next();
			System.out.println(ni.get_name() + (_ncol > 1 ? "\t" + ni.get_gender() + "\t" + ni.get_postcode() + "\t" + ni.get_dob() : ""));
		}
	}
	
	private void process(BufferedReader br) {
		String line;
		try {
			while ( (line = br.readLine()) != null ) {
				this._inputs.add(new NameItem(line, _ncol));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	public static void main(String[] args) {

	}


}
