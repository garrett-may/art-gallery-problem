package geometry.types;

public final class Ray {

	private static final double MAX_DISTANCE = 10000;
	public Point origin;
	public Point target;
	
	public Ray(Point origin, double angle) {
		this.origin = origin;
		this.target = new Point(origin.x + Math.cos(angle) * MAX_DISTANCE, origin.y + Math.sin(angle) * MAX_DISTANCE); 
	}
	
	public double length(Point point) {
		return Math.sqrt(Math.pow(point.x - origin.x, 2) + Math.pow(point.y - origin.y, 2));
	}
	
	public Point intersection(Line line) {
		Point p1 = line.start;
		Point p2 = line.end;
		Point p3 = origin;
		Point p4 = target;
		double s1x = p2.x - p1.x;
		double s1y = p2.y - p1.y;
		double s2x = p4.x - p3.x;
		double s2y = p4.y - p3.y;
		double s = (-s1y * (p1.x - p3.x) + s1x * (p1.y - p3.y)) / (-s2x * s1y + s1x * s2y);
		double t = (s2x * (p1.y - p3.y) - s2y * (p1.x - p3.x)) / (-s2x * s1y + s1x * s2y);
		if(0 < s && 0 <= t && t <= 1) {
			return new Point(p1.x + (t * s1x), p1.y + (t * s1y));
		} else {
			return null;
		}
	}	
}
