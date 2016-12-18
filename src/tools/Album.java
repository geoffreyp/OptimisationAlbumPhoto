package tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Album {
	public static final String	photoFileName		= "data/info-photo.json";
	public static final String	albumFileName		= "data/info-album.json";
	public static final String	solutionFileName	= "data/chronologic-order.sol";
	private static double[][]	averageHashDistance	= null;

	public static double getAverageHashDistance(int image1, int image2) {
		if (averageHashDistance == null) {
			computeAverageHashDistances();
		}

		return averageHashDistance[image1][image2];
	}

	public static void printAverageHashDistance(int[] solution) {
		String s = "";
		int j = 0;
		for (int i = 0; i < solution.length - 1; i++) {
			j++;
			s += round(getAverageHashDistance(solution[i], solution[i + 1]), 5) + "\t\t";
			if (j == 6) {
				s += "\n";
				j = 0;
			}
		}
		System.out.println("\t\t\t===== Distances with the next photo ===== \n" + s + "\n");
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * @author S.Verel
	 */
	public static void computeAverageHashDistances() {
		try {
			FileReader reader = new FileReader(photoFileName);

			JSONParser parser = new JSONParser();

			Object obj = parser.parse(reader);

			JSONArray array = (JSONArray) obj;

			averageHashDistance = new double[array.size()][array.size()];

			for (int i = 0; i < array.size(); i++) {
				JSONObject image = (JSONObject) array.get(i);
				JSONArray d = (JSONArray) image.get("ahashdist");
				for (int j = 0; j < d.size(); j++) {
					averageHashDistance[i][j] = (double) d.get(j);

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
	 * Ecrit la solution dans un fichier utilisé par le créateur de pages html
	 */
	public static void writeSolution(int[] s) {
		String solution = "";
		for (int i = 0; i < s.length; i++) {
			solution += s[i] + " ";
		}

		try {
			PrintWriter writer = new PrintWriter(Album.solutionFileName);
			writer.write(solution);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void writeCsvFile(String filename, int nbEval, double eval) {
		try {
			FileWriter writer = new FileWriter(filename, true);
			writer.append(nbEval+","+eval+"\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
