public class Run {

	public static void main(String[] args) {
		//
		// double best = 1000;
		//
		// for (int i = 0; i < 1000; i++) {
		// AlbumPhoto a = new AlbumPhoto(55, true);
		// double t = a.hillClimberFirstImprovement(10000);
		//
		// if (t < best) {
		// best = t;
		// }
		// }
		// System.out.println(best);

		AlbumPhoto b = new AlbumPhoto(55, true);
		int nb_parents = 55;
		int nb_geniteur = 20;
		int nb_generations = 50;
		b.algoEvolutionnaire(nb_parents, nb_geniteur, nb_generations);
	}
}
