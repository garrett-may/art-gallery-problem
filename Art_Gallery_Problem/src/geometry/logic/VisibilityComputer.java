package geometry.logic;

import java.util.ArrayList;
import java.util.List;

import geometry.types.Line;
import geometry.types.Point;
import geometry.types.Polygon;
import geometry.types.Ray;

public final class VisibilityComputer {

	private VisibilityComputer() {
		
	}
	
	private static List<Ray> createScanLines(Point guard) {
		List<Ray> scanLines = new ArrayList<>();
		double startAngle = 0;
		double endAngle = 2 * Math.PI;
		double step = endAngle / 11520;
		for(double angle = startAngle; angle < endAngle; angle += step) {
			scanLines.add(new Ray(guard, angle));
		}
		return scanLines;
	}
	
	private static List<Point> getIntersections(Ray scanLine, List<Line> sceneLines) {
		List<Point> list = new ArrayList<>();
		for(Line line : sceneLines) {
			Point intersection = scanLine.intersection(line);
			if(intersection != null) {
				list.add(intersection);
			}
		}
		return list;
	}
	
	private static boolean rayFlyingOutOfPolygon(Polygon polygon, Point origin, Point target) {
		Point midpoint = new Line(origin, target).interpolate(0.5);
		return !polygon.contains(midpoint);
	}
	
	private static List<Point> getIntersectionPoints(Polygon polygon, List<Ray> scanLines) {
		List<Point> points = new ArrayList<>();
		for(Ray scanLine : scanLines) {
			List<Point> intersections = getIntersections(scanLine, polygon.segments);
			intersections.sort((x, y) -> Double.compare(scanLine.length(x), scanLine.length(y)));
			if(intersections.size() > 0) {
				if(rayFlyingOutOfPolygon(polygon, scanLine.origin, intersections.get(0))) {
					
				} else {
					points.add(intersections.get(0));
				}				
			}
		}
		return points;
	}
	
	private static List<Point> optimise(List<Point> polygon) {
		List<Point> optimisedPolygon = new ArrayList<>();
		for(int i = 0; i < polygon.size(); i++) {
			int j = (i - 1 + polygon.size()) % polygon.size();
			int k = (i + 1) % polygon.size();
			if(!new Line(polygon.get(j), polygon.get(k)).contains(polygon.get(i))) {
				optimisedPolygon.add(polygon.get(i));
			}
		}
		return optimisedPolygon;
	}
	
	public static List<Point> computeVisibilityPoints(Polygon room, Point guard) {
		List<Ray> scanLines = createScanLines(guard);
		List<Point> polygon = new ArrayList<>();
		if(room.points.contains(guard)) {
			polygon.add(guard);
		}
		polygon.addAll(getIntersectionPoints(room, scanLines));
		polygon = optimise(polygon);
		return polygon;
	}
}
