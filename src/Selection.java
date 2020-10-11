
/**
 * Selection operator interface.
 * 
 * @author Todor Balabanov
 */
interface Selection {
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