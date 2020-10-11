
/**
 * Schwefel function.
 *
 * https://www.sfu.ca/~ssurjano/schwef.html
 * 
 * @author Todor Balabanov
 */
final class Schwefel implements Function {
	@Override
	public double calculate(double[] x) {
		double sum = 0D;
		for (double xi : x) {
			sum += (xi * Math.sin(Math.sqrt(Math.abs(xi))));
		}

		return 418.9829D * x.length - sum;
	}

	@Override
	public double minimum() {
		return -500;
	}

	@Override
	public double maximum() {
		return +500;
	}

	@Override
	public String title() {
		return "Schwefel";
	}
}