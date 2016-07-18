package utils;

public enum Color {
	BLACK("B", java.awt.Color.BLACK), WHITE("W", java.awt.Color.WHITE);

	private String shortName;
	private java.awt.Color awtColor;

	private Color(String shortName, java.awt.Color c) {
		this.shortName = shortName;
		awtColor = c;
	}

	public String getShortName() {
		return shortName;
	}

	/**
	 * @return the awtColor
	 */
	public java.awt.Color getAwtColor() {
		return awtColor;
	}
}
