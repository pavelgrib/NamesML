package ml;

/**
 * Stats class to track average and variance of added values
 * @author paul.
 */
public class SimpleStats {
	
	private int _count;
	private double _xa;
	private double _x2a;
	private double _min;
	private double _max;
	private int _minAtCount;
	private int _maxAtCount;
	
	public SimpleStats() {
		this._count = 0;
		this._xa = 0;
		this._x2a = 0;
		this._min = Double.POSITIVE_INFINITY;
		this._max = Double.NEGATIVE_INFINITY;
		this._maxAtCount = 0;
		this._minAtCount = 0;
	}

	public void reset() {
		this._count = 0;
		this._xa = 0;
		this._x2a = 0;
		this._min = Double.POSITIVE_INFINITY;
		this._max = Double.NEGATIVE_INFINITY;
		this._maxAtCount = 0;
		this._minAtCount = 0;
	}

	public void add(double x) {
		this._count++;
		this._xa += (x - get_avg())  / this._count;
		this._x2a += (x*x - this._x2a) / get_count();
		if ( x < this._min ) {
			this._min = x;
			this._minAtCount = this._count;
		} else if ( x > this._max ) {
			this._max = x;
			this._maxAtCount = this._count;
		}
	}
	
	public void add(double[] array) {
		for ( double x: array ) {
			this.add(x);
		}
	}
	
	public boolean sub(double x) {
		if ( get_count() < 2 ) {
			return false;
		} else {
			this._count--;
			this._xa -= (x - get_avg()) / get_count();
			this._x2a -= (x*x - this._x2a) / get_count();
			return true;
		}
	}
	
	public boolean sub(double[] array) {
		if ( get_count() - array.length < 1 ) {
			return false;
		} else {
			for ( double x: array ) {
				this.sub(x);
			}
			return true;
		}
	}
	
	public double variance() {
		return _x2a - _xa*_xa;
	}
	
	public double standardDeviation() {
		return Math.sqrt(variance());
	}

	public double normalizedValue(double x) {
		return (x - _xa) / variance();
	}
	
	public double get_avg() {
		return this._xa;
	}

	public int get_count() {
		return this._count;
	}

	public int get_minAtCount() {
		return _minAtCount;
	}

	public void set_minAtCount(int _minAtCount) {
		this._minAtCount = _minAtCount;
	}

	public int get_maxAtCount() {
		return _maxAtCount;
	}

	public void set_maxAtCount(int _maxAtCount) {
		this._maxAtCount = _maxAtCount;
	}
}
