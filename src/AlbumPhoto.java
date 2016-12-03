import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AlbumPhoto extends LocalSearchAlgorithm {

	private final String	photoFileName	= "/home/geoffrey/Documents/Master/RO/prj1-ro/data/info-photo.json";
	private final String	albumFileName	= "/home/geoffrey/Documents/Master/RO/prj1-ro/data/info-album.json";
	private double[][]		photoDist;
	private double[][]		albumInvDist;

	public AlbumPhoto(int taille, boolean debug) {
		super(debug);
		computeDistances(photoFileName, albumFileName);
		this.nb_photos = taille;
		solution = generateShuffleSolution(taille);
	}

	public AlbumPhoto(int[] solution, boolean debug) {
		super(debug);
		computeDistances(photoFileName, albumFileName);
		this.solution = new int[solution.length];
		for (int i = 0; i < solution.length; i++)
			this.solution[i] = solution[i];
	}

	@Override
	protected int[] generateShuffleSolution(int taille) {
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

	@Override
	public int[] getRandomNeighbor() {
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

	@Override
	public String toString() {
		String res = "";
		for (int i = 0; i < solution.length; i++)
			res += solution[i] + " ";

		return res;
	}

	/**
	 * @author S.Verel
	 * @category Un exemple de fonction objectif (à minimiser): distance entre
	 *           les photos pondérées par l'inverse des distances spatiales sur
	 *           l'album Modélisaiton comme un problème d'assignement
	 *           quadratique (QAP)
	 *
	 *           Dans cette fonction objectif, pas de prise en compte d'un effet
	 *           de page (harmonie/cohérence de la page) par le choix de
	 *           distance, pas d'intéraction entre les photos sur des
	 *           différentes pages
	 */
	@Override
	public double eval(int[] solution) {
		double sum = 0;

		for (int i = 0; i < albumInvDist.length; i++) {
			for (int j = i + 1; j < albumInvDist.length; j++) {
				sum += photoDist[solution[i]][solution[j]] * albumInvDist[i][j];
			}
		}

		return sum;
	}

	/**
	 * @author S.Verel
	 * @category Compute the matrice of distance between solutions and of
	 *           inverse distance between positions
	 */
	private void computeDistances(String photoFileName, String albumFileName) {
		computePhotoDistances(photoFileName);
		computeAlbumDistances(albumFileName);
	}

	/**
	 * @author S.Verel
	 */
	private void computeAlbumDistances(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(reader);

			JSONObject album = (JSONObject) obj;

			JSONArray pageSize = (JSONArray) album.get("pagesize");

			int size = (int) (long) pageSize.get(0);
			int nbPhoto = 0;
			for (int i = 0; i < pageSize.size(); i++)
				nbPhoto += (int) (long) pageSize.get(i);

			albumInvDist = new double[nbPhoto][nbPhoto];

			// compute the distance
			for (int i = 0; i < nbPhoto; i++)
				for (int j = 0; j < nbPhoto; j++)
					albumInvDist[i][j] = inverseDistance(size, i, j);

		} catch (ParseException pe) {
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @author S.Verel
	 */
	private void computePhotoDistances(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);

			JSONParser parser = new JSONParser();

			Object obj = parser.parse(reader);

			JSONArray array = (JSONArray) obj;

			photoDist = new double[array.size()][array.size()];

			for (int i = 0; i < array.size(); i++) {
				JSONObject image = (JSONObject) array.get(i);
				JSONArray d = (JSONArray) image.get("ahashdist");
				for (int j = 0; j < d.size(); j++) {
					photoDist[i][j] = (double) d.get(j);
				}
			}

		} catch (ParseException pe) {
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @author S.Verel
	 */
	private static double inverseDistance(int size, int i, int j) {
		// number of pages
		int pagei = i / size;
		int pagej = j / size;

		if (pagei != pagej)
			// not on the same page: distance is infinite. Another choice is
			// possible of course!
			return 0;
		else {
			// positions in the page
			int posi = i % size;
			int posj = j % size;

			// coordinate on the page
			int xi = posi % 2;
			int yi = posi / 2;
			int xj = posj % 2;
			int yj = posj / 2;

			// Manhatthan distance
			return ((double) 1) / (double) (Math.abs(xi - xj) + Math.abs(yi - yj));
		}
	}
}
