/*
 * The Effects of Ties Testing
 */
public class EffectsOfTies {
	public static void main(String[] args) {
		
		// Test larger vs smaller tie breaking
		Grid g;
		Boolean found;
		int numFound = 0;

		while (numFound < 50) {
			g = new Grid(101, 101, 0, 0, 100, 100);

			// smaller
			found = g.AStar(0, 1);
			if (found) {
				numFound++;
				System.out.print(g.getNumberOfExpansions() + "," + g.getOptimalPathCost() + ",");
				g.resetPath();

				// larger
				g.AStar(0, 2);
				System.out.print(g.getNumberOfExpansions() + "," + g.getOptimalPathCost() + "\n");
			}

		}
	}
}
