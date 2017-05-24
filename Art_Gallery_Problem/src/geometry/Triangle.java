package geometry;

import java.util.Arrays;
import java.util.Iterator;

public class Triangle implements Iterable<Point> {

	public final Point p1;
	public final Point p2;
	public final Point p3;
	
	public Triangle(Point p1, Point p2, Point p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	@Override
	public Iterator<Point> iterator() {
		return Arrays.asList(new Point[] {p1, p2, p3}).iterator();
	}
}
