import java.util.ArrayList;
import java.util.List;
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
		public Double calculate(List<Double> input);

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
		public Double calculate(List<Double> x) {
			double sum = 0D;
			for (Double xi : x) {
				sum += (xi * xi);
			}

			int i = 1;
			double multiplication = 1D;
			for (Double xi : x) {
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
		public Double calculate(List<Double> x) {
			final double A = 10D;
			final double n = x.size();

			double sum = 0D;
			for (Double xi : x) {
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
	private static final int INPUT_SIZE = 100;

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
	private static List<Double> crossover(List<Double> first,
			List<Double> second) {
		List<Double> child = new ArrayList<Double>();

		for (int i = 0; i < INPUT_SIZE; i++) {
			if (PRNG.nextBoolean()) {
				child.add(first.get(i));
			} else {
				child.add(second.get(i));
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
	private static void mutate(List<Double> child) {
		for (int i = 0; i < INPUT_SIZE; i++) {
			if (PRNG.nextDouble() >= MUTATION_RATE) {
				continue;
			}

			child.set(i, child.get(i) + PRNG.nextDouble() - 0.5D);
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
	 * @param function
	 *            Benchmark function object reference.
	 * @return Best found solution.
	 */
	private static List<Double> solution(int depth, int size,
			Function function) {
		List<Double> result = new ArrayList<Double>();

		if (depth > 0) {
			List<List<Double>> population = new ArrayList<List<Double>>();
			for (int j = 0; j < size; j++) {
				population.add(solution(depth - 1, size, function));
			}

			/* Crossover and mutation with each other. */
			double optimum = Double.MAX_VALUE;
			for (List<Double> first : population) {
				for (List<Double> second : population) {
					List<Double> child = crossover(first, second);
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
		} else if (depth == 0) {
			/* First generation is selected randomly. */
			for (int i = 0; i < INPUT_SIZE; i++) {
				result.add(function.minimum() + PRNG.nextDouble()
						* (function.maximum() - function.minimum()));
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
		Function functions[] = {new Rastrigin(), new Griewank()};

		for (Function function : functions) {
			for (int depth = MIN_RECURSION_DEPTH; depth <= MAX_RECURSION_DEPTH; depth++) {
				for (int size = MIN_POPULATION_SIZE; size <= MAX_POPULATION_SIZE; size++) {
					individuals = 0L;
					long start = System.currentTimeMillis();
					List<Double> input = solution(depth, size, function);
					long stop = System.currentTimeMillis();

					double output = function.calculate(input);

					System.out.print("Fuction Name:");
					System.out.print("\t");
					System.out.println(function.title());
					System.out.print("Recursion Depth:");
					System.out.print("\t");
					System.out.println(depth);
					System.out.print("Population Size:");
					System.out.print("\t");
					System.out.println(size);
					System.out.print("Evaluated Individuals:");
					System.out.print("\t");
					System.out.println(individuals);
					System.out.print("Input Vector:");
					System.out.print("\t");
					System.out.println(input.toString().replace(',', '\t')
							.replace("[", "").replace("]", ""));
					System.out.print("Output Value:");
					System.out.print("\t");
					System.out.println(output);
					System.out.print("Time [ms]:");
					System.out.print("\t");
					System.out.println(stop - start);
					System.out.println();
				}
			}
		}
	}

}
