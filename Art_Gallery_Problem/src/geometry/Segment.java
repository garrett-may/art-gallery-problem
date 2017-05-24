package geometry;

import java.util.Arrays;
import java.util.Iterator;
import static java.lang.Math.*;

public class Segment implements Iterable<Point> {
	
	public Point p1;
	public Point p2;
	public double d;
	
	public Segment(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	@Override
	public Iterator<Point> iterator() {
		return Arrays.asList(new Point[] {p1, p2}).iterator();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Segment)) {
			return false;
		}
		Segment that = (Segment) obj;
		return (this.p1 == that.p1 && this.p2 == that.p2)
				|| (this.p1 == that.p2 && this.p1 == that.p2);
	}
	
	@Override
	public String toString() {
		return "[" + p1 + ", " + p2 + "]";
	}
}
