
/**
 * Benchmark function interface.
 * 
 * @author Todor Balabanov
 */
interface Function {
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