package geometry.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import resources.Hasher;

public final class Triangle implements Iterable<Point> {

	public final Point p1;
	public final Point p2;
	public final Point p3;
	public final List<Line> segments;
	
	public Triangle(Point p1, Point p2, Point p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.segments = new ArrayList<>();
		
		segments.add(new Line(p1, p2));
		segments.add(new Line(p2, p3));
		segments.add(new Line(p3, p1));
	}
	
	public boolean contains(Point point) {
		for(Line segment : segments) {
			if(point.leftOf(segment)) {
				return false;
			}
		}
		return true; 
	}

	@Override
	public Iterator<Point> iterator() {
		return Arrays.asList(new Point[] {p1, p2, p3}).iterator();
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Triangle)) {
			return false;
		}
		Triangle that = (Triangle) object;
		return this.p1.equals(that.p1)
				&& this.p2.equals(that.p2)
				&& this.p3.equals(that.p3);
	}
	
	@Override
	public int hashCode() {
		return Hasher.hash(p1, p2, p3);
	}
}
