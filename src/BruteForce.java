
/**
 * Brute-force selection between each other.
 * 
 * @author Todor Balabanov
 */
final class BruteForce implements Selection {
	@Override
	public double[] best(double[][] population, Function function) {
		double[] result = {};

		/* Crossover and mutation with each other. */
		double optimum = Double.MAX_VALUE;
		for (double first[] : population) {
			for (double second[] : population) {
				double child[] = Main.crossover(first, second);
				Main.mutate(child);

				double value = function.calculate(child);

				/* Keep track of the best solution found. */
				if (value < optimum) {
					result = child;
					optimum = value;
				}

				/* Count the number of evaluated individuals. */
				Main.individuals++;
			}
		}

		return result;
	}

	@Override
	public String title() {
		return "Brute Force";
	}
}