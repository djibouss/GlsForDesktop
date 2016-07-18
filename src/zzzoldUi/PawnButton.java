package zzzoldUi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import utils.Box;

public class PawnButton extends CustomButton {
	public final static int WIDTH = 120;
	// after calculation in a perfect hexagon.
	public final static int HEIGHT = (int) ((Math.sqrt(3.0) / 2) * WIDTH);
	private Color color;

	public PawnButton(Box b) {
		super();
		if (b != null) {
			name = b.toString();
			color = b.getFirstPawn().getColor().getAwtColor();
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(getParent().getBackground());
		g2D.fillRect(0, 0, WIDTH, HEIGHT);
		// if (hit == true) {
		// g2D.setColor(Color.yellow);
		// }
		// else{
		// g2D.setColor(Color.WHITE);
		// }
		g2D.setColor(color);

		int x1 = (int) (HEIGHT / (2.0 * Math.sqrt(3.0)));
		int[] xPoints = { 0, x1, WIDTH - x1, WIDTH, WIDTH - x1, x1 };
		int[] yPoints = { HEIGHT / 2, 0, 0, HEIGHT / 2, HEIGHT, HEIGHT };

		g2D.fillPolygon(xPoints, yPoints, 6);
		g2D.setColor(Color.black);
		g2D.drawPolygon(xPoints, yPoints, 6);
		g2D.drawString(name, 30, HEIGHT / 2);

	}

}
