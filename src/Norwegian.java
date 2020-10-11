
/**
 * Norwegian function.
 *
 * http://ssudl.solent.ac.uk/3366/1/Seminar_BAN_30_03_2016.ppt
 * 
 * @author Todor Balabanov
 */
final class Norwegian implements Function {
	@Override
	public double calculate(double[] x) {
		double mul = 1D;
		for (double xi : x) {
			mul *= Math.cos(Math.PI * xi * xi * xi) * (99D + xi) / 100D;
		}

		return -mul;
	}

	@Override
	public double minimum() {
		return -1.1;
	}

	@Override
	public double maximum() {
		return +1.1;
	}

	@Override
	public String title() {
		return "Norwegian";
	}
}