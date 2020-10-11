
/**
 * Brute-force selection between each other.
 * 
 * @author Todor Balabanov
 */
final class LocalSearch implements Selection {
	@Override
	public double[] best(double[][] population, Function function) {
		double[] result = {};

		boolean stop = false;
		double optimum = Double.MAX_VALUE;
		while (stop == false) {
			stop = true;

			/* Crossover and mutation with each other. */
			for (double first[] : population) {
				for (double second[] : population) {
					double child[] = Main.crossover(first, second);
					Main.mutate(child);

					double value = function.calculate(child);

					/* Keep track of the best solution found. */
					if (value < optimum) {
						result = child;
						optimum = value;
						stop = false;
					}

					/* Count the number of evaluated individuals. */
					Main.individuals++;
				}
			}
		}

		return result;
	}

	@Override
	public String title() {
		return "Local Search";
	}
}