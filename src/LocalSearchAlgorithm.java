import java.util.Random;

public abstract class LocalSearchAlgorithm {
	protected int[]	solution;
	protected int	nb_photos;

	public abstract double eval(int[] solution);

	public abstract int[] getRandomNeighbor();

	protected abstract int[] generateShuffleSolution(int taille);

	/**
	 * @author Geoffrey Pruvost
	 * @param nbEvalMax
	 *            : number of maximum evaluations
	 * @return double value : the best evaluation
	 */
	public double hillClimberFirstImprovement(int nbEvalMax) {
		double eval = eval(solution);
		double best_eval_neighbor = 100;
		int nbEval = 0;

		while (nbEval < nbEvalMax) {
			nbEval++;
			boolean betterSolutionFounded = false;
			int[] neighbor = new int[solution.length];

			do {
				nbEval++;
				neighbor = getRandomNeighbor();
				double eval_neighbor = eval(neighbor);

				if (eval_neighbor < best_eval_neighbor) {
					best_eval_neighbor = eval_neighbor;
					betterSolutionFounded = true;
				}
			} while (!betterSolutionFounded && nbEval < nbEvalMax);

			if (eval > best_eval_neighbor) {
				eval = best_eval_neighbor;
				solution = neighbor;
			}
		}

		return eval;
	}

}
