package Movement;

import game.Board;
import game.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import utils.Box;
import utils.Color;
import utils.Race;

/**
 * Will contain all {@link OneStepMove} that the computer can do for next move.
 */
public class ComputerOptions {

	public ComputerOptions() {
		super();
		this.allOneStepMove = new LinkedList<OneStepMove>();

		// HashMap<Integer, Box> movableBoxes = board.getMovableBoxes(c);

	}

	private List<OneStepMove> allOneStepMove;

	/**
	 * @return the allOneStepMove
	 */
	public List<OneStepMove> getAllOneStepMove() {
		return allOneStepMove;
	}

	/**
	 * @return the first {@link OneStepMove} from the list.
	 */
	public OneStepMove getFirstOneStepMove() {
		if (this.allOneStepMove != null && this.allOneStepMove.size() > 0) {
			return this.allOneStepMove.get(0);
		}
		return null;
	}

	/**
	 * fill the ComputerOptions with all the possible {@link OneStepMove}
	 * 
	 * @param board
	 * @param c
	 * @param moveNumber
	 */
	public void initialize(Board board, Color c, int moveNumber) {
		initializeAdd(board, c, moveNumber);
	}

	private void initializeAdd(Board board, Color c, int moveNumber) {
		Set<Race> addableRace;
		if (Game.mustQueenBeAdded(moveNumber, c)) {
			addableRace = new HashSet<Race>();
			addableRace.add(Race.Queen);
		} else {
			addableRace = board.getPlayerPawns(c).getNotCompleteRace();
		}
		HashMap<Integer, Box> freeSpots = board.getAndMarkFreeSpots(c);

	}
}
