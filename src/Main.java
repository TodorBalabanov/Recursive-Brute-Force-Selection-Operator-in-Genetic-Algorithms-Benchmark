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
	 * Benchmark function interface.
	 * 
	 * @author Todor Balabanov
	 */
	private static interface Function {
		/**
		 * Calculation of the function result.
		 * 
		 * @param input
		 *            Input vector.
		 * @return Output value.
		 */
		public double calculate(double input[]);

		/**
		 * Minimal solution space value.
		 * 
		 * @return Minimum.
		 */
		public double minimum();

		/**
		 * Maximal solution space value.
		 * 
		 * @return Maximum.
		 */
		public double maximum();

		/**
		 * Function name.
		 * 
		 * @return Name.
		 */
		public String title();
	}

	/**
	 * Griewank function.
	 *
	 * https://en.wikipedia.org/wiki/Griewank_function
	 * 
	 * @author Todor Balabanov
	 */
	private static final class Griewank implements Function {
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

	/**
	 * Rastrigin function.
	 *
	 * https://en.wikipedia.org/wiki/Rastrigin_function
	 * 
	 * @author Todor Balabanov
	 */
	private static class Rastrigin implements Function {
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

	/**
	 * Schwefel function.
	 *
	 * https://www.sfu.ca/~ssurjano/schwef.html
	 * 
	 * @author Todor Balabanov
	 */
	private static final class Schwefel implements Function {
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

	/**
	 * Ackley function.
	 *
	 * https://www.sfu.ca/~ssurjano/ackley.html
	 * 
	 * @author Todor Balabanov
	 */
	private static final class Ackley implements Function {
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

	/**
	 * Michalewicz function.
	 *
	 * https://www.sfu.ca/~ssurjano/ackley.html
	 * 
	 * @author Todor Balabanov
	 */
	private static final class Michalewicz implements Function {
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

	/**
	 * Selection operator interface.
	 * 
	 * @author Todor Balabanov
	 */
	private static interface Selection {
		/**
		 * Best individual search function.
		 * 
		 * @param population
		 *            Population with individuals.
		 * @param function
		 *            Objective function.
		 * 
		 * @return Best found individual after selection procedure.
		 */
		double[] best(double[][] population, Function function);

		/**
		 * Selection name.
		 * 
		 * @return Name.
		 */
		public String title();
	}

	/**
	 * Brute-force selection between each other.
	 * 
	 * @author Todor Balabanov
	 */
	private static final class BruteForce implements Selection {
		@Override
		public double[] best(double[][] population, Function function) {
			double[] result = {};

			/* Crossover and mutation with each other. */
			double optimum = Double.MAX_VALUE;
			for (double first[] : population) {
				for (double second[] : population) {
					double child[] = crossover(first, second);
					mutate(child);

					double value = function.calculate(child);

					/* Keep track of the best solution found. */
					if (value < optimum) {
						result = child;
						optimum = value;
					}

					/* Count the number of evaluated individuals. */
					individuals++;
				}
			}

			return result;
		}

		@Override
		public String title() {
			return "Brute Force";
		}
	}

	/**
	 * Brute-force selection between each other.
	 * 
	 * @author Todor Balabanov
	 */
	private static final class LocalSearch implements Selection {
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
						double child[] = crossover(first, second);
						mutate(child);

						double value = function.calculate(child);

						/* Keep track of the best solution found. */
						if (value < optimum) {
							result = child;
							optimum = value;
							stop = false;
						}

						/* Count the number of evaluated individuals. */
						individuals++;
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
	private static final int MAX_RECURSION_DEPTH = 8;

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
	private static long individuals = 0L;

	/**
	 * Uniform crossover.
	 * 
	 * @param first
	 *            First parent.
	 * @param second
	 *            Second parent.
	 * @return Child produced after crossover.
	 */
	private static double[] crossover(double first[], double second[]) {
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
	private static void mutate(double child[]) {
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

		Function functions[] = {new Michalewicz(), new Ackley(), new Schwefel(),
				new Rastrigin(), new Griewank()};

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
