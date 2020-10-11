
/**
 * Michalewicz function.
 *
 * https://www.sfu.ca/~ssurjano/ackley.html
 * 
 * @author Todor Balabanov
 */
final class Michalewicz implements Function {
	private double m = 10;

	@Override
	public double calculate(double[] x) {
		double i = 1D;
		double sum = 0D;
		for (double xi : x) {
			sum += Math.sin(xi)
					* Math.pow(Math.sin(i * xi * xi / Math.PI), 2 * m);
			i++;
		}

		return -sum;
	}

	@Override
	public double minimum() {
		return 0;
	}

	@Override
	public double maximum() {
		return Math.PI;
	}

	@Override
	public String title() {
		return "Michalewicz";
	}
}