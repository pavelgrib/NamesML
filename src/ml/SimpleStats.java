package ml;

/**
 * Stats class to track average and variance of added values
 * @author paul.
 */
public class SimpleStats {
	
	private int _count;
	private double _xa;
	private double _x2a;
	
	public SimpleStats() {
		this._count = 0;
		this._xa = 0;
		this._x2a = 0;
	}

	public void add(double x) {
		this._count++;
		this._xa += (x - get_avg())  / this._count;
		this._x2a += (x*x - this._x2a) / get_count();
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
}
