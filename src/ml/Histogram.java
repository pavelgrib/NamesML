package ml;

import java.util.Iterator;
import java.util.List;
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
	private int _numBins, _count, _maxBinCount;
	private double _min, _max;
	private Bin _minOverflow, _maxOverflow;
	private String _histName, _maxBinCountAt;
	private boolean _overflow;
	
	public Histogram(int n, double min, double max, boolean overflow) {
		_count = 0;
		set_min(min);
		set_max(max);
		set_maxC(0);
		_histName = null;
		_maxBinCountAt = null;
		_numBins = n;
		_overflow = overflow;
		if ( overflow ) {
			_minOverflow = new Bin(Double.NEGATIVE_INFINITY, _min);
			_maxOverflow = new Bin(_max, Double.POSITIVE_INFINITY);
		}
		_bins = new TreeMap<Double, Bin>();
		for ( int i = 0; i < n; i++ ) {
//			System.out.println("Bin " + i + " covering [" + ((_min*(_numBins-i) + i*_max) / _numBins) + ", " + ((_min*(_numBins-i-1) + (i+1)*_max) / _numBins) + ")");  
			_bins.put((_min*(_numBins-i) + i*_max) / _numBins, new Bin((_min*(_numBins-i) + i*_max) / _numBins, (_min*(_numBins-i-1) + (i+1)*_max) / _numBins));
		}
	}
	
	public void add(double x) {
		if ( _overflow ) {
			if ( x < _min ) {
				_minOverflow.add(x);
			} else if ( x >= _max ) {
				_maxOverflow.add(x);
			}
			_count++;
		} 
		
		if ( x >= _min && x < _max ) {
//			Bin b = _bins.get(Double.toString(_min + _increment * Math.floor(x-_min)));   //_bins[(int) Math.floor((x - _min) / _increment)];
			Bin b = _bins.get( _min + Math.floor(_numBins * (x - _min) / (_max - _min))*(_max - _min) / _numBins );
			b.add(x);
			if ( b.freq() > _maxBinCount ) {
				_maxBinCount = b.freq();
				_maxBinCountAt = b.get_name();
			}
			_count++;
		}
	}

	public void add(double x, int n) {
		for ( int i = 0; i < n; i++ ) {
			this.add(x);
		}
	}
	
	public int freq(double x) {
		if ( x < _min ) {
			return _minOverflow.freq();
		} else if ( x >= _max ) {
			return _maxOverflow.freq();
		} else {
			return _bins.get( _min + Math.floor(_numBins * (x - _min) / (_max - _min))*(_max - _min) / _numBins ).freq();
		}
	}
	
	public double probability(double x) {
		return (double) freq(x) / (double) get_count();
	}
	
	public double probability(String name) {
		for ( Map.Entry<Double,Bin> entry: _bins.entrySet() ) {
			if ( entry.getValue().get_name() == name ) {
				return entry.getValue().freq() / (double) get_count();
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
	
	public String maxCountAt(List<String> excluding) {
		int maxCValue = 0;
		String maxCKey = null;
		for ( Map.Entry<Double, Bin> b: _bins.entrySet() ) {   //Bin b: _bins ) {
			if ( !excluding.contains(b.getValue().get_name()) ) {
				if (b.getValue().freq() > maxCValue) {
					maxCValue = b.getValue().freq();
					maxCKey = b.getValue().get_name();
				}
			}
		}
		return maxCKey;
	}

	public void printHist(boolean includeOverflow, boolean includeBinNames) {
		double maxC = this.maxBinCount();
		String repStr = "|";
		String printStr = "";
		if ( includeOverflow ) {
			 printStr = includeBinNames ? _minOverflow.get_name() + " " : "";
			 printStr += StringUtils.repeat( repStr, (int) (MAX_PRINT*(_minOverflow.freq() / this.get_count())) );
		}
		for ( Map.Entry<Double, Bin> b: _bins.entrySet() ) {
			printStr += (includeBinNames ? b.getValue().get_name() : "" ) + StringUtils.repeat( repStr, (int) (MAX_PRINT*(b.getValue().freq() / maxC)) ) + "\n";
		}
		if ( includeOverflow ) {
			printStr += StringUtils.repeat( repStr, (int) (MAX_PRINT*(_maxOverflow.freq() / maxC)) ) + "\n\n";
		}
		
		System.out.print(printStr);
	}
	
	public Iterator<Entry<Double, Bin>> binEntryIterator() {
		return this._bins.entrySet().iterator();
	}
	
	public static Histogram combineHistograms(Histogram h1, Histogram h2) {
		Iterator<Entry<Double, Bin>> bins1 = h1.binEntryIterator();
		Iterator<Entry<Double, Bin>> bins2 = h2.binEntryIterator();
		if ( h1.compareBins(h2) ) {
			Histogram combined = new Histogram( h1.get_numbins(), h1.get_min(), h1.get_max(), true);
			Bin b1, b2;
			while ( bins1.hasNext() && bins2.hasNext() ) {
				b1 = bins1.next().getValue();
				b2 = bins2.next().getValue();
				combined.add(b1.avg(), b1.freq());
				combined.add(b2.avg(), b2.freq());
			}
			// TODO need to include min and max overflows to new histogram
			return combined;
		} else {
			return null;
		}
	}

	
	public double get_max() {
		return _max;
	}

	private void set_max(double max) {
		this._max = max;
	}

	public double get_min() {
		return _min;
	}

	private void set_min(double min) {
		this._min = min;
	}

	public void set_histname(String name) {
		_histName = name;
	}
	
	public String get_histname() {
		return _histName;
	}
	
	private int get_count() {
		return _count;
	}
	
	public int get_numbins() {
		return _numBins;
	}
	
	public boolean compareBins(Histogram h) {
		boolean output = false;
		if ( get_numbins()==h.get_numbins() && get_max()==h.get_max() && get_min()==h.get_min() && h.get_overflow() == get_overflow()) {
			output = true;
		}
		return output;
	}

	public boolean get_overflow() {
		return _overflow;
	}

	public String maxCountAtName() {
		return _maxBinCountAt;
	}
	
	private void set_maxC(int maxC) {
		_maxBinCount = maxC;
	}
	
	public int maxBinCount() {
		return _maxBinCount;
	}

}
