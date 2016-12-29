import album.AlbumAhash;
import album.AlbumAhashTags;
import algorithm.LocalSearchAlgorithm;
import tools.Album;

public class Run {
	// Arg 0 : choose the evaluation function
	// Arg 1 : choose the algorithm
	// Arg 2 : number of execution
	// Arg 3 : number of iteration for the ILS algorithm
	// Arg 3 : debug mode

	public static void main(String[] args) {
		boolean debug = false;
		LocalSearchAlgorithm a;
		switch (args[0]) {
			case "default":

				if (args.length > 4 && args[4].equals("true")) {
					debug = true;
				}
				System.out.println("Mode debug : " + debug);

				double bestrand = 1000;
				for (int i = 0; i < new Integer(args[2]); i++) {
					a = new AlbumAhash(55, debug);
					int[] sh = getSolution(args[1], a, new Integer(args[3]));

					if (bestrand > a.eval(sh)) {
						bestrand = a.eval(sh);
						Album.writeSolution(sh);

						if (debug) {
							System.out.println("A new best eval is founded : " + bestrand + "");
							Album.printAverageHashDistance(sh);
						}
					}
				}

				System.out.println("The best evaluation founded is " + bestrand + ". The solution is located at path/chronologic-order.sol\n");
				break;

			case "tags":
				
				if (args.length > 4 && args[4].equals("true")) {
					debug = true;
				}
				System.out.println("Mode debug : " + debug);
				
				double best = 1000;
				for (int i = 0; i < new Integer(args[2]); i++) {
					a = new AlbumAhashTags(55, debug);
					int[] sh = getSolution(args[1], a, new Integer(args[3]));

					if (best > a.eval(sh)) {
						best = a.eval(sh);
						Album.writeSolution(sh);
						System.out.println("ecrit " + best);
					}
				}

				System.out.println("Evaluation : " + best);

				break;

			default:
				System.err.println("Error");
				break;
		}
	}

	private static int[] getSolution(String algorithm, LocalSearchAlgorithm a, int ilsIteration) {
		int[] res = null;

		switch (algorithm) {
			case "hc":
				res = a.hillClimberFirstImprovement(10000);
				break;

			case "ils":
				res = a.IteratedLocalSearch(ilsIteration);
				break;

			default:
				res = a.hillClimberFirstImprovement(10000);
				break;
		}

		return res;
	}
}
