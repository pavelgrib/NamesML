package ml;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO Add items to Histogram and it groups them into Bins to keep track of frequency and probability
 *
 * @author paul.
 */

public class Histogram {

	private static final int MAX_PRINT = 100;
	private TreeMap<Double, Bin> _bins;
	private int _numBins, _num;
	private double _min, _max;
	private Bin _minOverflow, _maxOverflow;
	private String _histName;
	
	public Histogram(int n, double min, double max) {
		_num = 0;
		set_min(min);
		set_max(max);
		_histName = null;
		_numBins = n;
		_minOverflow = new Bin(Double.NEGATIVE_INFINITY, _min);
		_maxOverflow = new Bin(_max, Double.POSITIVE_INFINITY);
		_bins = new TreeMap<Double, Bin>();
		for ( int i = 0; i < n; i++ ) {
			System.out.println("Bin " + i + " covering [" + ((_min*(_numBins-i) + i*_max) / _numBins) + ", " + ((_min*(_numBins-i-1) + (i+1)*_max) / _numBins) + ")");  
			_bins.put((_min*(_numBins-i) + i*_max) / _numBins, new Bin((_min*(_numBins-i) + i*_max) / _numBins, (_min*(_numBins-i-1) + (i+1)*_max) / _numBins));
//			_bins[i] = new Bin(_min + i / (_max - _min), _max);
		}
	}
	
	public void add(double x) {
		if ( x < _min ) {
			_minOverflow.add(x);
		} else if ( x >= _max ) {
			_maxOverflow.add(x);
		} else {
//			Bin b = _bins.get(Double.toString(_min + _increment * Math.floor(x-_min)));   //_bins[(int) Math.floor((x - _min) / _increment)];
			Bin b = _bins.get( _bins.floorKey(Math.floor((x - _min) *_numBins / (_max - _min)) / _max ) );
			b.add(x);
		}
		_num++;
	}

	public int freq(double x) {
		if ( x < _min ) {
			return _minOverflow.freq();
		} else if ( x >= _max ) {
			return _maxOverflow.freq();
		} else {
			return _bins.get( _bins.floorKey( _bins.floorKey(Math.floor((x - _min) *_numBins / (_max - _min)) / _max ) ) ).freq();
		}
	}
	
	public double probability(double x) {
		return (double) freq(x) / (double) get_num();
	}
	
	public double probability(String name) {
		for ( Map.Entry<Double,Bin> entry: _bins.entrySet() ) {
			if ( entry.getValue().get_name() == name ) {
				return entry.getValue().freq() / (double) get_num();
			}
		}
		return 0;
	}
	
	public double cumulative(double x) {
		double c = 0, max = Double.NEGATIVE_INFINITY;
		if ( x < _bins.get(Double.toString(_min)).get_min() ) {
			return this.probability(x);
		} else if ( x > _maxOverflow.get_min() ) {
			return _maxOverflow.avg();
		} else {
			c += _minOverflow.get_max();
			max = _minOverflow.get_max();
		}
		ArrayListIterator bin_it = new ArrayListIterator(_bins);
		while( bin_it.hasNext() && max < ((Bin) bin_it.next()).get_min() ) {
			c += this.probability(x);
		}
		return c;
	}
	
	public void setBinNames(Set<String> names) throws Exception {
		String nextName;
		Iterator<String> name_it = names.iterator();
		Iterator<Entry<Double, Bin>> bin_it = _bins.entrySet().iterator();
		if ( names.size() == _bins.size() ) {
			while ( name_it.hasNext() && bin_it.hasNext() ) {
				nextName = name_it.next();
				bin_it.next().getValue().set_name(nextName);
			}
		} else {
			throw new Exception("attempting to assign a list of " + names.size() + " strings to names of " + _bins.size() + " bins.");
		}
	}
	
	public int maxCount() {
		int maxC = 0;
		for ( Map.Entry<Double, Bin> b: _bins.entrySet() ) {   //Bin b: _bins ) {
			if (b.getValue().freq() > maxC) {
				maxC = b.getValue().freq();
			}
		}
		return maxC;
	}

	public void printHist(boolean includeOverflow, boolean includeBinNames) {
		double maxC = this.maxCount();
		String repStr = "|";
		String printStr = "";
		if ( includeOverflow ) {
			 printStr = includeBinNames ? _minOverflow.get_name() + " " : "";
			 printStr += StringUtils.repeat( repStr, (int) (MAX_PRINT*(_minOverflow.freq() / this.get_num())) );
		}
		for ( Map.Entry<Double, Bin> b: _bins.entrySet() ) {
			printStr += (includeBinNames ? b.getValue().get_name() : "" ) + StringUtils.repeat( repStr, (int) (MAX_PRINT*(b.getValue().freq() / maxC)) ) + "\n";
		}
		if ( includeOverflow ) {
			printStr += StringUtils.repeat( repStr, (int) (MAX_PRINT*(_maxOverflow.freq() / maxC)) ) + "\n\n";
		}
		
		System.out.print(printStr);
	}
	
	public double get_max() {
		return _max;
	}

	public void set_max(double max) {
		this._max = max;
	}

	public double get_min() {
		return _min;
	}

	public void set_min(double min) {
		this._min = min;
	}

	public void set_histname(String name) {
		_histName = name;
	}
	
	public String get_histname() {
		return _histName;
	}
	
	private int get_num() {
		return _num;
	}
}
