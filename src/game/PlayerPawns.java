package game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import pawn.Pawn;

import utils.Box;
import utils.Race;

public class PlayerPawns {
	// contains all the pawns (which contains the location in the board)
	private Set<Pawn> pawns = new HashSet<Pawn>();
	/**
	 * counts the occurrence of each race, will never contain any Race with 0 as
	 * occurrence
	 */
	private HashMap<Race, Integer> nbOfEachRace = new HashMap<Race, Integer>();
	/**
	 * {@link Set} which contains all the {@link Race} which can still be added
	 * for this player.
	 */
	private Set<Race> notCompleteRace = new HashSet<Race>(Arrays.asList(Race
			.values()));

	/**
	 * We want to keep track of the queen localization for each player.
	 * <code>null</code> if the queen was not yet played.
	 */
	private Box queenLocalisation;

	public Set<Race> getNotCompleteRace() {
		return notCompleteRace;
	}

	/**
	 * @return the Pawns
	 */
	public Set<Pawn> getPawns() {
		return pawns;
	}

	/**
	 * @param pawn
	 *            the pawn to set
	 */
	public void addPawn(Pawn p) {
		this.pawns.add(p);
		// each time we add a pawn, we have to increment its number of
		// occurence.
		Race race = p.getRace();
		incrementNbOf(race);
		if (getNbOf(race) == race.getMaxAllowed()) {
			notCompleteRace.remove(race);
		}

	}

	/**
	 * @return the nbOfEachRace
	 */
	public Integer getNbOf(Race r) {
		return this.nbOfEachRace.get(r);
	}

	/**
	 * @param nbOfEachRace
	 *            the nbOfEachRace to set
	 */
	private void incrementNbOf(Race r) {
		Integer nb = this.nbOfEachRace.get(r);
		if (nb == null) {
			this.nbOfEachRace.put(r, 1);
		} else {
			this.nbOfEachRace.put(r, ++nb);
		}
	}

	public Set<Box> getNeighbors(Box[][] grid) {
		Set<Box> neighbors = new HashSet<Box>();
		for (Pawn p : this.pawns) {
			Box currentBox = p.getLocalisation();
			Set<Box> currentNeighbors = currentBox.getNeighbors(grid);
			neighbors.addAll(currentNeighbors);
		}
		return neighbors;
	}

	/** return a set of all race on the board of the current player. */
	public Set<Race> getRaceOnBoard() {
		return nbOfEachRace.keySet();
	}

	/**
	 * @return the queenLocalisation
	 */
	public Box getQueenLocalisation() {
		return queenLocalisation;
	}

	/**
	 * @param queenLocalisation the queenLocalisation to set
	 */
	public void setQueenLocalisation(Box queenLocalisation) {
		this.queenLocalisation = queenLocalisation;
	}
}
