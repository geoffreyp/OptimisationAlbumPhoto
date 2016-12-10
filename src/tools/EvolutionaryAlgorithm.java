package tools;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;

public class EvolutionaryAlgorithm {
	/**
	 * Sélection des géniteurs en effectuant des tournois
	 */
	public static int[][] getGeniteurs(double[] evalParents, int nb_geniteur, int[][] parents, int nb_photos) {
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

	public static void mutation(int[][] enfants) {
		// mutations (memetic EA) : variations mutation (on a n = (54*55)/2
		// mutations possible avec 55 photos)
		// la proba de faire une mutation est donc de 1/n , on aura au moins une
		// chance de faire une mutation, voir plus
		Random r = new Random();

		for (int i = 0; i < enfants.length; i++) {
			int indice1 = r.nextInt(enfants[i].length);
			int indice2 = r.nextInt(enfants[i].length);
			int temp = enfants[i][indice1];

			enfants[i][indice1] = enfants[i][indice2];
			enfants[i][indice2] = temp;
		}
	}

	/**
	 * generation des enfants en effectuant une mutation + un Hill Climber
	 */
	public static int[][] getEnfantsAvecVariation(int[][] geniteurs, int nb_photos, Function<int[], int[]> hc) { // TODO
																													// WIP
		int[][] enfants = new int[geniteurs.length][nb_photos];
		for (int i = 0; i < geniteurs.length; i++) {
			for (int j = 0; j < geniteurs[i].length; j++) {
				enfants[i][j] = geniteurs[i][j];
			}
		}

		EvolutionaryAlgorithm.mutation(enfants);

		for (int i = 0; i < enfants.length; i++) {
			enfants[i] = hc.apply(enfants[i]);
		}

		return enfants;
	}

	/**
	 * Sélection des survivants : les mu meilleurs parents+enfants
	 */
	public static int[][] getSurvivants(int[][] parents, int[][] enfants, int nb_photos, Function<int[], Double> eval) {
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
				if (eval.apply(o1) < eval.apply(o2))
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
