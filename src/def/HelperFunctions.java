package def;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public final class HelperFunctions {

	private HelperFunctions() {}
	
	public static <T> void writeListToFile(List<T> l, String filepath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
			ListIterator<T> it = l.listIterator();
			while ( it.hasNext() ) {
				bw.write(it.next().toString() + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static <T> void writeListToConsole(List<T> l) {
		ListIterator<T> it = l.listIterator();
		while ( it.hasNext() ) {
			System.out.println(it.next().toString());
		}
	}
	
	public static void printDirectoryContents(String directory) {
		HelperFunctions.printDirectoryContents(directory, 0);
	}
	
	private static void printDirectoryContents(String directory, int ntabs) {
		File[] dir = new File(directory).listFiles();
		for ( File d: dir) {
			System.out.print(StringUtils.repeat('\t',ntabs) + d.getName());
			if ( d.isDirectory() ) {
				System.out.print("/\n");
				printDirectoryContents(d.getAbsolutePath(), ntabs + 1);
			} else {
				System.out.print("\n");
			}
		}
	}
	
	public static double sum(double[] array) {
		double sum = 0.0;
		for ( double x: array ) {
			sum += x;
		}
		return sum;
	}
	
	// note: use with caution, tendency to explode
	public static double product(double[] array) {
		double prod = 1;
		for ( double x: array ) {
			prod *= x;
		}
		return prod;
	}

	@SuppressWarnings("resource")
	public static void printFileLinesToConsole(String file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
