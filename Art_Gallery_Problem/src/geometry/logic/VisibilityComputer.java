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
	
	private static int prev(int i, int size) {
		return i <= 0 ? i - 1 + size : i - 1;
	}
	
	private static int next(int i, int size) {
		return i >= size - 1 ? i + 1 - size : i + 1;
	}
	
	private static double boundAngle(double angle, double lower, double upper) {
		while(angle < lower) {
			angle += 2 * Math.PI;
		}
		while(angle >= upper) {
			angle -= 2 * Math.PI;
		}
		return angle;
	}
	
	private static List<Ray> createScanLines(List<Point> points, Point guard) {
		List<Ray> scanLines = new ArrayList<>();
		// Rays to shot everywhere
		double startAngle = 0;
		double endAngle = 2 * Math.PI;
		double step = endAngle / 11520;
		for(double angle = startAngle; angle < endAngle; angle += step) {
			scanLines.add(new Ray(guard, angle));
		}
		
		// Ray to the guard
		boolean guardInPoints = false;
		int guardIndex = -1;
		double[] angleMap = new double[points.size()];
		for(int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			if(point.equals(guard)) {
				guardInPoints = true;
				guardIndex = i;
			} else {
				double angle = boundAngle(new Line(guard, point).angle(), 0, 2 * Math.PI);
				angleMap[i] = angle;
			}			
		}
		if(guardInPoints) {
			double a1 = angleMap[prev(guardIndex, points.size())];
			double a2 = angleMap[next(guardIndex, points.size())];	
			while(a2 < a1) {
				a2 += 2 * Math.PI;
			}
			scanLines.add(new Ray(guard, guard, (a2 + a1) / 2));
		}
		
		// Re-sort
		scanLines.sort((x, y) -> Double.compare(x.angle, y.angle));
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
			if(scanLine.origin.equals(scanLine.target)) {
				points.add(scanLine.origin);
			} else {
				List<Point> intersections = getIntersections(scanLine, polygon.segments);
				intersections.sort((x, y) -> Double.compare(scanLine.length(x), scanLine.length(y)));
				if(intersections.size() > 0) {
					if(rayFlyingOutOfPolygon(polygon, scanLine.origin, intersections.get(0))) {
						
					} else {
						points.add(intersections.get(0));
						if(intersections.get(0).x == 10.000000000002448) {
							System.out.println(scanLine);
							System.out.println("Midpoint: " + new Line(scanLine.origin, intersections.get(0)).interpolate(0.5));
							System.out.println(polygon.contains(new Line(scanLine.origin, intersections.get(0)).interpolate(0.5)));
						}
					}				
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
		List<Ray> scanLines = createScanLines(room.points, guard);
		List<Point> polygon = new ArrayList<>();
		if(room.points.contains(guard)) {
			//polygon.add(guard);
		}
		polygon.addAll(getIntersectionPoints(room, scanLines));
		polygon = optimise(polygon);
		return polygon;
	}
}
