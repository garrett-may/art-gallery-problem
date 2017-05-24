package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import geometry.MuseumRoom;

public class Canvas extends JPanel {

	private static final long serialVersionUID = -6547418658972390702L;

	public MuseumRoom museumRoom;
	
	public Canvas(Dimension dimension) {
		this.museumRoom = null;
		
		super.setPreferredSize(dimension);
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponents(graphics);
		if(museumRoom != null) {
			museumRoom.paintComponent(graphics, super.getSize());
		} else {
			graphics.clearRect(0, 0, getWidth(), getHeight());
		}
	}
	
}
