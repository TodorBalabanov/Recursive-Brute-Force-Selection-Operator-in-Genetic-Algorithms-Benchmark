import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	/**
	 * Pseudo-random number generator.
	 */
	private static final Random PRNG = new Random();

	/**
	 * Size of the population for a single generation.
	 */
	private static final int POPULATION_SIZE = 7;

	/**
	 * Mutation rate.
	 */
	private static final double MUTATION_RATE = 0.01;

	/**
	 * Number of generations to be created as depth of the recursion.
	 */
	private static final int RECURSION_DEPTH = 8;

	/**
	 * Size of the input vector (search space dimensions).
	 */
	private static final int INPUT_SIZE = 100;

	/**
	 * Input minimum value.
	 */
	private static final double INPUT_MINIMUM = -5.12D;

	/**
	 * Input maximum value;
	 */
	private static final double INPUT_MAXIMUM = +5.12D;

	/**
	 * Griewank function.
	 *
	 * https://en.wikipedia.org/wiki/Griewank_function
	 *
	 * @param x
	 *            Input vector (-600<=xi<=+600).
	 * 
	 * @return Result of the function.
	 */
	private static Double griewank(List<Double> x) {
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

	/**
	 * Rastrigin function.
	 *
	 * https://en.wikipedia.org/wiki/Rastrigin_function
	 *
	 * @param x
	 *            Input vector (-5.12<=xi<=+5.12).
	 * 
	 * @return Result of the function.
	 */
	private static Double rastrigin(List<Double> x) {
		final double A = 10D;
		final double n = x.size();

		double sum = 0D;
		for (Double xi : x) {
			sum += (xi * xi - A * Math.cos(2D * Math.PI * xi));
		}

		return A * n + sum;
	}

	private static List<Double> crossover(List<Double> first,
			List<Double> second) {
		List<Double> child = new ArrayList<Double>();

		for (int i = 0; i < INPUT_SIZE; i++) {
			if (PRNG.nextDouble() < 0.5D) {
				child.add(first.get(i));
			} else {
				child.add(second.get(i));
			}
		}

		return child;
	}

	private static void mutate(List<Double> child) {
		for (int i = 0; i < INPUT_SIZE; i++) {
			if (PRNG.nextDouble() >= MUTATION_RATE) {
				continue;
			}

			child.set(i, child.get(i) + PRNG.nextDouble() - 0.5D);
		}
	}

	private static List<Double> solution(int depth) {
		List<Double> result = new ArrayList<Double>();

		if (depth > 0) {
			List<List<Double>> population = new ArrayList<List<Double>>();
			for (int j = 0; j < POPULATION_SIZE; j++) {
				population.add(solution(depth - 1));
			}

			/* Crossover and mutation with each other. */
			double optimum = Double.MAX_VALUE;
			for (List<Double> first : population) {
				for (List<Double> second : population) {
					List<Double> child = crossover(first, second);
					mutate(child);

					/* Keep track of the best solution found. */
					double value = rastrigin(child);
					if (value < optimum) {
						result = child;
						optimum = value;
					}
				}
			}
		} else if (depth == 0) {
			/* First generation is selected randomly. */
			for (int i = 0; i < INPUT_SIZE; i++) {
				result.add(INPUT_MINIMUM
						+ PRNG.nextDouble() * (INPUT_MAXIMUM - INPUT_MINIMUM));
			}
		}

		return result;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		List<Double> input = solution(RECURSION_DEPTH);
		long stop = System.currentTimeMillis();

		double output = rastrigin(input);

		System.out.println(output);
		System.out.println(input);
		System.out.println(stop - start);
	}

}
