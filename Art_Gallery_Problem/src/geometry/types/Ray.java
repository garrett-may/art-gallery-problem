package geometry.types;

import resources.Hasher;

public final class Ray {

	private static final double MAX_DISTANCE = 10000;
	public final Point origin;
	public final Point target;
	public final double angle;
	
	public Ray(Point origin, double angle) {
		this.origin = origin;
		this.target = new Point(origin.x + Math.cos(angle) * MAX_DISTANCE, origin.y + Math.sin(angle) * MAX_DISTANCE); 
		this.angle = angle;
	}
	
	public Ray(Point origin, Point target) {
		this.origin = origin;
		this.target = target;
		
		double angle = Math.atan2(target.y - origin.y, target.x - origin.x);
		while(angle < 0) {
			angle += 2 * Math.PI;
		}
		while(angle >= 2 * Math.PI) {
			angle -= 2 * Math.PI;
		}
		this.angle = angle;
	}
	
	public Ray(Point origin, Point target, double angle) {
		this.origin = origin;
		this.target = target;
		this.angle = angle;
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
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Ray)) {
			return false;
		}
		Ray that = (Ray) object;
		return this.origin.equals(that.origin) && this.angle == that.angle;
	}
	
	@Override
	public int hashCode() {
		return Hasher.hash(origin, angle);
	}
	
	@Override
	public String toString() {
		return "Origin: " + origin + " | Angle: " + angle;
	}
}
