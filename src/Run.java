import album.AlbumAverageHash;
import album.AlbumTest;
import tools.Album;

public class Run {

	public static void main(String[] args) {
		switch (args[0]) {
			case "default":
				double bestrand = 1000;
				for (int i = 0; i < new Integer(args[1]); i++) {
					AlbumTest a = new AlbumTest(55, false);
					int[] sh = a.hillClimberFirstImprovement(10000);
					
					if(bestrand > a.eval(sh)){
						bestrand = a.eval(sh);
						Album.writeSolution(sh);
						System.out.println("\tA new best eval is founded : "+bestrand);
					}
				}
				
				System.out.println("The best evaluation is "+bestrand+". The solution is located at path/chronologic-order.sol");
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
