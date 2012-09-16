package ml;

import java.util.ListIterator;
import java.util.LinkedList;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO Add items to object and it groups them according to frequency into Bins
 *
 * @author paul.
 */

public class Histogram {

	private Bin[] _bins;
	private double _min, _max, _increment;
	private int _numBins, _num;
	private Bin _minOverflow, _maxOverflow;
	private String _histName;
	
	public Histogram(int n, double min, double max) {
		set_num(0);
		set_min(min);
		set_max(max);
		_histName = null;
		_numBins = n;
		_increment = (_max - _min) / _numBins;
		_minOverflow = new Bin(Double.NEGATIVE_INFINITY, _min);
		_maxOverflow = new Bin(_max, Double.POSITIVE_INFINITY);
		_bins = new Bin[n];
		for ( int i = 0; i < n; i++ ) {
			_bins[i] = new Bin(_min + i / (_max - _min), _max);
		}
	}
	
	public void add(double x) {
		if ( x < _min ) {
			_minOverflow.add(x);
		} else if ( x >= _max ) {
			_maxOverflow.add(x);
		} else {
			Bin b = _bins[(int) Math.floor((x - _min) / _increment)];
			b.add(x);
		}
	}

	public int freq(double x) {
		if ( x < _min ) {
			return _minOverflow.freq();
		} else if ( x >= _max ) {
			return _maxOverflow.freq();
		} else {
			return _bins[(int) Math.floor((x - _min) / _increment)].freq();
		}
	}
	
	public double probability(double x) {
		return (double) freq(x) / (double) get_num();
	}
	
	public double cumulative(double x) {
		double c = 0, max = Double.NEGATIVE_INFINITY;
		if ( x < _bins[0].get_min() ) {
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
	
	public void setBinNames(LinkedList<Character> lettersExcludeOne) throws Exception {
		ListIterator<Character> it = lettersExcludeOne.listIterator();
		if ( lettersExcludeOne.size() == _bins.length ) {
			while ( it.hasNext() ) {
				
			}
		} else {
			throw new Exception("attempting to assign a list of " + lettersExcludeOne.size() + " strings to names of " + _bins.length + " bins.");
		}
	}
	
	public int maxCount() {
		int maxC = 0;
		for ( Bin b: _bins ) {
			if (b.freq() > maxC) {
				maxC = b.freq();
			}
		}
		return maxC;
	}

	public void printHist(boolean includeOverflow, boolean includeBinNames) {
		double maxC = this.maxCount();
		String str = "|";
		String printStr = "";
		if ( includeOverflow ) {
			 printStr = includeBinNames ? _minOverflow.get_name() + " " : "";
			 printStr += StringUtils.repeat( str, (int) (200*(_minOverflow.freq() / maxC)) );
		}
		for ( Bin b: _bins ) {
			printStr += StringUtils.repeat( str, (int) (200*(b.freq() / maxC)) ) + "\n";
		}
		if ( includeOverflow ) {
			printStr += StringUtils.repeat( str, (int) (200*(_maxOverflow.freq() / maxC)) ) + "\n\n";
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
	
	private void set_num(int num) {
		_num = num;
	}

	private double get_num() {
		return _num;
	}
}
