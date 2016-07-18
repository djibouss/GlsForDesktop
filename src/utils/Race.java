package utils;

public enum Race {
	Ant("A", "Ant", 4), Beetle("B", "Beetle", 2), Grasshoper("G", "Grasshoper",
			2), Queen("Q", "Queen", 1), Spider("S", "Spider", 2);

	private String shortName;
	private String fullName;
	private int maxAllowed;

	private Race(String shortName, String fullName, int m) {
		this.shortName = shortName;
		this.fullName = fullName;
		this.maxAllowed = m;
	}

	public String getShortName() {
		return shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public static Race findValue(String input) {
		Race res = null;

		for (Race r : Race.values()) {
			if (r.getShortName().equalsIgnoreCase(input)) {
				res = r;
			}
		}

		return res;
	}

	/**
	 * @return the maxAllowed
	 */
	public int getMaxAllowed() {
		return maxAllowed;
	}

}
