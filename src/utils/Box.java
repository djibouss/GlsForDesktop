package utils;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.text.AbstractDocument.Content;

import pawn.Pawn;


/**
 * @author Djibouss
 * 
 */
public class Box {
	private int x;
	private int y;
	private int offsetX;
	private int offsetY;
	public static final int COORD_1_FIRST_BLACK_BOX = 50;
	public static final int COORD_2_FIRST_BLACK_BOX = 50;
	private static final int COORD_1_FIRST_WHITE_BOX = COORD_1_FIRST_BLACK_BOX;
	private static final int COORD_2_FIRST_WHITE_BOX = COORD_2_FIRST_BLACK_BOX + 2;

	/**
	 * We use a collection because of beetle case. <br>
	 * {@link Deque} because we need a LIFO.
	 */
	private Deque<Pawn> pawns = new LinkedList<Pawn>();
	/**
	 * If the {@link Content} is <code>null</code> option is different from
	 * <code>null</code> if it is possible for the current player to play at
	 * that place. <br>
	 * If the {@link Content} is not <code>null</code> option is different from
	 * <code>null</code> if it is possible for the current player to move that
	 * pawn.
	 * */
	private Integer option;

	/**
	 * first {@link Box} where {@link Color#BLACK} will play.
	 */
	public static final Box firstBlackBox = new Box(COORD_1_FIRST_BLACK_BOX,
			COORD_2_FIRST_BLACK_BOX);
	/**
	 * first {@link Box} where {@link Color#WHITE} will play.
	 */
	public static final Box firstWhiteBox = new Box(COORD_1_FIRST_WHITE_BOX,
			COORD_2_FIRST_WHITE_BOX);


	/*
	 * 
	 * 		x o x o x o
	 * 		o x o x o x
	 * 		x o x o x o
	 * 		o x o x o x
	 * 		x o x o x o
	 */

	private Box(int x, int y) {
		// private constructor because we do not want each box to be created 10
		// times!
		this.x = x;
		this.y = y;
	}



	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.option != null) {
			sb.append(this.option);
			if (this.pawns != null) {
				sb.append(" ");
			}
		}
		if (this.pawns != null) {
			for (Pawn p : this.pawns) {
				sb.append(p.toString());
			}
		}
		return sb.toString();
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the upper {@link Pawn} of the box.
	 */
	public Pawn getFirstPawn() {
		return pawns.peek();
	}

	/**
	 * @return the upper {@link Pawn} of the box and removes it.
	 */
	public Pawn pollFirstPawn() {
		return pawns.poll();
	}

	/**
	 * @param newPawn
	 *            the new {@link Pawn} to be added on top of the {@link Box}.
	 */
	public void addPawn(Pawn newPawn) {
		this.pawns.offerFirst(newPawn);
	}

	/**
	 * @return the pawns
	 */
	public Deque<Pawn> getPawns() {
		return pawns;
	}


	public Set<Box> getNeighbors(Box[][] grid) {
		Set<Box> neighbors = new HashSet<Box>();
		for (DirectionsToGetANeighbor d : DirectionsToGetANeighbor.values()) {
			neighbors.add(lazyGet(grid, x + d.getX(), y + d.getY()));
		}
		return neighbors;
	}

	public Set<Box> getNotEmptyNeighbors(Box[][] grid) {
		// get all neighbors
		Set<Box> allNeighbors = getNeighbors(grid);
		Set<Box> notEmptyNeighbors = new HashSet<Box>();
		// and remove the empty ones
		for (Box b : allNeighbors) {
			if (b != null && !b.isEmpty()) {
				notEmptyNeighbors.add(b);
			}
		}
		return notEmptyNeighbors;
	}

	public Set<Box> getEmptyNeighbors(Box[][] grid) {
		// get all neighbors
		Set<Box> allNeighbors = getNeighbors(grid);
		Set<Box> emptyNeighbors = new HashSet<Box>();
		// and keep only the empty ones
		for (Box b : allNeighbors) {
			// b should never be null because the getNeighbors is instantiating
			// if it was the case.
			if (b != null && b.isEmpty()) {
				emptyNeighbors.add(b);
			}
		}
		return emptyNeighbors;
	}

	/**
	 * @param grid
	 * @param d
	 * @return the next free spot in the direction given in input (will be used
	 *         for a grasshoper move.)
	 */
	public Box getNextFreeSpot(Box[][] grid, DirectionsToGetANeighbor d) {
		Box currentBox = this;
		Box nextBox = currentBox.lazyGet(grid, x + d.getX(), y + d.getY());
		// we need at list one pawn to jump on.
		if (nextBox.isEmpty()) {
			return null;
		}
		while (!nextBox.isEmpty()) {
			nextBox = nextBox.lazyGet(grid, nextBox.x + d.getX(),
					nextBox.y + d.getY());
		}
		return nextBox;
	}



	/**
	 * return the box whith coordonates i,j and instantiates it, if it was null.
	 */
	private Box lazyGet(Box[][] grid, int i, int j) {
		Box box = grid[i][j];
		if (box != null) {
			return box;
		}
		grid[i][j] = new Box(i, j);
		return grid[i][j];
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(Integer option) {
		this.option = option;
	}


	/**
	 * @return <code>true</code> iif box contains at least one Pawn
	 */
	public boolean isEmpty() {
		return (this.pawns == null || this.pawns.size() == 0);
	}

	/**
	 * @return <code>true</code> iif box contains at least one Pawn OR an
	 *         option.
	 */
	public boolean shouldBeDisplayed() {
		return (!this.isEmpty() || (this.option != null));
	}

	public boolean containsAnOption() {
		return (this.option != null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	/**
	 * @return the offsetX
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * @return the offsetY
	 */
	public int getOffsetY() {
		return offsetY;
	}
}
