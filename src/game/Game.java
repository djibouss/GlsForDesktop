package game;

import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import pawn.Pawn;
import uI.HyveFrame;
import uI.HyvePanel;
import utils.Box;
import utils.Color;
import utils.Race;
import utils.ReadInput;
import Movement.Action;
import Movement.ComputerOptions;
import Observer.Observer;

public class Game implements Observer {
	private static JFrame jFrame = new HyveFrame();
	private static HyvePanel playPanel = new HyvePanel();
	private static JLabel jLabel = new JLabel();

	/**
	 * @return the jFrame
	 */
	public static JFrame getjFrame() {
		return jFrame;
	}

	/**
	 * @return the playPanel
	 */
	public static HyvePanel getPlayPanel() {
		return playPanel;
	}

	private static Board board = new Board(100);

	/**
	 * If a {@link Race} was selected (for adding a pawn).
	 */
	private static Race selectedRace;
	/**
	 * If a {@link Box} was selected (as a start or a destination from a move).
	 */
	private static Box selectedBox;

	private static final int BASIC_WAITING_TIME = 100;

	/**
	 * @return the board
	 */
	public static Board getBoard() {
		return board;
	}

	static {
		jFrame.add(playPanel);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		playPanel.addObserver(new Game());
		int moveNumber = 1;
		boolean gameOver = false;
		outer: while (!gameOver) {
			for (Color c : Color.values()) {
				if (gameOver) {
					break outer;
				}
				// ComputerOptions computerOptions = new ComputerOptions();
				// computerOptions.initialize(board, c, moveNumber);
				playPanel.cleanPreviousSelection();
				playPanel.repaint();
				System.out.println("---------------------Mister " + c
						+ " is playing.----------------------------------");
				selectedRace = null;
				selectedBox = null;
				while (selectedRace == null && selectedBox == null) {
					waitBasicTime();
					if (selectedRace != null) {
						if (mustQueenBeAdded(moveNumber, c) && selectedRace != Race.Queen) {
							System.out
							.println("You have no choice on which race to add! It's now time to add your queen!!");
							selectedRace = null;
							continue;
						}
						handle(Action.ADD, c, moveNumber);
					}
					// selectedBox should never be null here, its just for
					// safety reason.
					else if (selectedBox != null) {
						HashMap<Integer, Box> possibleMove = board.getMovableBoxes(c);
						if (!board.isQueenOnTheGame(c)) {
							System.out
							.println("not allowed to move a Box if Queen is not yet on the game");
							selectedBox = null;
							continue;
						} else if (!isBoxInHashMap(possibleMove, selectedBox)) {
							System.out.println("not allowed to move this Box");
							selectedBox = null;
							continue;
						}
						board.removeOptions(possibleMove);
						handle(Action.MOVE, c, moveNumber);
					}
				}
				moveNumber++;
				gameOver = board.isQueenSurrounded(Color.BLACK)
						|| board.isQueenSurrounded(Color.WHITE);
			}
		}
		// Game is now over.
		String message = "lost!!!";
		if (board.isQueenSurrounded(Color.BLACK)) {
			if (board.isQueenSurrounded(Color.WHITE)) {
				message = "nice fight!!!! you both win!!";
			} else {
				message = "Congrats White!!";
			}
		} else {
			message = "Congrats Black!!";
		}

		JOptionPane.showMessageDialog(jLabel, message);
	}

	/**
	 * @param moveNumber
	 * @param c
	 * @return <code>true</code> if the player can play only the queen at this
	 *         point.
	 */
	public static boolean mustQueenBeAdded(int moveNumber, Color c) {
		return !board.isQueenOnTheGame(c) && moveNumber > 6;
	}

	private static void waitBasicTime() {
		try {
			Thread.sleep(BASIC_WAITING_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void handle(Action action, Color c, int moveNumber) {
		switch (action) {
		case ADD:
			handleAdd(c, moveNumber);
			break;

		case MOVE:
			handleMove(c);
			break;
		}
	}

	private static void handleMove(Color c) {
		Pawn movedPawn = selectedBox.pollFirstPawn();
		HashMap<Integer, Box> possibleDestination = board.markAndGetPossibleDestinations(movedPawn);
		System.out.println("size" + possibleDestination.size());
		playPanel.repaint();
		while (true) {
			selectedBox = null;
			waitForUpdateSelectedBox();
			if (isBoxInHashMap(possibleDestination, selectedBox)) {
				break;
			}
		}
		selectedBox.addPawn(movedPawn);
		movedPawn.setLocalisation(selectedBox);
		if (movedPawn.getRace() == Race.Queen) {
			board.getPlayerPawns(c).setQueenLocalisation(selectedBox);
		}
		board.removeOptions(possibleDestination);
		playPanel.repaint();
	}

	private static void handleAdd(Color c, int moveNumber) {
		// if (!board.isQueenOnTheGame(c) && moveNumber > 6) {
		// System.out
		// .println("You have no choice on which race to add! It's now time to add your queen!!");
		// selectedRace = Race.Queen;
		// } else {
		// System.out.println("Mister " + c
		// + " Which animal do you want to add?");
		// selectedRace = ReadInput.getInputFromUser(board.getPlayerPawns(c)
		// .getNotCompleteRace());
		// }

		// for the 2 first moves, there is only one option for the position
		// of the new Pawn.
		if (moveNumber <= 2) {
			board.add(selectedRace, c);
		} else {
			HashMap<Integer, Box> possibleAdd = board.getAndMarkFreeSpots(c);
			playPanel.repaint();
			boolean validDestination = false;
			while (!validDestination) {
				waitForUpdateSelectedBox();
				if (!isBoxInHashMap(possibleAdd, selectedBox)) {
					selectedBox = null;
					System.out.println("not a valid destination");
				} else {
					validDestination = true;
					board.removeOptions(possibleAdd);
					board.add(selectedRace, c, selectedBox);
				}
			}
		}
	}

	private static boolean isBoxInHashMap(HashMap<Integer, Box> possibleAdd, Box box) {
		return possibleAdd.values().contains(box);
	}

	private static void waitForUpdateSelectedBox() {
		while (selectedBox == null) {
			waitBasicTime();
		}
	}

	private static Box chooseAmongOptions(HashMap<Integer, Box> possibleAdd) {
		Integer i = ReadInput.getInputFromUser(possibleAdd.size());

		Box chosenBox = possibleAdd.get(i);
		board.removeOptions(possibleAdd);
		return chosenBox;
	}

	@Override
	public void update(Race r) {
		Game.selectedRace = r;
	}

	@Override
	public void update(Box b) {
		Game.selectedBox = b;
	}

	// // just for debugging
	// private static void displayAllPawnsNeighbors() {
	// for (Pawn p : board.getAllPawnsInPlay()) {
	// System.out.println(p + " has for neigbors "
	// + p.getNeighbors(board.getGrid()));
	// }
	// }
	//
	// // just for debugging
	// private static void displayAllNotEmptyBoxWithContent() {
	// for (Box[] bA : board.getGrid()) {
	// for (Box b : bA) {
	// if (b != null && !b.isEmpty()) {
	// System.out.println(b);
	// }
	// }
	// }
	// }
}
