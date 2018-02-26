/*
 * Adaptive A* vs. Forward A* Testing
 */
public class AdaptiveVsForward {

	public static void main(String[] args) {
		
		// Forward vs. Adaptive
		Grid g;
    	Boolean found;
    	int numFound = 0;
    	
    	while (numFound < 50) {
    		g = new Grid(101,101,0,0,100,100);
    		
    		// First Search in Adaptive would be a normal forward anyway!
    		found = g.AdaptiveAStar(0, 0);
            
    		if (found) {
    			numFound++;
    			
    			//g.printGrid();
    			System.out.print(g.getNumberOfExpansions() + ", "+g.getOptimalPathCost() + ", ");
                
                g.resetPath();
                
                // Second Search, Different Initial State: pass initial state as argument (row, column)
                g.AdaptiveAStar(0, 0);
    			System.out.print(g.getNumberOfExpansions() + ", " +g.getOptimalPathCost() + "\n");
    		}
            
        }

	}

}
