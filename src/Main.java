import java.util.Arrays;
import java.util.Random;

/**
 * Benchmark of recursive brute-force selection operator used in genetic
 * algorithms.
 * 
 * @author Todor Balabanov
 */
public class Main {
	/**
	 * Pseudo-random number generator.
	 */
	private static final Random PRNG = new Random();

	/**
	 * Minimum number of generations to be created as depth of the recursion.
	 */
	private static final int MIN_RECURSION_DEPTH = 2;

	/**
	 * Maximum number of generations to be created as depth of the recursion.
	 */
	private static final int MAX_RECURSION_DEPTH = 7;

	/**
	 * Minimum size of the population for a single generation.
	 */
	private static final int MIN_POPULATION_SIZE = 2;

	/**
	 * Maximum size of the population for a single generation.
	 */
	private static final int MAX_POPULATION_SIZE = 11;

	/**
	 * Mutation rate.
	 */
	private static final double MUTATION_RATE = 0.01;

	/**
	 * Size of the input vector (search space dimensions).
	 */
	private static final int INPUT_SIZE = 10_000;

	/**
	 * Counting of generated individuals.
	 */
	static long individuals = 0L;

	/**
	 * Uniform crossover.
	 * 
	 * @param first
	 *            First parent.
	 * @param second
	 *            Second parent.
	 * @return Child produced after crossover.
	 */
	static double[] crossover(double first[], double second[]) {
		double[] child = new double[INPUT_SIZE];

		for (int i = 0; i < INPUT_SIZE; i++) {
			if (PRNG.nextBoolean()) {
				child[i] = first[i];
			} else {
				child[i] = second[i];
			}
		}

		return child;
	}

	/**
	 * Mutation of the child.
	 * 
	 * @param child
	 *            Individual to be mutated.
	 */
	static void mutate(double child[]) {
		for (int i = 0; i < INPUT_SIZE; i++) {
			if (PRNG.nextDouble() >= MUTATION_RATE) {
				continue;
			}

			child[i] += PRNG.nextDouble() - 0.5D;
		}
	}

	/**
	 * Generate solution of particular population size and with particular depth
	 * of recursive selection.
	 * 
	 * @param depth
	 *            Recursion depth.
	 * @param size
	 *            Population size size.
	 * @param selection
	 *            Selection operator object reference.
	 * @param function
	 *            Benchmark function object reference.
	 * @return Best found solution.
	 */
	private static double[] solution(int depth, int size, Selection selection,
			Function function) {
		double[] result = null;

		if (depth > 0) {
			double population[][] = new double[size][];
			for (int j = 0; j < size; j++) {
				population[j] = solution(depth - 1, size, selection, function);
			}

			result = selection.best(population, function);
		} else if (depth == 0) {
			/* First generation is selected randomly. */
			result = new double[INPUT_SIZE];
			for (int i = 0; i < INPUT_SIZE; i++) {
				result[i] = function.minimum() + PRNG.nextDouble()
						* (function.maximum() - function.minimum());
			}
		}

		return result;
	}

	/**
	 * Application single entry point.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		Selection selections[] = {new LocalSearch(), new BruteForce()};

		Function functions[] = {new Norwegian(), new Michalewicz(),
				new Ackley(), new Schwefel(), new Rastrigin(), new Griewank()};

		for (Selection selection : selections) {
			for (Function function : functions) {
				long times[][] = new long[MAX_RECURSION_DEPTH
						+ 1][MAX_POPULATION_SIZE + 1];
				long evaluations[][] = new long[MAX_RECURSION_DEPTH
						+ 1][MAX_POPULATION_SIZE + 1];
				double values[][] = new double[MAX_RECURSION_DEPTH
						+ 1][MAX_POPULATION_SIZE + 1];

				for (int depth = MIN_RECURSION_DEPTH; depth <= MAX_RECURSION_DEPTH; depth++) {
					for (int size = MIN_POPULATION_SIZE; size <= MAX_POPULATION_SIZE; size++) {
						individuals = 0L;
						long start = System.currentTimeMillis();
						double input[] = solution(depth, size, selection,
								function);
						long stop = System.currentTimeMillis();

						double output = function.calculate(input);

						times[depth][size] = stop - start;
						evaluations[depth][size] = individuals;
						values[depth][size] = output;

						System.out.print("Selection Name:");
						System.out.print("\t");
						System.out.println(selection.title());
						System.out.print("Function Name:");
						System.out.print("\t");
						System.out.println(function.title());
						System.out.print("Time [ms]:");
						System.out.println();
						System.out.println(Arrays.deepToString(times)
								.replace("], [", "\n").replace("[[", "")
								.replace("]]", "").replace(", ", "\t"));
						System.out.print("Values:");
						System.out.println();
						System.out.println(Arrays.deepToString(values)
								.replace("], [", "\n").replace("[[", "")
								.replace("]]", "").replace(", ", "\t"));
						System.out.print("Evaluations:");
						System.out.println();
						System.out.println(Arrays.deepToString(evaluations)
								.replace("], [", "\n").replace("[[", "")
								.replace("]]", "").replace(", ", "\t"));
						System.out.print("Best Found:");
						System.out.println();
						System.out
								.println(Arrays.toString(input).replace("[", "")
										.replace("]", "").replace(", ", "\t"));
						System.out.println();
					}
				}
			}
		}
	}

}
