package tools;

import java.util.Random;

public class IteratedLocalSearch {

	public static int[] copyArray(int[] a) {
		int[] tmp = new int[a.length];

		for (int i = 0; i < a.length; i++) {
			tmp[i] = a[i];
		}

		return tmp;
	}

	public static int[] perturbation(int[] a) {
		int[] perturb = copyArray(a);
		Random r = new Random();

		// perturb 25% of the solution
		for (int i = 0; i < perturb.length * 0.25; i++) {
			int indice1 = r.nextInt(a.length);
			int indice2 = r.nextInt(a.length);
			int tmp = perturb[indice1];

			perturb[indice1] = perturb[indice2];
			perturb[indice2] = tmp;
		}

		return perturb;
	}

}
