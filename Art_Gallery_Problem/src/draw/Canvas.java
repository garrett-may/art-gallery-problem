package draw;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Museum;

public class Canvas extends JPanel {

	private static final long serialVersionUID = -6547418658972390702L;

	public Museum museumRoom;
	
	public Canvas(Dimension dimension) {
		this.museumRoom = null;
		
		super.setPreferredSize(dimension);
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponents(graphics);
		if(museumRoom != null) {
			Drawing.drawMuseum(graphics, museumRoom, getSize());
		} else {
			graphics.clearRect(0, 0, getWidth(), getHeight());
		}
	}	
}
