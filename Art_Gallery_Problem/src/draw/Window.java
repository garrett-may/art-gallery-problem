package draw;

import java.awt.Dimension;

import javax.swing.JFrame;
import main.Museum;

public class Window extends JFrame {

	private static final long serialVersionUID = 3881539186925934796L;

	private final Canvas canvas;
	
	public Window() {
		super.setPreferredSize(new Dimension(800, 800));
		this.canvas = new Canvas(new Dimension(800, 800));

		super.setContentPane(canvas);
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.pack();
		super.setVisible(true);
	}
	
	public void drawMuseumRoom(Museum museumRoom) {
		canvas.museumRoom = museumRoom;
		canvas.updateUI();	
	}
	
	public void clear() {
		canvas.museumRoom = null;
	}
}
