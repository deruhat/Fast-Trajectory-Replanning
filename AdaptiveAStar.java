/*
 * Adaptive A* Testing
 */
public class AdaptiveAStar {

	public static void main(String[] args) {
		Grid g;
    	Boolean found;
    	int numFound = 0;
    	
    	while (numFound < 1) {
    		g = new Grid(101,101,0,0,100,100);
    		
    		// First Search in Adaptive
    		found = g.AdaptiveAStar(0, 0);
            
    		if (found) {
    			numFound++;
    			
    			//g.printGrid();
    			System.out.print("Expansions: " + g.getNumberOfExpansions() + ", Optimal Path Cost: "+g.getOptimalPathCost() + "\n");
                
                g.resetPath();
                
                // Second Search, Different Initial State: pass initial state as argument (row, column)
                g.AdaptiveAStar(40, 35);
    			System.out.print("Expansions: " + g.getNumberOfExpansions() + ", Optimal Path Cost: "+g.getOptimalPathCost() + "\n");
    		}
            
        }

	}

}
