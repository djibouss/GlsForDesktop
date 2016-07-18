package zzzoldUi;
//file CustomButton.java  

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JPanel;

public abstract class CustomButton extends JPanel implements MouseListener {

	String name = null;
	private Vector listeners = null;
	boolean hit = false;


	public CustomButton() {
		super();
		listeners = new Vector();
		addMouseListener(this);
	}

	public CustomButton(String title) {
		this();
		this.name = title;

	}

	public Dimension getPreferredSize() {
		int W = 120;
		// after calculation in a perfect hexagon.
		int H = (int) ((Math.sqrt(3.0) / 2) * W);
		return new Dimension(W, H);
	}

	public abstract void paintComponent(Graphics g);

	public void mousePressed(MouseEvent e) {
		hit = true;
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		hit = false;
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		fireEvent(new ActionEvent(this, 0, name));
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void addActionListener(ActionListener listener) {
		listeners.addElement(listener);
	}

	public void removeActionListener(ActionListener listener) {
		listeners.removeElement(listener);
	}

	private void fireEvent(ActionEvent event) {
		for (int i = 0; i < listeners.size(); i++) {
			ActionListener listener = (ActionListener) listeners.elementAt(i);
			listener.actionPerformed(event);
		}
		;
	}

}// end class  