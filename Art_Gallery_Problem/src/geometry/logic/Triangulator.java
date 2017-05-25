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
		return triangle.contains(point);
	}
	
	private static boolean isReflexAngle(int i, List<Point> points) {
		Point prev = points.get(prev(i, points.size()));
		Point curr = points.get(i);
		Point next = points.get(next(i, points.size()));
		return next.leftOf(new Line(prev, curr));
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
					}
				}
				if(!concaveVertexFound) {
					return triangle;
				}
			}
		}
		int i = 0;
		Point prev = points.get(prev(i, size));
		Point curr = points.get(i);
		Point next = points.get(next(i, size));
		Triangle triangle = new Triangle(prev, curr, next);
		return triangle;
	}
	
	public static List<Triangle> triangulate(Polygon room) {
		List<Point> points = room.points.stream().map(p -> new Point(p.x, p.y)).collect(Collectors.toList());
		List<Triangle> triangles = new ArrayList<>();
		while(points.size() > 2) {
			Triangle ear = findEar(points);
			triangles.add(ear);
			points.remove(ear.p2);
		}
		return triangles;
	}
}
