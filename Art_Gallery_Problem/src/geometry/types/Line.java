package geometry.types;

import java.util.Arrays;
import java.util.Iterator;

public final class Line implements Iterable<Point> {

	public Point start;
	public Point end;
	private double angle;
	private double length;
	
	public Line(Point start, Point end) {
		this.start = start;
		this.end = end;
		this.angle = Math.atan2(end.y - start.y, end.x - start.x);
		this.length = Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
	}
	
	public double length() {
		return length;
	}
	
	public boolean contains(Point point) {
		Line subline = new Line(start, point);
		return subline.angle == angle && subline.length <= length;
	}	
	
	public Point interpolate(double f) {
		return new Point(start.x * (1 - f) + end.x * f, start.y * (1 - f) + end.y * f);
	}

	@Override
	public Iterator<Point> iterator() {
		return Arrays.asList(new Point[] {start, end}).iterator();
	}
	
	@Override
	public String toString() {
		return "[" + start + " -> " + end + "]";
	}
}
