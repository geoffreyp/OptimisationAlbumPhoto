package album;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import algorithm.LocalSearchAlgorithm;
import tools.Album;
import tools.HillClimber;

public class AlbumGreyAverage extends LocalSearchAlgorithm {

	private double[][]	albumInvDist;
	private long[]		photoGreyAverage;

	public AlbumGreyAverage(int taille, boolean debug) {
		super(debug);
		computeDistances(Album.albumFileName);
		computeGreyAvg();
		this.nb_photos = taille;
		this.solution = HillClimber.generateShuffleSolution(taille);
	}

	/**
	 * @author Geoffrey Pruvost
	 * @Category
	 */
	@Override
	public double eval(int[] solution) {
		double sum = 0;

		for (int i = 0; i < albumInvDist.length; i++) {
			for (int j = i + 1; j < albumInvDist.length; j++) {
				sum += getGreyAvgDistance(solution[i], solution[j]) * albumInvDist[i][j];
			}
		}

		return sum;
	}

	public long getGreyAvgDistance(int p1, int p2) {
		return Math.abs(photoGreyAverage[p1] - photoGreyAverage[p2]);
	}

	public void computeGreyAvg() {

		try {
			FileReader reader = new FileReader(Album.photoFileName);
			JSONParser parser = new JSONParser();
			Object obj;
			obj = parser.parse(reader);
			JSONArray photos = (JSONArray) obj;

			photoGreyAverage = new long[photos.size()];

			for (int indicePhoto = 0; indicePhoto < photos.size(); indicePhoto++) {
				JSONObject photo = (JSONObject) photos.get(indicePhoto);

				photoGreyAverage[indicePhoto] = (long) photo.get("greyavg");
				//System.out.println(photoGreyAverage[indicePhoto]);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author S.Verel
	 * @category Compute the matrice of distance between solutions and of
	 *           inverse distance between positions
	 */
	private void computeDistances(String albumFileName) {
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
