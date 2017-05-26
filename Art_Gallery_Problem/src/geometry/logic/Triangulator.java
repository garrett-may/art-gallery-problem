package geometry.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import geometry.types.Line;
import geometry.types.Point;
import geometry.types.Polygon;
import geometry.types.Triangle;

public final class Triangulator {

	private Triangulator() {
		
	}
	
	private static int prev(int i, int size) {
		return i <= 0 ? i - 1 + size : i - 1;
	}
	
	private static int next(int i, int size) {
		return i >= size - 1 ? i + 1 - size : i + 1;
	}
	
	public static boolean pointStrictlyInTriangle(Triangle triangle, Point point) {
		if(triangle.p1.equals(point) 
				|| triangle.p2.equals(point) 
				|| triangle.p3.equals(point)) {
			return false;
		}
		if(!triangle.segments.stream().map(line -> line.contains(point)).filter(x -> x == true).collect(Collectors.toList()).isEmpty()) {
			return true;
		}
		return triangle.contains(point);
	}
	
	private static boolean collinear(Point p1, Point p2, Point p3) {
		if(p1.x == p2.x && p2.x == p3.x) {
			return true;
		}
		if(p1.y == p2.y && p2.y == p3.y) {
			return true;
		}
		if(new Line(p1, p2).contains(p3)) {
			return true;
		}
		if(new Line(p1, p3).contains(p2)) {
			return true;
		}
		if(new Line(p2, p1).contains(p3)) {
			return true;
		}
		if(new Line(p2, p3).contains(p1)) {
			return true;
		}
		if(new Line(p3, p1).contains(p2)) {
			return true;
		}
		if(new Line(p3, p2).contains(p1)) {
			return true;
		}
		return false;
	}
	
	private static boolean isReflexAngle(int i, List<Point> points) {
		Point prev = points.get(prev(i, points.size()));
		Point curr = points.get(i);
		Point next = points.get(next(i, points.size()));
		return next.leftOf(new Line(prev, curr)) || collinear(prev, curr, next);
	}	
	
	private static Triangle findEar(List<Point> points) {
		int size = points.size();
		for(int i = 0; i < size; i++) {
			if(!isReflexAngle(i, points)) {
				Point prev = points.get(prev(i, size));
				Point curr = points.get(i);
				Point next = points.get(next(i, size));
				Triangle triangle = new Triangle(prev, curr, next);
				boolean concaveVertexFound = false;				
				for(int j = 0; j < size; j++) {					
					Point that = points.get(j);
					if(isReflexAngle(j, points) && pointStrictlyInTriangle(triangle, that)) {
						concaveVertexFound = true;
						break;
					}
				}
				if(!concaveVertexFound) {
					// Don't consider lines as triangles
					if(!collinear(prev, curr, next)) {
						return triangle;
					}
				}
			}
		}
		System.out.println("Point size: " + points.size() + " | Couldn't find proper ear!!!");
		int i = 0;
		Point prev = points.get(prev(i, size));
		Point curr = points.get(i);
		Point next = points.get(next(i, size));
		return new Triangle(prev, curr, next);
	}
	
	public static List<Triangle> triangulate(Polygon room) {
		List<Point> points = room.points.stream().map(p -> new Point(p.x, p.y)).collect(Collectors.toList());
		List<Triangle> triangles = new ArrayList<>();
		while(points.size() > 3) {
			Triangle ear = findEar(points);
			triangles.add(ear);
			points.remove(ear.p2);
		}
		triangles.add(new Triangle(points.get(0), points.get(1), points.get(2)));
		return triangles;
	}
}
