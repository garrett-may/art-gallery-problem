package geometry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static java.lang.Math.*;

public class Point {

	public double x;
	public double y;
	public int sign;
	public Segment segment;
	public double angle;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}	
	
	public void paintComponent(Graphics graphics, MuseumRoom museumRoom, Dimension canvasDimension) {
		List<Point> guardPoint = Arrays.asList(new Point[] {this});
		List<Point> viewPoints = MuseumChecker.computeVisibilityPolygon(museumRoom.room, this);
		int[][] guard = MuseumChecker.doMagicMappingToCanvas(guardPoint, museumRoom, canvasDimension);
		int[][] points = MuseumChecker.doMagicMappingToCanvas(viewPoints, museumRoom, canvasDimension);
		
		graphics.setColor(Color.YELLOW);
		graphics.fillPolygon(points[0], points[1], viewPoints.size());
		graphics.setColor(Color.ORANGE);
		for(int i = 0; i < points[0].length; i++) {
			//graphics.fillOval(points[0][i] - 5, points[1][i] - 5, 10, 10);
		}
		graphics.setColor(Color.BLUE);
		graphics.fillOval(guard[0][0] - 5, guard[1][0] - 5, 10, 10);		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Point)) {
			return false;
		}
		Point that = (Point) obj;
		return this.x == that.x && this.y == that.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
