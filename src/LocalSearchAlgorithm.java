import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public abstract class LocalSearchAlgorithm {
	protected int[]	solution;
	protected int	nb_photos;

	public abstract double eval(int[] solution);

	public abstract int[] getRandomNeighbor();

	protected abstract int[] generateShuffleSolution(int taille);

	/**
	 * @author Geoffrey Pruvost
	 * @param nbEvalMax:
	 *            nb of maximum evaluations
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

	public void algoEvolutionnaire(int nb_parents, int nb_geniteur, int nb_generations) {
		int[][] parents = new int[nb_parents][nb_photos];
		double[] evaluationParents = new double[nb_parents];
		String strEvalParents = "";

		for (int i = 0; i < nb_parents; i++) {
			parents[i] = generateShuffleSolution(nb_photos);
			evaluationParents[i] = eval(parents[i]);
			strEvalParents += "[" + i + "]=" + evaluationParents[i] + " ";
		}

		int generation = 0;
		while (generation < nb_generations) {
			generation++;
			System.out.println("\nGénération.... :" + generation);
			System.out.println("Parents....... :" + strEvalParents);

			int[][] geniteurs = getGeniteurs(evaluationParents, nb_geniteur, parents);

			int[][] enfants = variations(geniteurs);

			// evaluation des enfants TODO utilisé pour débugguer, inutile sinon
			// : a supprimer
			double[] evaluationEnfants = new double[enfants.length];
			String strEvalEnfants = "";
			for (int i = 0; i < enfants.length; i++) {
				evaluationEnfants[i] = eval(enfants[i]);
				strEvalEnfants += "[" + i + "]=" + evaluationEnfants[i] + " ";
			}
			System.out.println("Enfants....... :" + strEvalEnfants);

			parents = getSurvivants(parents, enfants);

			strEvalParents = "";
			for (int i = 0; i < parents.length; i++) {
				evaluationParents[i] = eval(parents[i]);
				strEvalParents += "[" + i + "]=" + evaluationParents[i] + " ";
			}
		}
	}

	/**
	 * Sélection des géniteurs en effectuant des tournois
	 */
	private int[][] getGeniteurs(double[] evalParents, int nb_geniteur, int[][] parents) {
		int[][] geniteurs = new int[nb_geniteur][nb_photos];

		for (int i = 0; i < nb_geniteur; i++) {
			int parent1 = new Random().nextInt(evalParents.length);
			int parent2 = new Random().nextInt(evalParents.length);

			double evalP1 = evalParents[parent1];
			double evalP2 = evalParents[parent2];

			// Tournoi
			if (evalP1 < evalP2) {
				for (int j = 0; j < parents[parent1].length; j++) {
					geniteurs[i][j] = parents[parent1][j];
				}
			}
			else {
				for (int j = 0; j < parents[parent2].length; j++) {
					geniteurs[i][j] = parents[parent2][j];
				}
			}
		}

		return geniteurs;
	}

	private int[][] variations(int[][] geniteurs) { // TODO WIP
		int[][] enfants = geniteurs;
		// mutations (memetic EA) : variations mutation (on a n = (54*55)/2
		// mutations
		// possible avec 55 photos) la proba de faire une mutation est donc de
		// 1/n , on aura au moins une chance de faire une mutation, voir plus
		// hc

		return enfants;
	}

	/**
	 * Sélection des survivants : les mu meilleurs parents+enfants
	 */
	private int[][] getSurvivants(int[][] parents, int[][] enfants) {
		int[][] parentsEtEnfants = new int[parents.length + enfants.length][nb_photos];

		for (int i = 0; i < parents.length; i++) {
			for (int j = 0; j < parents[i].length; j++) {
				parentsEtEnfants[i][j] = parents[i][j];
			}
		}

		for (int i = parents.length; i < parentsEtEnfants.length; i++) {
			for (int j = 0; j < enfants[i - parents.length].length; j++) {
				parentsEtEnfants[i][j] = enfants[i - parents.length][j];
			}
		}

		Arrays.sort(parentsEtEnfants, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				if (eval(o1) < eval(o2))
					return -1;
				else
					return 1;
			}
		});

		int[][] newParents = new int[parents.length][nb_photos];
		for (int i = 0; i < newParents.length; i++) {
			for (int j = 0; j < newParents[i].length; j++) {
				newParents[i][j] = parentsEtEnfants[i][j];
			}
		}

		return newParents;
	}
}
