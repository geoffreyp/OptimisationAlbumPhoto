import album.AlbumAverageHash;
import album.AlbumRandom;
import tools.Album;

public class Run {

	public static void main(String[] args) {
		switch (args[0]) {
			case "rand":
				System.out.println("Random Album");
				double bestrand = 1000;
				for (int i = 0; i < new Integer(args[1]); i++) {
					AlbumRandom a = new AlbumRandom(55, false);
					int[] sh = a.hillClimberFirstImprovement(10000);
					
					if(bestrand > a.eval(sh)){
						bestrand = a.eval(sh);
						Album.writeSolution(sh);
						System.out.println("ecrit "+bestrand);
					}
				}
				
				System.out.println("Evaluation : "+bestrand);
				break;
				
			case "ahash":
				System.out.println("Average Album");
				double best = 1000;
				for (int i = 0; i < new Integer(args[1]); i++) {
					AlbumAverageHash ah = new AlbumAverageHash(55, false);
					int[] sh = ah.hillClimberFirstImprovement(10000);
					
					if(best > ah.eval(sh)){
						best = ah.eval(sh);
						Album.writeSolution(sh);
						System.out.println("ecrit "+best);
					}
				}
				
				System.out.println("Evaluation : "+best);
				
				break;
			
			default:
				System.err.println("Error");
				break;
		}
	}
}
