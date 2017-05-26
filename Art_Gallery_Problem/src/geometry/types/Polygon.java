package geometry.types;

import java.util.ArrayList;
import java.util.List;

import geometry.logic.Triangulator;
import resources.Hasher;

public final class Polygon {

	public final List<Point> points;
	public final List<Line> segments;
	public final List<Triangle> triangles;
		
	public Polygon(List<Point> room) {
		this.points = new ArrayList<>();
		this.segments = new ArrayList<>();
		
		for(Point point : room) {
			points.add(new Point(point.x, point.y));
		}
		
		int size = points.size();
		for(int i = 0; i < size; i++) {
			int j = (i + 1) % size;
			segments.add(new Line(points.get(i), points.get(j)));
		}
		
		this.triangles = Triangulator.triangulate(this);
	}
	
	public boolean contains(Point point) {
		for(Triangle triangle : triangles) {
			if(triangle.contains(point)) {
				if(point.equals(new Point(-4, -0.99980582140952))) {
					System.out.println(triangle);
				}
				return true;
			}
		}
		return false;
	}
	
	public List<Point> intersections(Ray scanLine) {
		List<Point> list = new ArrayList<>();
		for(Line segment : segments) {
			Point intersection = scanLine.intersection(segment);
			if(intersection != null) {
				list.add(intersection);
			}
		}
		return list;
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Polygon)) {
			return false;
		}
		Polygon that = (Polygon) object;
		return this.points.equals(that.points);
	}
	
	@Override
	public int hashCode() {
		return Hasher.hash(points);
	}
}
