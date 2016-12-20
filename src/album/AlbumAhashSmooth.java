package album;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import algorithm.LocalSearchAlgorithm;
import tools.Album;
import tools.HillClimber;

/* WIP TODO : add to the evaluation function a system to sort photo on a page */
public class AlbumAhashSmooth extends LocalSearchAlgorithm {

	private double[][]			photoDist;
	private double[][]			albumInvDist;
	private ArrayList<String>[]	photoTags;

	public AlbumAhashSmooth(int taille, boolean debug) {
		super(debug);
		computeDistances(Album.photoFileName, Album.albumFileName);
		computePhotosTags();
		this.nb_photos = taille;
		solution = HillClimber.generateShuffleSolution(taille);
	}

	/**
	 * @author S.Verel
	 * @Category Un exemple de fonction objectif (à minimiser): distance entre
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
	
	public double getTagWeight(int photo1, int photo2){
		double w = 0;
		
		for (int i = 0; i < photoTags[photo1].size(); i++) {
			for (int j = 0; j < photoTags[photo2].size(); j++) {
				if(photoTags[photo1].get(i).equals(photoTags[photo1].get(j)))
					w++;
			}
		}
		
		return w;
	}

	/**
	 * @author geoffrey
	 * Keep tags of photos with a high probability
	 */
	@SuppressWarnings("unchecked")
	public void computePhotosTags() {
		try {
			FileReader reader = new FileReader(Album.photoFileName);
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(reader);
			JSONArray photos = (JSONArray) obj;

			photoTags = new ArrayList[photos.size()];

			for (int indicePhoto = 0; indicePhoto < photos.size(); indicePhoto++) {
				JSONObject photo = (JSONObject) photos.get(indicePhoto);
				JSONObject tags = (JSONObject) photo.get("tags");
				JSONArray tagsClasses = (JSONArray) tags.get("classes");
				JSONArray tagsProbs = (JSONArray) tags.get("probs");

				photoTags[indicePhoto] = new ArrayList<>();

				for (int indicetag = 0; indicetag < tagsClasses.size(); indicetag++) {
					double prob = (double) tagsProbs.get(indicetag);
					String tag = (String) tagsClasses.get(indicetag);
					if (prob > 0.97 && !tag.equals("nobody")) {
						photoTags[indicePhoto].add(tag);
						//System.out.println("Photo "+indicePhoto+" => "+tag);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
