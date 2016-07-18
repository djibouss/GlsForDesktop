package pawn;

import java.util.HashSet;
import java.util.Set;

import utils.Box;
import utils.Color;
import utils.Race;

public class Pawn {

	private Color color;

	private Box localisation;
	private Race race;



	public Pawn(Race race, Color color, Box b) {
		super();
		this.color = color;
		this.race = race;
		this.localisation = b;
	}


	/**
	 * @return the neighbors and all of them even if some are located on the
	 *         same box.
	 */
	public Set<Pawn> getNeighbors(Box[][] grid) {
		Set<Box> boxSet = this.localisation.getNotEmptyNeighbors(grid);
		Set<Pawn> pawnSet = new HashSet<Pawn>();
		for (Box b : boxSet) {
			pawnSet.addAll(b.getPawns());
		}
		return pawnSet;
	}


	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the localisation
	 */
	public Box getLocalisation() {
		return localisation;
	}

	/**
	 * @param localisation
	 *            the localisation to set
	 */
	public void setLocalisation(Box localisation) {
		this.localisation = localisation;
	}


	/**
	 * @return the race
	 */
	public Race getRace() {
		return race;
	}

	public String toString() {
		// Color not needed any more in swing.
		return getRace().getFullName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result
				+ ((localisation == null) ? 0 : localisation.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
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
		Pawn other = (Pawn) obj;
		if (color != other.color)
			return false;
		if (localisation == null) {
			if (other.localisation != null)
				return false;
		} else if (!localisation.equals(other.localisation))
			return false;
		if (race != other.race)
			return false;
		return true;
	}

}
