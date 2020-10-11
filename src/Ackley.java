
/**
 * Ackley function.
 *
 * https://www.sfu.ca/~ssurjano/ackley.html
 * 
 * @author Todor Balabanov
 */
final class Ackley implements Function {
	private double a = 20;
	private double b = 0.2;
	private double c = 2 * Math.PI;

	@Override
	public double calculate(double[] x) {
		double sum1 = 0D;
		double sum2 = 0D;
		for (double xi : x) {
			sum1 += xi * xi;
			sum2 += Math.cos(c * xi);
		}

		return -a * Math.exp(-b * Math.sqrt(sum1 / x.length))
				- Math.exp(sum2 / x.length) + a + Math.exp(1D);
	}

	@Override
	public double minimum() {
		return -32.768;
	}

	@Override
	public double maximum() {
		return +32.768;
	}

	@Override
	public String title() {
		return "Ackley";
	}
}