import java.util.Comparator;

public class Node implements Comparable<Node> {
	public int row, column;
	public int f, g, h;
	public Node parent;
	public boolean isBlocked;
	public boolean visited;
	public boolean path; // part of the optimal path
	
	/**
	 * 
	 * Node object row = row of array y = column of array ex) (1,0) has x = 0, y
	 * 
	 * = 1
	 * 
	 */
	public Node(int row, int column, int h, boolean isBlocked, int row_max, int column_max) {
		this.parent = null;
		this.row = row;
		this.column = column;
		this.h = h;
		this.g = 0;
		this.f = 0;
		this.isBlocked = isBlocked;
		this.visited = false;
		this.path = false;
	}

	/**
	 * 
	 * Set the new parent node of the current node
	 * 
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * Get the parent node of the current node
	 * 
	 */
	public Node getParent() {
		return this.parent;
	}

	/**
	 * 
	 * calulate a heurtistic value for the current node
	 * 
	 */
	public int calculateHValue(int row_max, int column_max) {
		return (row_max - this.row) + (column_max - this.column);
	}

	/**
	 * 
	 * Return the g value of the current node
	 * 
	 */
	public int getGValue() {
		return this.g;
	}

	/**
	 * 
	 * Return the h value of the current node
	 * 
	 */
	public int getHValue() {
		return this.h;
	}

	/**
	 * 
	 * Return the f value of the current node
	 * 
	 */
	public int getFValue() {
		return getGValue() + getHValue();
	}

	/**
	 * 
	 * Updates the current g value and respectively updates the f value of the
	 * 
	 * node
	 * 
	 */
	public void updateGValue(int newG) {
		this.g = newG;
		this.f = this.g + this.h;
	}

	/**
	 * 
	 * Node has been visited
	 * 
	 */

	public void visit() {
		this.visited = true;
	}

	/**
	 * 
	 * Check if Node has been visited
	 * 
	 */

	public boolean isVisited() {
		return this.visited;
	}

	/**
	 * 
	 * Custom equals method for
	 * 
	 */

	@Override

	public boolean equals(Object other) {
		if (!(other instanceof Node)) {
			return false;
		}
		Node that = (Node) other;
		return this.row == that.row && this.column == that.column;
	}

	public int compareTo(Node other) {
		if (this.f == other.f) {
			return this.g - other.g; // this sorts by g value smallest ->
			// largest
			// return other.g - this.g; // this sorts by g value largest ->
			// smallest
		}
		return this.f - other.f;
	}

	public String toString() {
		return "(" + this.row + " " + this.column + ")";
	}

	static final Comparator<Node> GREATER_G = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			if (o1.f == o2.f) {
				return o2.g - o1.g; // this sorts by g value largest -> smallest
			}
			return o1.f - o2.f;
		}
		//
	};

	static final Comparator<Node> LESSER_G = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			if (o1.f == o2.f) {
				return o1.g - o2.g; // this sorts by g value smallest -> largest
			}
			return o1.f - o2.f;
		}
	};
}