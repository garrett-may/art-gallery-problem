package geometry.types;

import java.util.Arrays;
import java.util.Iterator;

import resources.Hasher;

public final class Line implements Iterable<Point> {

	public final Point start;
	public final Point end;
	private final double angle;
	private final double length;
	
	public Line(Point start, Point end) {
		this.start = start;
		this.end = end;
		this.angle = Math.atan2(end.y - start.y, end.x - start.x);
		this.length = Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
	}
	
	public double angle() {
		return angle;
	}
	
	public double length() {
		return length;
	}
	
	public boolean contains(Point point) {
		Line subline = new Line(start, point);
		return Math.abs(subline.angle - angle) < 0.0000000001 && subline.length <= length;
	}	
	
	public Point interpolate(double f) {
		return new Point(start.x * (1 - f) + end.x * f, start.y * (1 - f) + end.y * f);
	}
	
	public Point intersection(Line line) {
		Point p1 = line.start;
		Point p2 = line.end;
		Point p3 = start;
		Point p4 = end;
		double s1x = p2.x - p1.x;
		double s1y = p2.y - p1.y;
		double s2x = p4.x - p3.x;
		double s2y = p4.y - p3.y;
		double s = (-s1y * (p1.x - p3.x) + s1x * (p1.y - p3.y)) / (-s2x * s1y + s1x * s2y);
		double t = (s2x * (p1.y - p3.y) - s2y * (p1.x - p3.x)) / (-s2x * s1y + s1x * s2y);
		if(0 <= s && 0 <= t && t <= 1) {
			return new Point(p1.x + (t * s1x), p1.y + (t * s1y));
		} else {
			return null;
		}
	}

	@Override
	public Iterator<Point> iterator() {
		return Arrays.asList(new Point[] {start, end}).iterator();
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Line)) {
			return false;
		}
		Line that = (Line) object;
		return this.start.equals(that.start) && this.end.equals(that.end);
	}
	
	@Override
	public int hashCode() {
		return Hasher.hash(start, end);
	}
	
	@Override
	public String toString() {
		return "[" + start + " -> " + end + "]";
	}
}
