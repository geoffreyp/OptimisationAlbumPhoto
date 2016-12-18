package algorithm;

import java.util.Date;
import java.util.function.Function;

import tools.Album;
import tools.IteratedLocalSearch;
import tools.EvolutionaryAlgorithm;
import tools.HillClimber;

public abstract class LocalSearchAlgorithm {
	protected int[]				solution;
	protected int				nb_photos;
	protected static boolean	isDebugEnabled;

	public LocalSearchAlgorithm(boolean b) {
		isDebugEnabled = b;
	}

	public abstract double eval(int[] solution);

	/**
	 * @author Geoffrey Pruvost
	 * @return the best solution
	 */
	public int[] hillClimberFirstImprovement(int nbEvalMax) {
		double eval = eval(solution);
		double best_eval_neighbor = 100;
		int nbEval = 0;

		while (nbEval < nbEvalMax) {
			nbEval++;
			boolean betterSolutionFounded = false;
			int[] neighbor = new int[solution.length];

			do {
				nbEval++;
				neighbor = HillClimber.getRandomNeighbor(solution);
				double eval_neighbor = eval(neighbor);

				if (eval_neighbor < best_eval_neighbor) {
					best_eval_neighbor = eval_neighbor;
					betterSolutionFounded = true;
					if (isDebugEnabled) {
						Album.writeCsvFile("hc-" + new Date().toString().replaceAll(" ", "-") + ".csv", nbEval, best_eval_neighbor);
					}
				}
			} while (!betterSolutionFounded && nbEval < nbEvalMax);

			if (eval > best_eval_neighbor) {
				eval = best_eval_neighbor;
				solution = neighbor;
			}
		}

		return solution;
	}

	/**
	 * @author Geoffrey Pruvost
	 */
	public void algoEvolutionnaire(int nb_parents, int nb_geniteur, int nb_generations) {
		int[][] parents = new int[nb_parents][nb_photos];
		double[] evaluationParents = new double[nb_parents];
		String strEvalParents = "";

		for (int i = 0; i < nb_parents; i++) {
			parents[i] = HillClimber.generateShuffleSolution(nb_photos);
			evaluationParents[i] = eval(parents[i]);
			strEvalParents += "[" + i + "]=" + evaluationParents[i] + " ";
		}

		int generation = 0;
		while (generation < nb_generations) {
			generation++;

			if (isDebugEnabled) {
				System.out.println("\nGénération.... :" + generation);
				System.out.println("Parents....... :" + strEvalParents);
			}

			int[][] geniteurs = EvolutionaryAlgorithm.getGeniteurs(evaluationParents, nb_geniteur, parents, nb_photos);

			Function<int[], int[]> hc = (enfant) -> {
				setSolution(enfant);
				return hillClimberFirstImprovement(10000);
			};

			int[][] enfants = EvolutionaryAlgorithm.getEnfantsAvecVariation(geniteurs, nb_photos, hc);

			if (isDebugEnabled) {
				double[] evaluationEnfants = new double[enfants.length];
				String strEvalEnfants = "";
				for (int i = 0; i < enfants.length; i++) {
					evaluationEnfants[i] = eval(enfants[i]);
					strEvalEnfants += "[" + i + "]=" + evaluationEnfants[i] + " ";
				}
				System.out.println("Enfants....... :" + strEvalEnfants);
			}
			Function<int[], Double> eval = (i) -> {
				return eval(i);
			};

			parents = EvolutionaryAlgorithm.getSurvivants(parents, enfants, nb_photos, eval);

			strEvalParents = "";
			for (int i = 0; i < parents.length; i++) {
				evaluationParents[i] = eval(parents[i]);
				strEvalParents += "[" + i + "]=" + evaluationParents[i] + " ";
			}
		}

		double best_eval = 100;
		int indice_best_eval = -1;
		for (int i = 0; i < evaluationParents.length; i++) {
			if (best_eval > evaluationParents[i]) {
				best_eval = evaluationParents[i];
				indice_best_eval = i;
			}
		}

		System.out.println("\nThe best Eval is " + best_eval + " at " + indice_best_eval + " & verif = " + eval(parents[indice_best_eval]));
		Album.writeSolution(parents[indice_best_eval]);
	}

	public int[] IteratedLocalSearch(int nbEvalMax) {
		hillClimberFirstImprovement(10000);
		int[] best_solution = IteratedLocalSearch.copyArray(solution);

		/* During the loop, in contrary to others algos, solution is not the
		 * best solution because I use HC on the perturbed solution */
		for (int i = 0; i < nbEvalMax; i++) {
			setSolution(IteratedLocalSearch.perturbation(best_solution));

			if (isDebugEnabled) {
				Album.writeCsvFile("ils.csv", i, eval(getSolution()));
			}

			hillClimberFirstImprovement(10000);
			// System.out.println(eval(getSolution()) + " < " +

			if (eval(best_solution) > eval(getSolution())) {
				best_solution = getSolution();
			}

		}

		// solution became the best solution at the end
		setSolution(best_solution);
		System.out.println("Best eval : " + eval(getSolution()));

		return getSolution();
	}

	/* Getters / Setters */
	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] sol) {
		this.solution = new int[sol.length];
		for (int i = 0; i < this.solution.length; i++) {
			this.solution[i] = sol[i];
		}
	}

}
