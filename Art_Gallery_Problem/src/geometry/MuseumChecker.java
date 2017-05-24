package geometry;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.swing.text.Position;

import static java.lang.Math.*;
import static geometry.Geometry.*;

public class MuseumChecker {
	
	private MuseumChecker() {
		
	}
	
	/**
	 * For segment p1->p2, is p on the left of the segment or the right
	 * @param s
	 * @param p
	 * @return
	 */
	public static boolean leftOf(Segment s, Point p) {
		double cross = (s.p2.x - s.p1.x) * (p.y - s.p1.y) - (s.p2.y - s.p1.y) * (p.x - s.p1.x);
		return cross < 0;
	}
	
	public static Point interpolate(Point p, Point q, double f) {
		return new Point(p.x * (1 - f) + q.x * f, p.y * (1 - f) + q.y * f);
	}
	
	public static int sign(Point p1, Point p2, Point p3) {
		if(leftOf(new Segment(p1, p2), p3)) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public static boolean pointStrictlyInTriangle(Point v1, Point v2, Point v3, Point pt) {
		if(v1.equals(pt) || v2.equals(pt) || v3.equals(pt)) {
			return false;
		}
		boolean b1 = sign(pt, v1, v2) < 0;
		boolean b2 = sign(pt, v2, v3) < 0;
		boolean b3 = sign(pt, v3, v1) < 0;
		return (b1 == b2) && (b2 == b3); 
	}
	
	public static boolean pointInTriangle(Point v1, Point v2, Point v3, Point pt) {
		boolean b1 = sign(pt, v1, v2) < 0;
		boolean b2 = sign(pt, v2, v3) < 0;
		boolean b3 = sign(pt, v3, v1) < 0;
		return (b1 == b2) && (b2 == b3); 
	}
	
	public static boolean pointInTriangles(List<Triangle> triangles, Point point) {
		for(Triangle triangle : triangles) {
			if(pointInTriangle(triangle.p1, triangle.p2, triangle.p3, point)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean pointOnLineSegment(Point a, Point b, Point c) {
		double a1 = atan2(b.y - a.y, b.x - a.x);
		double a2 = atan2(c.y - a.y, c.x - a.x);
		if(a1 != a2) {
			return false;
		}
		if(distanceBetween(a, b) <= distanceBetween(a, c)) {
			return true;
		}
		return false;
	}		
	
	public static double distanceBetween(Point a, Point b) {
		return sqrt(pow(a.x - b.x, 2) + pow(a.y - b.y, 2));
	}
	
	public static int prev(int i, int size) {
		return i <= 0 ? i - 1 + size : i - 1;
	}
	
	public static int next(int i, int size) {
		return i >= size - 1 ? i + 1 - size : i + 1;
	}
	
	public static List<Point> copyOf(List<Point> points) {
		List<Point> newPoints = new ArrayList<>();
		for(Point point : points) {
			newPoints.add(new Point(point.x, point.y));
		}
		return newPoints;
	}
	
	public static boolean isReflexAngle(int i, List<Point> points) {
		Point prev = points.get(prev(i, points.size()));
		Point curr = points.get(i);
		Point next = points.get(next(i, points.size()));
		return leftOf(new Segment(prev, curr), next);
	}
	
	public static int findEarPoint(List<Point> points) {
		int size = points.size();
		for(int i = 0; i < size; i++) {
			if(!isReflexAngle(i, points)) {
				boolean concaveVertexFound = false;
				for(int j = 0; j < size; j++) {
					Point prev = points.get(prev(i, size));
					Point curr = points.get(i);
					Point next = points.get(next(i, size));
					Point that = points.get(j);
					if(isReflexAngle(j, points) && pointStrictlyInTriangle(prev, curr, next, that)) {
						concaveVertexFound = true;
					}
				}
				if(!concaveVertexFound) {
					return i;
				}
			}
		}
		return 0;
	}
		
	public static final List<Triangle> triangulation(List<Point> room) {
		List<Point> points = copyOf(room);
		List<Triangle> triangles = new ArrayList<>();
		while(points.size() > 2) {
			int i = findEarPoint(points);
			Point prev = points.get(prev(i, points.size()));
			Point curr = points.get(i);
			Point next = points.get(next(i, points.size()));
			Triangle triangle = new Triangle(prev, curr, next);
			triangles.add(triangle);
			points.remove(curr);
		}
		return triangles;
	}
	
	public static boolean pointInPolygon(List<Point> room, Point point) {
		List<Triangle> triangles = triangulation(room);
		for(Triangle triangle : triangles) {
			if(pointInTriangle(triangle.p1, triangle.p2, triangle.p3, point)) {
				return true;
			}
		}
		return false;
	}
	
	public static List<Point> gatherSortedIntersections(List<Segment> segments, Point origin, Point target) {
		List<Point> intersections = new ArrayList<>();
		for(Segment segment : segments) {
			Point intersection = rayIntersection(segment.p1, segment.p2, origin, target);
			if(intersection != null) {
				intersections.add(intersection);
			}
		}
		intersections.sort((x, y) -> Double.compare(distanceBetween(origin, x), distanceBetween(origin, y)));
		return intersections;
	}
	
	public static boolean rayFlyingOutOfPolygon(Point origin, Point target, List<Point> room) {
		Point midpoint = interpolate(origin, target, 0.5);
		return !pointInPolygon(room, midpoint);
	}
	
	public static List<Segment> createScanLines(List<Point> points, Point guard) {
		List<Segment> scanLines = new ArrayList<>();
		double startAngle = 0;
		double endAngle = 2*PI;
		double step = 2*PI / 11520;
		double distance = 10000;
		List<Point> targets = new ArrayList<>();
		for(double angle = startAngle; angle < endAngle; angle += step) {
			Point target = new Point(guard.x + cos(angle) * distance, guard.y + sin(angle) * distance);
			targets.add(target);
		}
		targets.sort((x, y) -> Double.compare(angle(guard, x), angle(guard, y)));
		for(Point target : targets) {
			scanLines.add(new Segment(guard, target));
		}
		return scanLines;
	}
	
	public static List<Point> getIntersections(Segment scanLine, List<Segment> sceneLines) {
		List<Point> list = new ArrayList<>();
		for(Segment line : sceneLines) {
			Point intersection = rayIntersection(line.p1, line.p2, scanLine.p1, scanLine.p2);
			if(intersection != null) {
				list.add(intersection);
			}
		}
		return list;
	}
	
	public static List<Point> getIntersectionPoints(List<Point> room, List<Segment> scanLines, List<Segment> sceneLines) {
		List<Point> points = new ArrayList<>();
		for(Segment scanLine : scanLines) {
			List<Point> intersections = getIntersections(scanLine, sceneLines);
			intersections.sort((x, y) -> Double.compare(distanceBetween(scanLine.p1, x), distanceBetween(scanLine.p1, y)));
			if(intersections.size() > 0) {
				if(rayFlyingOutOfPolygon(scanLine.p1, intersections.get(0), room)) {
					
				} else {
					points.add(intersections.get(0));
				}				
			}
		}
		return points;
	}
	
	public static List<Point> optimise(List<Point> polygon) {
		List<Point> optimisedPolygon = new ArrayList<>();
		for(int i = 0; i < polygon.size(); i++) {
			int j = (i - 1 + polygon.size()) % polygon.size();
			int k = (i + 1) % polygon.size();
			if(!pointOnLineSegment(polygon.get(j), polygon.get(i), polygon.get(k))) {
				optimisedPolygon.add(polygon.get(i));
			}
		}
		return optimisedPolygon;
	}
	
	public static List<Point> computeVisibilityPolygon(List<Point> room, Point guard) {
		List<Point> points = copyOf(room);
		List<Segment> sceneLines = toListSegments(points);
		List<Segment> scanLines = createScanLines(points, guard);
		List<Point> polygon = new ArrayList<>();
		if(room.contains(guard)) {
			polygon.add(guard);
		}
		polygon.addAll(getIntersectionPoints(points, scanLines, sceneLines));
		polygon = optimise(polygon);
		for(Point point : polygon) {
			System.out.println(point);
		}
		return polygon;
	}
	
	public static List<Point> shootRayAt(List<Point> room, Point origin, Point target) {
		List<Point> points = new ArrayList<>();
		List<Point> intersections = getIntersections(new Segment(origin, target), toListSegments(room));
		return points;
	}
	
	public static List<Point> toListPoint(List<Segment> segments) {
		List<Point> points = new ArrayList<>();
		for(Segment segment : segments) {
			points.add(segment.p1);
			points.add(segment.p2);
		}
		return points;
	}
	
	public static final List<Segment> toListSegments(List<Point> room) {
		List<Segment> segments = new ArrayList<>();
		int size = room.size();
		for(int i = 0; i < size; i++) {
			int j = (i + 1) % size;
			segments.add(new Segment(room.get(i), room.get(j)));
		}
		return segments;
	}
	
	public static final List<Segment> toListSegmentsWithReferences(List<Point> room) {
		List<Segment> segments = new ArrayList<>();
		int size = room.size();
		for(int i = 0; i < size; i++) {
			int j = (i + 1) % size;
			Point p1 = room.get(i);
			Point p2 = room.get(j);
			Segment segment = new Segment(p1, p2);
			p1.segment = segment;
			p2.segment = segment;
			segments.add(segment);
		}
		return segments;
	}
	
	public static final int[][] doMagicMappingToCanvas(Iterable<Point> points, MuseumRoom museumRoom, Dimension canvasDimension) {
		double xMin = 0;
		double yMin = 0;
		double xMax = 0;
		double yMax = 0;		
		for(Point point : museumRoom.room) {
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
