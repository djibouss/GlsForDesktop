
package utils;

/**
 * define the direction of the 6 vectors(x,y) to get a neighbor from a given
 * box.
 */
public enum DirectionsToGetANeighbor {
	One(1, 1), Two(1, -1), Three(-1, 1), Four(-1, -1), Five(0, 2), Six(0, -2);
	private int x, y;

	private DirectionsToGetANeighbor(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
