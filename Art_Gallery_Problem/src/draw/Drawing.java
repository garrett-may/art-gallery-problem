package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import geometry.logic.MissingAreasComputer;
import geometry.types.Line;
import geometry.types.Point;
import geometry.types.Polygon;
import geometry.types.Triangle;
import main.Guard;
import main.Museum;

public final class Drawing {

	private Drawing() {
		
	}
	
	public static final void drawMuseum(Graphics graphics, Museum museumRoom, Dimension canvasDimension) {
		graphics.setColor(Color.GRAY);
		drawPolygon(graphics, museumRoom.room, museumRoom, canvasDimension);
		graphics.setColor(Color.YELLOW);
		for(Guard guard : museumRoom.guards) {
			drawPoints(graphics, guard.guardView, museumRoom, canvasDimension);
		}
		graphics.setColor(Color.CYAN);
		for(Line line : museumRoom.room.segments) {
			drawLine(graphics, line, museumRoom, canvasDimension);
		}
		graphics.setColor(Color.BLUE);
		for(Guard guard : museumRoom.guards) {
			drawPoint(graphics, guard.point, museumRoom, canvasDimension);
		}			
		graphics.setColor(Color.RED);
		List<List<Point>> missingAreas = MissingAreasComputer.computeMissingAreas(
				museumRoom.room, museumRoom.guards.stream().map(g -> g.guardView).collect(Collectors.toSet()));
		for(List<Point> missingArea : missingAreas) {
			drawPoints(graphics, missingArea, museumRoom, canvasDimension);
		}
	}
	
	private static final void drawPoint(Graphics graphics, Point point, Museum museumRoom, Dimension canvasDimension) {
		List<Point> points = Arrays.asList(new Point[] {point});
		int[][] coords = convertToViewport(points, museumRoom, canvasDimension);
		double ratio = canvasDimension.width < canvasDimension.height ? canvasDimension.width : canvasDimension.height;
		int diameter = (int) (Math.log(ratio)); 
		graphics.fillOval(coords[0][0] - diameter / 2, coords[1][0] - diameter / 2, diameter, diameter);
	}
	
	private static final void drawLine(Graphics graphics, Line line, Museum museumRoom, Dimension canvasDimension) {
		int[][] coords = convertToViewport(line, museumRoom, canvasDimension);
		graphics.drawLine(coords[0][0], coords[1][0], coords[0][1], coords[1][1]);
	}
	
	private static final void drawPoints(Graphics graphics, List<Point> points, Museum museumRoom, Dimension canvasDimension) {
		int[][] coords = convertToViewport(points, museumRoom, canvasDimension);
		graphics.fillPolygon(coords[0], coords[1], points.size());		
	}
	
	private static final void drawTriangle(Graphics graphics, Triangle triangle, Museum museumRoom, Dimension canvasDimension) {
		int[][] coords = convertToViewport(triangle, museumRoom, canvasDimension);
		graphics.drawPolygon(coords[0], coords[1], coords[0].length);
	}
	
	private static final void drawPolygon(Graphics graphics, Polygon polygon, Museum museumRoom, Dimension canvasDimension) {
		drawPoints(graphics, polygon.points, museumRoom, canvasDimension);
	}
	
	private static final int[][] convertToViewport(Iterable<Point> points, Museum museumRoom, Dimension canvasDimension) {
		double xMin = 0;
		double yMin = 0;
		double xMax = 0;
		double yMax = 0;		
		for(Point point : museumRoom.room.points) {
			xMin = point.x < xMin ? point.x : xMin;
			yMin = point.y < yMin ? point.y : yMin;
			xMax = point.x > xMax ? point.x : xMax;
			yMax = point.y > yMax ? point.y : yMax;
		}
		
		double bound = 0.03;
		double ratio = canvasDimension.width < canvasDimension.height ? canvasDimension.width : canvasDimension.height;
				
		List<Point> newPoints = new ArrayList<>();
		for(Point point : points) {
			Point newPoint = new Point(
								ratio * ((point.x - xMin) / (xMax - xMin) * (1 - bound * 2) + bound), 
								ratio * ((point.y - yMin) / (yMax - yMin) * (1 - bound * 2) + bound));
			newPoints.add(newPoint);
		}
		
		int size = newPoints.size();
		int[] xCoords = new int[size];
		int[] yCoords = new int[size];
		for(int i = 0; i < size; i++) {
			Point point = newPoints.get(i);
			xCoords[i] = (int) point.x;
			yCoords[i] = (int) point.y;
		}
		
		return new int[][] {xCoords, yCoords};
	}	
}
