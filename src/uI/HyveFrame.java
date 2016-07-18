package uI;

import java.awt.HeadlessException;

import javax.swing.JFrame;

public class HyveFrame extends JFrame {

	public static final int FRAME_WIDTH = 1000;
	public static final int FRAME_HEIGHT = 700;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HyveFrame() throws HeadlessException {
		super("Hyve");
		this.setVisible(true);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
