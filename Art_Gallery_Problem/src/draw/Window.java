package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import geometry.types.Triangle;
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
		try {
			Thread.sleep(100);
		} catch(Exception ex) {
			
		}
		for(int i = 0; i < museumRoom.room.triangles.size(); i++) {
			Triangle triangle = museumRoom.room.triangles.get(i);
			JPanel panel = new JPanel() {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void paintComponent(Graphics graphics) {
					graphics.setColor(Color.MAGENTA);
					Drawing.drawTriangle(graphics, triangle, museumRoom, getSize());
					graphics.setColor(Color.GREEN);
					Drawing.drawPoint(graphics, triangle.p2, museumRoom, getSize());
				}
				
			};
			super.setContentPane(panel);
			panel.updateUI();
			try {
				Thread.sleep(50);
			} catch(Exception ex) {
				
			}
		}		
	}
	
	public void clear() {
		canvas.museumRoom = null;
	}
}
