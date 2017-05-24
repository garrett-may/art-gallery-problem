package geometry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MuseumRoom {

	public final List<Point> room;
	public final Set<Point> guards;
	
	public MuseumRoom() {
		this.room = new ArrayList<>();
		this.guards = new HashSet<>();
	}
	
	public void paintComponent(Graphics graphics, Dimension canvasDimension) {
		int[][] points = MuseumChecker.doMagicMappingToCanvas(room, this, canvasDimension);
		graphics.fillPolygon(points[0], points[1], room.size());
		/*List<Triangle> triangles = MuseumChecker.triangulation(room);
		graphics.setColor(Color.MAGENTA);
		for(Triangle triangle : triangles) {
			int[][] triangleCoords = MuseumChecker.doMagicMappingToCanvas(triangle, this, canvasDimension);
			graphics.drawPolygon(triangleCoords[0], triangleCoords[1], triangleCoords[0].length);
		}*/
		for(Point guard : guards) {
			guard.paintComponent(graphics, this, canvasDimension);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		builder.append("Room: ");
		builder.append(room.size() == 0 ? "empty" : room.get(0));
		for(int i = 1; i < room.size(); i++) {
			builder.append(", ");
			builder.append(room.get(i));
		}
		builder.append("\n");
		
		Iterator<Point> iterator = guards.iterator();
		builder.append("Guards: ");		
		builder.append(!iterator.hasNext() ? "none" : iterator.next());
		while(iterator.hasNext()) {
			builder.append(", ");
			builder.append(iterator.next());
		}
		
		return builder.toString();
	}
}
