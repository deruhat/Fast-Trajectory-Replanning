import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Grid {
	Node[][] grid;
	int ROW_MAX, COLUMN_MAX; // max x and y values in the grid (length - 1)

	ArrayList<Node> openedList;
	Stack<Node> closedList;
	Stack<Node> optimalPath;
	Node currNode;
	Node initial, goal;

	int numberOfExpansions;

	boolean isAdaptive = false;
	boolean second_run = false;

	Boolean foundPath; // found a path from initial to goal node

	/**
	 * Create Grid Object
	 * 
	 * @param row
	 *            Number of rows
	 * @param column
	 *            Number of columns
	 * @param initial_row
	 *            Row position on grid for Initial Node
	 * @param initial_column
	 *            Column position on grid for Initial Node
	 * @param goal_row
	 *            Row position on grid for Goal Node
	 * @param goal_column
	 *            Column position on grid for Goal Node
	 */
	public Grid(int row, int column, int initial_row, int initial_column, int goal_row, int goal_column) {
		// check row and column sizes
		if (row < 1) {
			ROW_MAX = 101;
		}
		if (column < 1) {
			COLUMN_MAX = 101;
		}

		ROW_MAX = row - 1;
		COLUMN_MAX = column - 1;

		this.grid = new Node[row][column];
		setGrid();

		// row and column checks for initial node
		if (initial_row > row || initial_column > column || initial_row < 0 || initial_column < 0) {
			initial = this.grid[0][0];
		} else {
			initial = this.grid[initial_row][initial_column];
		}

		// row and column checks for goal node
		if (goal_row > row || goal_column > column || goal_row < 0 || goal_column < 0) {
			goal = this.grid[ROW_MAX][COLUMN_MAX];
		} else {
			goal = this.grid[goal_row][goal_column];
		}

		calculateHValue();

		openedList = new ArrayList<Node>();
		closedList = new Stack<Node>();
		optimalPath = new Stack<Node>();

		initial.isBlocked = false;
		goal.isBlocked = false;
		this.currNode = initial;

		numberOfExpansions = 0;

		foundPath = false;
	}

	/**
	 * Purely random grid arrangement
	 */
	public void setGrid() {
		for (int i = 0; i <= ROW_MAX; i++) {
			for (int j = 0; j <= COLUMN_MAX; j++) {
				this.grid[i][j] = new Node(i, j, 0, toBlockOrNotToBlock(), ROW_MAX, COLUMN_MAX);
			}
		}
	}

	/**
	 * Print the grid in the terminal
	 */
	public void printGrid() {
		System.out.print("\n");
		for (int i = 0; i <= ROW_MAX; i++) {
			System.out.print(i + "\t");
			for (int j = 0; j <= COLUMN_MAX; j++) {
				if (this.grid[i][j].isBlocked) {
					System.out.print("X");
				} else if (this.grid[i][j].path) {
					System.out.print("0");
				} else {
					System.out.print("_");
				}
			}
			System.out.print("\n");
		}
	}

	/**
	 * Calculate hueristic distance between
	 * 
	 * @param n
	 *            Node to check
	 * @return h value
	 */
	public void calculateHValue() {
		for (int i = 0; i <= ROW_MAX; i++) {
			for (int j = 0; j <= COLUMN_MAX; j++) {
				this.grid[i][j].h = Math.abs(goal.row - i) + Math.abs(goal.column - j);
			}
		}
	}

	/**
	 * Cheap and easy way to determine if node is blocked or not with 30%
	 * probability of blocking
	 */
	public boolean toBlockOrNotToBlock() {
		return Math.random() * 10 < 3 ? true : false;
	}

	/**
	 * Check if node has a valid left neighbor
	 */
	public boolean nodeHasLeft(Node n) {
		if (n.column > 0 && !this.grid[n.row][n.column - 1].isBlocked) {
			return true;
		}
		return false;
	}

	/**
	 * Check if node has a valid up neighbor
	 */
	public boolean nodeHasUp(Node n) {
		if (n.row > 0 && !this.grid[n.row - 1][n.column].isBlocked) {
			return true;
		}
		return false;
	}

	/**
	 * Check if node has a valid right neighbor
	 */
	public boolean nodeHasRight(Node n) {
		if (n.column < ROW_MAX && !this.grid[n.row][n.column + 1].isBlocked) {
			return true;
		}
		return false;
	}

	/**
	 * Check if node has a valid down neighbor
	 */
	public boolean nodeHasDown(Node n) {
		if (n.row < COLUMN_MAX && !this.grid[n.row + 1][n.column].isBlocked) {
			return true;
		}
		return false;
	}

	/**
	 * Get left neighbor of node
	 */
	public Node getLeftNode(Node n) {
		Node temp = this.grid[n.row][n.column - 1];
		return new Node(temp.row, temp.column, temp.h, temp.isBlocked, ROW_MAX, COLUMN_MAX);
	}

	/**
	 * Get up neighbor of node
	 */
	public Node getUpNode(Node n) {
		Node temp = this.grid[n.row - 1][n.column];
		return new Node(temp.row, temp.column, temp.h, temp.isBlocked, ROW_MAX, COLUMN_MAX);
	}

	/**
	 * Get right neighbor of node
	 */
	public Node getRightNode(Node n) {
		Node temp = this.grid[n.row][n.column + 1];
		return new Node(temp.row, temp.column, temp.h, temp.isBlocked, ROW_MAX, COLUMN_MAX);
	}

	/**
	 * Get down neighbor of node
	 */
	public Node getDownNode(Node n) {
		Node temp = this.grid[n.row + 1][n.column];
		return new Node(temp.row, temp.column, temp.h, temp.isBlocked, ROW_MAX, COLUMN_MAX);
	}

	/**
	 * Add a node to Opened List only if new node has smaller f value
	 * 
	 * @param n
	 */
	public void addToOpenedList(Node n) {
		if (openedList.contains(n)) {
			if (openedList.get(openedList.indexOf(n)).getFValue() > n.getFValue()) { // add smaller version of node
				openedList.remove(openedList.indexOf(n));
				n.setParent(currNode);
				this.grid[n.row][n.column].setParent(this.grid[currNode.row][currNode.column]);
				openedList.add(n);
			}
		} else {
			n.setParent(currNode);
			this.grid[n.row][n.column].setParent(this.grid[currNode.row][currNode.column]);
			openedList.add(n);
		}

	}

	/**
	 * reset path variable of every node in grid
	 */
	public void resetPath() {
		for (int i = 0; i <= ROW_MAX; i++) {
			for (int j = 0; j <= COLUMN_MAX; j++) {
				this.grid[i][j].path = false;
				this.grid[i][j].parent = null;
			}
		}
		numberOfExpansions = 0;
	}

	/**
	 * Print the list of nodes in the optimal path found from the previous search
	 */
	public void printOptimalPath() {
		int count = 0;
		while (!optimalPath.isEmpty()) {
			System.out.print(optimalPath.pop() + " => ");
			count++;
		}
		System.out.println("\nTotal Cost of Path Found:" + count);
	}

	/**
	 * Print the cost of the optimal path found
	 */
	public int getOptimalPathCost() {
		int count = 0;
		while (!optimalPath.isEmpty()) {
			count++;
			optimalPath.pop();
		}
		return count;
	}

	public int getNumberOfExpansions() {
		return numberOfExpansions;
	}

	/**
	 * Helper function to perform A* Search forward or backwards
	 * 
	 * @param direction
	 *            0 for forward, 1 for backward
	 * @return True if path if found, false if not
	 */
	public Boolean AStar(int direction, int priority) {
		Boolean ret = false;

		numberOfExpansions = 0;

		if (direction == 0) { // forward
			currNode = initial;
			calculateHValue();
			ret = AStarSearch(0, priority);
			openedList.clear();

			while (!closedList.isEmpty()) {
				closedList.pop();
			}

			return ret;
		} else if (direction == 1) { // backward
			
			// swap initial and goal
			Node temp = goal;
			goal = initial;
			initial = temp;
			calculateHValue();
			currNode = initial;

			ret = AStarSearch(0, priority);
			openedList.clear();

			while (!closedList.isEmpty()) {
				closedList.pop();
			}

			// swap initial and goal back
			temp = goal;
			goal = initial;
			initial = temp;

			return ret;
		} else { // error input
			return false;
		}
	}

	/**
	 * Perform the AStar Search and alter the Grid to reflect the optimal path found
	 */
	private Boolean AStarSearch(int step, int priority) {
		numberOfExpansions = step;
		this.grid[currNode.row][currNode.column].h = currNode.h;
		this.grid[currNode.row][currNode.column].g = currNode.g;
		this.grid[currNode.row][currNode.column].f = currNode.f;
		closedList.push(currNode);

		// base case
		if (goal.equals(currNode)) {
			foundPath = true;
			return foundPath;
		}

		// add neighbors to opened list
		Node temp;
		if (nodeHasLeft(currNode)) {
			temp = getLeftNode(currNode); // get the left node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
			}
		}
		if (nodeHasUp(currNode)) {
			temp = getUpNode(currNode); // get the up node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
			}
		}
		if (nodeHasRight(currNode)) {
			temp = getRightNode(currNode); // get the right node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
			}
		}
		if (nodeHasDown(currNode)) {
			temp = getDownNode(currNode); // get the down node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
			}
		}
		
		if (openedList.size() < 1) {
			return foundPath;
		}

		// tie breaking and sorting
		if (priority == 1) {
			Collections.sort(openedList, Node.LESSER_G); // priority for smaller g values
		} else {
			Collections.sort(openedList, Node.GREATER_G); // priority for larger g values
		}
		
		currNode = openedList.get(0);
		openedList.remove(0);

		AStarSearch(step + 1, priority);

		// all done with the planning phase
		// trail up from the currNode should be the optimal path

		if (currNode != null) {
			this.grid[currNode.row][currNode.column].path = true;
			optimalPath.push(currNode);
			currNode = currNode.getParent();
		}

		return foundPath; // end
	}

	/**
	 * Helper function to perform A* Search forward or backwards
	 * 
	 * @param direction
	 *            0 for forward, 1 for backward
	 * @return True if path if found, false if not
	 */
	public Boolean AdaptiveAStar(int initial_row, int initial_column) {
		Boolean ret = false;

		numberOfExpansions = 0;

		// always forward
		currNode = grid[initial_row][initial_column];
		if (!isAdaptive) {
			calculateHValue();
			isAdaptive = true;
		} else {
			second_run = true;
		}
		ret = AdaptiveAStarSearch(0);
		openedList.clear();

		// Adaptive A*:
		if (ret == true) {
			Node temp;
			while (!closedList.isEmpty()) {
				temp = closedList.pop();
				int tempH = this.grid[temp.row][temp.column].h;
				this.grid[temp.row][temp.column].h = this.goal.getGValue() - temp.getGValue();
				if ((tempH != this.grid[temp.row][temp.column].h)) {
					/*
					 * System.out.print("Node: " + temp + " changed h from " + tempH);
					 * System.out.print(" to " + this.grid[temp.row][temp.column].h + "\n");
					 */
				}
			}
		}
		return ret;
	}

	/**
	 * Perform the Adaptive AStar Search and alter the Grid to reflect the optimal
	 * path found
	 */
	private Boolean AdaptiveAStarSearch(int step) {
		numberOfExpansions = step;
		this.grid[currNode.row][currNode.column].h = currNode.h;
		this.grid[currNode.row][currNode.column].g = currNode.g;
		this.grid[currNode.row][currNode.column].f = currNode.f;
		closedList.push(currNode);

		// base case
		if (goal.equals(currNode)) {
			foundPath = true;
			return foundPath;
		}

		// add neighbors to opened list
		Node temp;
		if (nodeHasLeft(currNode)) {
			temp = getLeftNode(currNode); // get the left node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
			}
		}
		if (nodeHasUp(currNode)) {
			temp = getUpNode(currNode); // get the up node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
				// addToOpenList(temp, priority);
			}
		}
		if (nodeHasRight(currNode)) {
			temp = getRightNode(currNode); // get the right node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
			}
		}
		if (nodeHasDown(currNode)) {
			temp = getDownNode(currNode); // get the down node
			if (!closedList.contains(temp)) { // don't update g value if in closed list
				temp.updateGValue(currNode.getGValue() + 1); // update node's g and f values
				addToOpenedList(temp);
			}
		}
		
		if (openedList.size() < 1) {
			return foundPath;
		}

		Collections.sort(openedList, Node.GREATER_G); // priority for larger g values

		currNode = openedList.get(0);
		openedList.remove(0);

		AdaptiveAStarSearch(step + 1);

		// all done with the planning phase
		// trail up from the currNode should be the optimal path

		if (currNode != null) {
			this.grid[currNode.row][currNode.column].path = true;
			optimalPath.push(currNode);
			currNode = currNode.getParent();
		}

		return foundPath; // end
	}
}