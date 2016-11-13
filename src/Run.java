public class Run {

	public static void main(String[] args) {

		double best = 1000;

		for (int i = 0; i < 1000; i++) {
			AlbumPhoto a = new AlbumPhoto(55);
			double t = a.hillClimberFirstImprovement(10000);

			if (t < best) {
				best = t;
			}
		}
		System.out.println(best);
		
		
	}
}
