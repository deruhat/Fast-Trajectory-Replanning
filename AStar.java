/*
 * Forward A* vs. Backward A* Testing
 */
public class AStar {

	public static void main(String[] args) {
		
		// Forward vs. Backward
		Grid g;
		Boolean found;
		int numFound = 0;

		while (numFound < 50) {
			g = new Grid(101, 101, 0, 0, 100, 100);

			// forward
			found = g.AStar(0, 2);
			if (found) {
				numFound++;
				System.out.print(g.getNumberOfExpansions() + "," + g.getOptimalPathCost() + ",");
				g.resetPath();

				// backward
				g.AStar(1, 2);
				System.out.print(g.getNumberOfExpansions() + "," + g.getOptimalPathCost() + "\n");
			}

		}

	}
}