package Movement;

import utils.Box;

/**
 * @author Djibouss Can represent any "coup" ie move or add of a pawn.
 */
public class OneStepMove {

	private Box startingBox;
	private Box destinationBox;
	private Action action;
	private int score;

	/**
	 * @return the startingBox
	 */
	public Box getStartingBox() {
		return startingBox;
	}

	/**
	 * @param startingBox
	 *            the startingBox to set
	 */
	protected void setStartingBox(Box startingBox) {
		this.startingBox = startingBox;
	}

	/**
	 * @return the destinationBox
	 */
	public Box getDestinationBox() {
		return destinationBox;
	}

	/**
	 * @param destinationBox
	 *            the destinationBox to set
	 */
	protected void setDestinationBox(Box destinationBox) {
		this.destinationBox = destinationBox;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	protected void setAction(Action action) {
		this.action = action;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	protected void setScore(int score) {
		this.score = score;
	}

}
