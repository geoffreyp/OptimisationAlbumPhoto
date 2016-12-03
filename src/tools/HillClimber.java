package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HillClimber {

	public static int[] getRandomNeighbor(int[] solution) {
		Random r = new Random();
		int indice1 = r.nextInt(solution.length);
		int indice2 = r.nextInt(solution.length);

		int[] neighbor = new int[solution.length];
		for (int i = 0; i < solution.length; i++)
			neighbor[i] = solution[i];

		neighbor[indice1] = solution[indice2];
		neighbor[indice2] = solution[indice1];

		return neighbor;
	}

	public static int[] generateShuffleSolution(int taille) {
		int[] solution = new int[taille];
		for (int i = 0; i < taille; i++)
			solution[i] = i;

		List<Integer> s = new ArrayList<>();
		for (int i = 0; i < solution.length; i++)
			s.add(i, solution[i]);

		Collections.shuffle(s);
		for (int i = 0; i < solution.length; i++)
			solution[i] = s.get(i);

		return solution;
	}

	public static void showSolution(int[] solution) {
		String res = "";
		for (int i = 0; i < solution.length; i++)
			res += solution[i] + " ";

		System.out.println(res);
	}
}
