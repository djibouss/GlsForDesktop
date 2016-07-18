package uI;

import game.Board;
import game.Game;
import game.PlayerPawns;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.UIManager;

import pawn.Pawn;
import utils.Box;
import utils.Race;
import Observer.Observable;
import Observer.Observer;

public class HyvePanel extends JPanel implements MouseListener, Observable {
	// Width of our hexagon
	private static int WIDTH_PAWN = 70;
	/**
	 * at the bottom for some reasons some spaces were lost, we need this
	 * variable in order not to cut the addable white pawns.
	 */
	private final static int LOST_SPACE_ON_THE_BOTTOM = 40;
	// after calculation in a perfect hexagon.
	private final static int HEIGHT_PAWN = (int) ((Math.sqrt(3.0) / 2) * WIDTH_PAWN);
	private final static int x1 = (int) (HEIGHT_PAWN / (2.0 * Math.sqrt(3.0)));
	private static final int X_SPACE_BETWEEN_BOXES = HyvePanel.WIDTH_PAWN - x1;
	private final static int[] xPointsStandard = { 0, x1, WIDTH_PAWN - x1,
			WIDTH_PAWN, WIDTH_PAWN - x1, x1 };
	private final static int[] yPointsStandard = { HEIGHT_PAWN / 2, 0, 0,
			HEIGHT_PAWN / 2, HEIGHT_PAWN, HEIGHT_PAWN };
	// space at the top that we would like to have between the edge and the
	// first pawn: one WIDTH_PAWN is needed for allowed pawns and one WIDTH_PAWN
	// is needed for space reasons.
	public final static int SPACE_ON_THE_TOP = 2 * HEIGHT_PAWN;
	private final static int X_FIRST_BOX = HyveFrame.FRAME_WIDTH / 2;
	private final static int Y_FIRST_BOX = HyveFrame.FRAME_HEIGHT / 3;

	private static final long serialVersionUID = 1L;

	/**
	 * {@link Box} which is currently selected.
	 */
	private Box selectedBox = null;
	/**
	 * equals to the {@link Race} which is going to be added, to
	 * <code>null</code> if black player is not currently adding a pawn.
	 */
	private Race toBeAddedBlackRace;
	/**
	 * equals to the {@link Race} which is going to be added, to
	 * <code>null</code> if white player is not currently adding a pawn.
	 */
	private Race toBeAddedWhiteRace;

	/**
	 * {@link Set} containing all the black pawns which can still be added.
	 * Updated each time paintComponent is called().
	 */
	private Set<Race> notCompleteBlackSet;

	/**
	 * {@link Set} containing all the white pawns which can still be added.
	 * Updated each time paintComponent is called().
	 */
	private Set<Race> notCompleteWhiteSet;
	private Observer observer;

	public HyvePanel() {
		super();
		addMouseListener(this);
	}

	public void paintComponent(Graphics g) {
		// System.out.println(g.getColor().);
		// Color color = UIManager.getColor ( "Panel.background" );

		g.setColor(UIManager.getColor("Panel.background"));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		Board board = Game.getBoard();
		PlayerPawns blackPawns = board.getPlayerPawns(utils.Color.BLACK);
		PlayerPawns whitePawns = board.getPlayerPawns(utils.Color.WHITE);
		notCompleteBlackSet = blackPawns.getNotCompleteRace();
		notCompleteWhiteSet = whitePawns.getNotCompleteRace();
		drawAddablePawns(g, Color.BLACK, notCompleteBlackSet, 0, 0,
				toBeAddedBlackRace);
		drawAddablePawns(
				g,
				Color.WHITE,
				notCompleteWhiteSet,
				0,
				(int) (HyveFrame.FRAME_HEIGHT - HEIGHT_PAWN - LOST_SPACE_ON_THE_BOTTOM),
				toBeAddedWhiteRace);
		for (Box[] boxArray : board.getGrid()) {
			for (Box b : boxArray) {
				if (b != null && b.shouldBeDisplayed()) {
					drawBox(g, b);
				}
			}
		}
	}

	/**
	 * TODO refactor to have call only one time drawPawn
	 */
	private void drawBox(Graphics g, Box b) {
		if (b != null && b.shouldBeDisplayed()) {
			Pawn topPawn = b.getFirstPawn();
			int xOffset = computeXOffset(b.getX());
			int yOffset = computeYOffset(b.getY());

			if (topPawn != null) {
				if (b.containsAnOption()) {
					drawPawn(g, Color.YELLOW, topPawn.toString(), xOffset,
							yOffset, true);
				} else if (b.equals(selectedBox)) {
					drawPawn(g, topPawn.getColor().getAwtColor(),
							topPawn.toString(), xOffset, yOffset, true);
				} else {
					drawPawn(g, topPawn.getColor().getAwtColor(),
							topPawn.toString(), xOffset, yOffset, false);
				}
			} else {
				// this Box is only a option to add a pawn
				// we paint it in yellow.
				drawPawn(g, Color.YELLOW, "", xOffset, yOffset, false);
			}
		}
	}

	private int computeXOffset(int x) {
		// We want first black box to be in the middle and we end up with the
		// function using the fact that 2 pawns with a difference of 2 for the
		// abscisses have a distance from (2 * HyvePanel.WIDTH_PAWN - 2 * x1)
		// between them.
		return (x - Box.COORD_1_FIRST_BLACK_BOX) * X_SPACE_BETWEEN_BOXES
				+ X_FIRST_BOX;
	}

	private int computeYOffset(int y) {
		// We want first black box to be in the middle and for one difference in
		// the y distance is HyvePanel.HEIGHT_PAWN/2.
		return (y - Box.COORD_2_FIRST_BLACK_BOX) * HyvePanel.HEIGHT_PAWN / 2
				+ Y_FIRST_BOX;
	}

	private void drawAddablePawns(Graphics g, Color c, Set<Race> races, int i,
			int j, Race selectedRace) {
		int n = 0;
		for (Race r : races) {
			boolean isSelected = (selectedRace != null && selectedRace == r);
			drawPawn(g, c, r.getFullName(), i + n * WIDTH_PAWN, j, isSelected);
			n++;
		}
	}

	/**
	 * @param g
	 * @param c
	 * @param name
	 * @param xOffset
	 * @param yOffset
	 * @param isSelected
	 *            would be <code>true</code> only if the user has clicked on
	 *            this pawn.
	 */
	private void drawPawn(Graphics g, Color c, String name, int xOffset,
			int yOffset, boolean isSelected) {
		int[] xPointsWithOffset = addOffsetToArray(xPointsStandard, xOffset);
		int[] yPointsWithOffset = addOffsetToArray(yPointsStandard, yOffset);
		if (isSelected) {
			g.setColor(Color.YELLOW);
		} else {
			g.setColor(c);
		}
		g.fillPolygon(xPointsWithOffset, yPointsWithOffset, 6);
		g.setColor(Color.RED);
		g.drawString(name, 30 + xOffset, HEIGHT_PAWN / 2 + yOffset);
	}

	private int[] addOffsetToArray(int[] refArray, int offset) {
		int length = refArray.length;
		int[] res = new int[length];
		for (int i = 0; i < length; i++) {
			res[i] = refArray[i] + offset;
		}
		return res;
	}

	/**
	 * @return the selectedBox
	 */
	public Box getSelectedBox() {
		return selectedBox;
	}

	private Race getRaceFromIndex(int index, Set<Race> raceSet) {
		int i = 0;
		for (Race r : raceSet) {
			if (i == index) {
				return r;
			}
			i++;
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		cleanPreviousSelection();
		int x = e.getX();
		int y = e.getY();
		if (y < HEIGHT_PAWN) {
			int toBeAddedBlackIndex = x / WIDTH_PAWN;
			toBeAddedBlackRace = getRaceFromIndex(toBeAddedBlackIndex,
					notCompleteBlackSet);
		} else if (y > HyveFrame.FRAME_HEIGHT
				- (HEIGHT_PAWN + LOST_SPACE_ON_THE_BOTTOM)) {
			int toBeAddedWhiteIndex = x / WIDTH_PAWN;
			toBeAddedWhiteRace = getRaceFromIndex(toBeAddedWhiteIndex,
					notCompleteWhiteSet);
		} else {

			int xRelative = x - X_FIRST_BOX;
			int positionRelativeInTheBox = (xRelative < 0) ? (X_SPACE_BETWEEN_BOXES - (-xRelative)
					% X_SPACE_BETWEEN_BOXES)
					% X_SPACE_BETWEEN_BOXES
					: xRelative % X_SPACE_BETWEEN_BOXES;
			System.out.println(positionRelativeInTheBox + " " + x1 + " "
					+ X_SPACE_BETWEEN_BOXES + " " + x + " " + y);
			if (positionRelativeInTheBox > x1) {
				int i = Box.COORD_1_FIRST_BLACK_BOX;
				if (xRelative >= 0) {
					i += xRelative / X_SPACE_BETWEEN_BOXES;
				} else {
					i -= 1 - xRelative / X_SPACE_BETWEEN_BOXES;
				}
				int j = Box.COORD_2_FIRST_BLACK_BOX;
				int yRelative = y - Y_FIRST_BOX;
				if (i % 2 == 0) {
					if (yRelative >= 0) {
						j += yRelative / HEIGHT_PAWN * 2;
					} else {
						j -= 2 - yRelative / HEIGHT_PAWN * 2;
					}
				} else {
					// i % 2 == 1
					int yRelativeCaseXOdd = yRelative - HEIGHT_PAWN / 2;
					if (yRelativeCaseXOdd >= 0) {
						j += yRelativeCaseXOdd / HEIGHT_PAWN * 2 + 1;
					} else {
						j -= -yRelativeCaseXOdd / HEIGHT_PAWN * 2 + 1;
					}
				}

				selectedBox = Game.getBoard().getGrid()[i][j];
				System.out.println(i + " " + j);
			} else {
				System.out.println("click was in the middle of 2 boxes!");
			}

		}
		this.notifyObserver();
	}

	public void cleanPreviousSelection() {
		toBeAddedBlackRace = null;
		toBeAddedWhiteRace = null;
		selectedBox = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addObserver(Observer obs) {
		this.observer = obs;
	}

	@Override
	public void notifyObserver() {
		// Not more than one of toBeAddedBlackRace, toBeAddedWhiteRace can be
		// not null.
		if (toBeAddedBlackRace != null) {
			this.observer.update(toBeAddedBlackRace);
		} else if (toBeAddedWhiteRace != null) {
			this.observer.update(toBeAddedWhiteRace);
		}
		if (selectedBox != null) {
			System.out.println("I was here!!!!!!!!!!!" + selectedBox.getX()
					+ " " + selectedBox.getY() + " option "
					+ selectedBox.toString());
			this.observer.update(selectedBox);
		}
	}
}
