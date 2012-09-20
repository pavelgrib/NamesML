package def;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.iterators.ArrayIterator;
//import org.apache.commons.collections.iterators.ArrayIterator;
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
	
	public static String eclosingDir(String file) {
		String[] temp = file.split("/");
		String output = "/";
		for ( int i = 0; i < temp.length-1; i++ ) {
			output += temp[i] + "/";
		}
		return output;
	}
	
	public static String fileNumber(String file) {
		char[] arr = file.split("\\.")[0].toCharArray();
		char[] out = new char[4];
		for ( int i = 0; i < arr.length; i++ ) {
			if ( Character.isDigit(arr[i]) ) {
				for ( int j = i; j < arr.length; j++ ) {
					out[j-i] = arr[j];
				}
			    break;
			}
		}
		return  String.valueOf(out);
	}
	
	public static <K, V> void writeMapToFile(Map<K, V> map, String filepath) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
			Set<K> keys = map.keySet();
			for ( Iterator<K> i = keys.iterator(); i.hasNext(); ) {
			     K key = i.next();
			     V value = map.get(key);
				bw.write(key.toString() + "," + value.toString()  + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String surroundWithQuotes(String s) {
		int l = s.length();
		char[] s_arr = s.toCharArray();
		char[] out = new char[2 + l];
		out[0] = '\"';
		for ( int i = 0; i < l; i++ ) {
			out[i+1] = s_arr[i]; 
		}
		out[l+1] = '\"';
		return String.valueOf(out);
	}

	public static double min(double[] array) {
		double min = Double.POSITIVE_INFINITY;
		for ( double x: array ) {
			min = ( x < min ? x: min);
		}
		return min;
	}

	public static double max(double[] array) {
		double max = Double.NEGATIVE_INFINITY;
		for ( double x: array ) {
			max = ( x > max ? x: max);
		}
		return max;
	}

	public static String purgeWhitespace(String s) {
		char[] arr = s.toCharArray();
		char next;
		ArrayIterator it = new ArrayIterator(arr);
		while ( it.hasNext() ) {
			if ( Character.isWhitespace((Character) it.next())) {
				it.remove();
			}
		}
		return String.valueOf(arr);
	}
	
	public static int minAt(double[] array) {
		double min = Double.POSITIVE_INFINITY;
		int idx = 0;
		for ( int i = 0; i < array.length; i++ ) {
			if ( array[i] < min ) {
				min = array[i];
				idx = i;
			}
		}
		return idx;
	}
}
