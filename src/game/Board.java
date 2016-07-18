package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import pawn.Pawn;
import utils.Box;
import utils.Color;
import utils.DirectionsToGetANeighbor;
import utils.Race;

public class Board {
	private int size;

	/**
	 * the min x or y of an occupied box (use for display.)
	 */
	private int min;

	/**
	 * the max x or y of an occupied box (use for display.)
	 */
	private int max;
	/**
	 * the number of beetles which are on top of some other pawns. We need this
	 * info to know the number of occupied box. We need to be careful to
	 * increment this value each time a beetle is move from a bottom position to
	 * a upper position.
	 */
	// private int nbNotBottomBeetles;

	private Box[][] grid;
	private PlayerPawns blackPawns = new PlayerPawns();
	private PlayerPawns whitePawns = new PlayerPawns();

	public Board(int i) {
		size = i;
		this.grid = new Box[size][size];
		min = size / 2;
		max = size / 2 + 2;
	}

	/**
	 * @return the board
	 */
	public Box[][] getGrid() {
		return grid;
	}

	/**
	 * @param board
	 *            the board to set
	 */
	public void setBoard(Box[][] board) {
		this.grid = board;
	}

	/**
	 * @return the nbOfEachRace
	 */
	public int getNbOf(Race r, Color c) {
		Integer res = null;
		switch (c) {
		case BLACK:
			res = this.blackPawns.getNbOf(r);
			break;

		case WHITE:
			res = this.whitePawns.getNbOf(r);
			break;
		}
		if (res == null)
			return 0;
		else
			return res;
	}

	/** increase the size of the displayed board if needed */
	private void increaseSize(Box b) {
		if (b != null) {
			int x = b.getX();
			int y = b.getY();
			if (x < min || y < min) {
				min = Math.min(x, y);
			}
			if (x > max || y > max) {
				max = Math.max(x, y);
			}
		}
	}

	/**
	 * add a pawn of the given {@link Race} and {@link Color} at the given
	 * {@link Box} location.
	 */
	public void add(Race r, Color c, Box b) {
		if (r == Race.Queen) {
			this.getPlayerPawns(c).setQueenLocalisation(b);
		}
		increaseSize(b);
		Pawn newPawn = new Pawn(r, c, b);
		switch (c) {
		case BLACK:
			this.blackPawns.addPawn(newPawn);
			break;

		case WHITE:
			this.whitePawns.addPawn(newPawn);
			break;
		}
		b.addPawn(newPawn);
		grid[b.getX()][b.getY()] = b;
	}

	/** 2 first moves case, the user do not choose where he will add the box. */
	public void add(Race race, Color c) {
		switch (c) {
		case BLACK:
			add(race, c, Box.firstBlackBox);
			break;
		case WHITE:
			add(race, c, Box.firstWhiteBox);
			break;
		}
	}

	/**
	 * add a box which can be chosen to contain a pawn to be added or in move
	 * case a box who contains a movable pawn.
	 */
	private void add(Box b, int availableBoxNumber) {
		b.setOption(availableBoxNumber);
	}

	public HashMap<Integer, Box> getAndMarkFreeSpots(Color c) {
		PlayerPawns currentPlayerPawns = getPlayerPawns(c);
		PlayerPawns opponentPawns = getOpponentPawns(c);
		Set<Box> neighbors = currentPlayerPawns.getNeighbors(this.getGrid());
		removeForbiddenSpots(opponentPawns, neighbors);
		HashMap<Integer, Box> optionsMap = createOptionsMapFromPossibleBox(neighbors);
		return optionsMap;
	}

	/**
	 * creates an hash map with all the possible {@link Box} to be moved or
	 * added.
	 */
	private HashMap<Integer, Box> createOptionsMapFromPossibleBox(
			Set<Box> neighbors) {
		int availableBoxNumber = 1;
		HashMap<Integer, Box> optionsMap = new HashMap<Integer, Box>();
		for (Box b : neighbors) {
			add(b, availableBoxNumber);
			optionsMap.put(availableBoxNumber++, b);
		}
		return optionsMap;
	}

	/**
	 * remove from the possible spots all the spots neighbors to the opponent
	 * and all the spots already occupied.
	 */
	private void removeForbiddenSpots(PlayerPawns opponentPawns,
			Set<Box> neighbors) {
		Set<Box> forbiddenNeighbors = opponentPawns
				.getNeighbors(this.getGrid());
		Set<Pawn> allPawnsInPlay = getAllPawnsInPlay();
		for (Pawn p : allPawnsInPlay) {
			forbiddenNeighbors.add(p.getLocalisation());
		}

		for (Box b : forbiddenNeighbors) {
			neighbors.remove(b);
		}
	}

	public Set<Pawn> getAllPawnsInPlay() {
		Set<Pawn> allPawnsInPlay = new HashSet<Pawn>(blackPawns.getPawns());
		allPawnsInPlay.addAll(whitePawns.getPawns());
		return allPawnsInPlay;
	}

	private Set<Box> getAllBoxesWithContent() {
		Set<Box> allOccupiedBoxes = new HashSet<Box>();
		for (Pawn p : this.getAllPawnsInPlay()) {
			allOccupiedBoxes.add(p.getLocalisation());
		}
		return allOccupiedBoxes;
	}

	private PlayerPawns getOpponentPawns(Color c) {
		PlayerPawns currentPlayerPawns = null;
		switch (c) {
		case BLACK:
			currentPlayerPawns = whitePawns;
			break;

		case WHITE:
			currentPlayerPawns = blackPawns;
			break;
		}
		return currentPlayerPawns;
	}

	public PlayerPawns getPlayerPawns(Color c) {
		PlayerPawns playerPawns = null;
		switch (c) {
		case BLACK:
			playerPawns = blackPawns;
			break;

		case WHITE:
			playerPawns = whitePawns;
			break;
		}
		return playerPawns;
	}

	/**
	 * set all the options back to 0.
	 */
	public void removeOptions(HashMap<Integer, Box> possibleAddOrMove) {
		for (Entry<Integer, Box> e : possibleAddOrMove.entrySet()) {
			Box b = e.getValue();
			b.setOption(null);
		}
	}

	/** return <code>true</code> if queen of this color is on the board. */
	public boolean isQueenOnTheGame(Color c) {
		int nbOfQueen = getNbOf(Race.Queen, c);
		switch (nbOfQueen) {
		case 1:
			return true;
		case 0:
			return false;
		default:
			throw new RuntimeException("cheater!!! you have " + nbOfQueen);
		}
	}

	/**
	 * return <code>true</code> if the board is connex if we remove the
	 * {@link Pawn} given as argument?
	 */
	public boolean isConnexWithout(Pawn p) {
		// in case of a beatle on top of other pawns, for sure the board is
		// connex without it.
		if (p.getLocalisation().getPawns().size() > 1) {
			return true;
		}
		Set<Pawn> allPawnsInPlay = getAllPawnsInPlay();
		allPawnsInPlay.remove(p);
		Set<Pawn> alreadyInspected = new HashSet<Pawn>();
		Pawn currentPawn = (Pawn) allPawnsInPlay.toArray()[0];
		Set<Pawn> toBeProcessed = new HashSet<Pawn>();
		toBeProcessed.add(currentPawn);

		do {
			alreadyInspected.add(currentPawn);
			toBeProcessed.remove(currentPawn);
			Set<Pawn> neighbors = currentPawn.getNeighbors(this.getGrid());
			for (Pawn n : neighbors) {
				if (!alreadyInspected.contains(n) && !n.equals(p)) {
					toBeProcessed.add(n);
				}
			}
			if (toBeProcessed.size() == 0) {
				break;
			}
			currentPawn = (Pawn) toBeProcessed.toArray()[0];
		} while (true);

		if (alreadyInspected.size() == allPawnsInPlay.size()) {
			return true;
		}
		return false;
	}

	// /**
	// * @return the nbOccupiedBox
	// */
	// public int getNbOccupiedBox() {
	// return getAllPawnsInPlay().size() - nbNotBottomBeetles;
	// }

	// /**
	// * @param increment
	// * nbNotBottomBeetles.
	// */
	// public void incrementNbNotBottomBeetles() {
	// this.nbNotBottomBeetles++;
	// }

	/**
	 * get a {@link HashMap} containing all the movable pawns for the color
	 * given in input.
	 */
	public HashMap<Integer, Box> getMovableBoxes(Color c) {
		Set<Box> movableBoxes = new HashSet<Box>();
		for (Pawn p : this.getPlayerPawns(c).getPawns()) {
			if (this.isConnexWithout(p)) {
				movableBoxes.add(p.getLocalisation());
			}
		}
		return createOptionsMapFromPossibleBox(movableBoxes);
	}

	public HashMap<Integer, Box> markAndGetPossibleDestinations(Pawn movedPawn) {
		Box originBox = movedPawn.getLocalisation();
		Set<Box> possibleDestinations = new HashSet<Box>();
		Set<Box> allBoxesInPlay = getAllBoxesWithContent();
		// we dont want to get the neighbors of the moved pawn (except if
		// there were more than one pawn on this box.)
		if (originBox.getPawns().size() <= 1) {
			allBoxesInPlay.remove(originBox);
		}
		Set<Box> allFreeNeighbors = getAllFreeNeighbors(allBoxesInPlay);
		switch (movedPawn.getRace()) {
		case Ant:
			// all free neighbors are a possible destination.
			possibleDestinations = allFreeNeighbors;
			possibleDestinations.remove(originBox);
			break;
		case Beetle:
			Set<Box> allowedValues = new HashSet<Box>(allFreeNeighbors);
			allowedValues.addAll(allBoxesInPlay);
			possibleDestinations = filter(originBox.getNeighbors(grid),allowedValues);
			break;
		case Queen:
			possibleDestinations = filter(originBox.getEmptyNeighbors(grid),
					allFreeNeighbors);
			break;
		case Grasshoper:
			for (DirectionsToGetANeighbor d : DirectionsToGetANeighbor.values()) {
				Box nextFreeSpot = originBox.getNextFreeSpot(grid, d);
				if (nextFreeSpot != null)
					possibleDestinations.add(nextFreeSpot);
			}
			break;
		case Spider:
			// Box reachable after one move.
			Set<Box> oneMove = filter(originBox.getEmptyNeighbors(grid),
					allFreeNeighbors);
			// Box reachable after two moves.
			Set<Box> twoMoves = new HashSet<Box>();
			for (Box b : oneMove) {
				twoMoves.addAll(b.getEmptyNeighbors(grid));
			}
			twoMoves.remove(originBox);
			twoMoves = filter(twoMoves, allFreeNeighbors);
			// Box reachable after three moves.
			Set<Box> threeMoves = new HashSet<Box>();
			for (Box b : twoMoves) {
				threeMoves.addAll(b.getEmptyNeighbors(grid));
			}
			threeMoves.removeAll(oneMove);
			possibleDestinations = filter(threeMoves, allFreeNeighbors);
			break;
		default:
			throw new RuntimeException("this race was not yet implemented.");
		}
		return createOptionsMapFromPossibleBox(possibleDestinations);
	}

	/**
	 * @param toBeFiltered
	 * @param allowedValues
	 * @return a set containing all allowed values from toBeFiltered.
	 */
	private <T> Set<T> filter(Set<T> toBeFiltered, Set<T> allowedValues) {
		Set<T> filteredSet = new HashSet<T>();
		for (T t : toBeFiltered) {
			if (allowedValues.contains(t)) {
				filteredSet.add(t);
			}
		}
		return filteredSet;
	}

	private Set<Box> getAllFreeNeighbors(Set<Box> allBoxesInPlay) {
		Set<Box> allFreeNeighbors = new HashSet<Box>();
		for (Box currentBox : allBoxesInPlay) {
			Set<Box> currentNeighbors = currentBox.getEmptyNeighbors(grid);
			allFreeNeighbors.addAll(currentNeighbors);
		}
		return allFreeNeighbors;
	}

	public boolean isQueenSurrounded(Color c) {
		PlayerPawns currentPlayerPawns = getPlayerPawns(c);
		Box queenBox = currentPlayerPawns.getQueenLocalisation();
		if (queenBox == null)
			return false;
		Set<Box> queenNeighbors = queenBox.getNotEmptyNeighbors(grid);
		if (queenNeighbors.size() == 6)
			return true;
		return false;
	}
}
