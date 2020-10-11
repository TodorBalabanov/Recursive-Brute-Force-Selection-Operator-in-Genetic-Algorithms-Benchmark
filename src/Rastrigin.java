
/**
 * Rastrigin function.
 *
 * https://en.wikipedia.org/wiki/Rastrigin_function
 * 
 * @author Todor Balabanov
 */
class Rastrigin implements Function {
	@Override
	public double calculate(double x[]) {
		final double A = 10D;
		final double n = x.length;

		double sum = 0D;
		for (double xi : x) {
			sum += (xi * xi - A * Math.cos(2D * Math.PI * xi));
		}

		return A * n + sum;
	}

	@Override
	public double minimum() {
		return -5.12D;
	}

	@Override
	public double maximum() {
		return +5.12D;
	}

	@Override
	public String title() {
		return "Rastrigin";
	}
}