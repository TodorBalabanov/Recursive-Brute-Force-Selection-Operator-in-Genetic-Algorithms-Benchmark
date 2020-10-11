
/**
 * Griewank function.
 *
 * https://en.wikipedia.org/wiki/Griewank_function
 * 
 * @author Todor Balabanov
 */
final class Griewank implements Function {
	@Override
	public double calculate(double x[]) {
		double sum = 0D;
		for (double xi : x) {
			sum += (xi * xi);
		}

		int i = 1;
		double multiplication = 1D;
		for (double xi : x) {
			multiplication *= Math.cos(xi / Math.sqrt(i));
			i++;
		}

		return 1D + sum / 4000D - multiplication;
	}

	@Override
	public double minimum() {
		return -600D;
	}

	@Override
	public double maximum() {
		return +600D;
	}

	@Override
	public String title() {
		return "Griewank";
	}
}