package album;

import algorithm.LocalSearchAlgorithm;
import tools.Album;
import tools.HillClimber;

public class AlbumAhashSmooth extends LocalSearchAlgorithm {

	public AlbumAhashSmooth(int taille, boolean debug) {
		super(debug);
		this.nb_photos = taille;
		solution = HillClimber.generateShuffleSolution(taille);
	}

	/**
	 * @author Geoffrey Pruvost
	 */
	@Override
	public double eval(int[] solution) {
		double sum = 0;
		for (int i = 0; i < solution.length-1; i++) {
			sum += Album.getAverageHashDistance(solution[i], solution[i+1]);
		}
		return sum;

	}

}
