package geometry.types;

import resources.Hasher;

public final class Point {

	public final double x;
	public final double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}	
	
	/**
	 * For segment p1->p2, is p on the left of the segment or the right
	 * @param s
	 * @param p
	 * @return
	 */
	public boolean leftOf(Line s) {
		double cross = (s.end.x - s.start.x) * (y - s.start.y) 
						- (s.end.y - s.start.y) * (x - s.start.x);
		return cross < 0;
	}
	
	public int sideRelativeTo(Line line) {
		return leftOf(line) ? 1 : -1;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Point)) {
			return false;
		}
		Point that = (Point) obj;
		return this.x == that.x && this.y == that.y;
	}
	
	@Override
	public int hashCode() {
		return Hasher.hash(x, y);		
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
