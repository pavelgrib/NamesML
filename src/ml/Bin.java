package ml;

/**
 * TODO Put here a description of what this class does.
 *
 * @author paul.
 */

public class Bin {

	private SimpleStats _ss;
	private double _min;
	private double _max;
	private String _binName;
	
	public Bin(double min, double max) {
		_ss = new SimpleStats();
		set_min(min);
		set_max(max);
	}
	
	public void add(double x) {
		_ss.add(x);
	}

	public void add(double[] values) {
		for (double i : values) {
			_ss.add(i);
		}
	}
	
	public int freq() {
		return _ss.get_count();
	}
	
	public double avg() {
		return _ss.get_avg();
	}
	
	public void set_name(String name) {
		_binName = name;
	}
	
	public String get_name() {
		return _binName;
	}
	
	public double get_min() {
		return _min;
	}

	public void set_min(double min) {
		this._min = min;
	}

	public double get_max() {
		return _max;
	}

	public void set_max(double max) {
		this._max = max;
	}
}

