package geometry;

import static java.lang.Math.*;

import java.util.ArrayList;


public class Geometry {

	public static Point segmentIntersection(Point p1, Point p2, Point p3, Point p4) {
		double s1x = p2.x - p1.x;
		double s1y = p2.y - p1.y;
		double s2x = p4.x - p3.x;
		double s2y = p4.y - p3.y;
		double s = (-s1y * (p1.x - p3.x) + s1x * (p1.y - p3.y)) / (-s2x * s1y + s1x * s2y);
		double t = (s2x * (p1.y - p3.y) - s2y * (p1.x - p3.x)) / (-s2x * s1y + s1x * s2y);
		if(0 < s && s <= 1 && 0 < t && t < 1) {
			return new Point(p1.x + (t * s1x), p1.y + (t * s1y));
		} else {
			return null;
		}
	}
	
	public static Point rayIntersection(Point p1, Point p2, Point p3, Point p4) {
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
	
	public static double rayIntersectionSValue(Point p1, Point p2, Point p3, Point p4) {
		double s1x = p2.x - p1.x;
		double s1y = p2.y - p1.y;
		double s2x = p4.x - p3.x;
		double s2y = p4.y - p3.y;
		double s = (-s1y * (p1.x - p3.x) + s1x * (p1.y - p3.y)) / (-s2x * s1y + s1x * s2y);
		double t = (s2x * (p1.y - p3.y) - s2y * (p1.x - p3.x)) / (-s2x * s1y + s1x * s2y);
		if(0 < s && 0 <= t && t <= 1) {
			return s;
		} else {
			return -1.0;
		}
	}
	
	public static double rayIntersectionTValue(Point p1, Point p2, Point p3, Point p4) {
		double s1x = p2.x - p1.x;
		double s1y = p2.y - p1.y;
		double s2x = p4.x - p3.x;
		double s2y = p4.y - p3.y;
		double s = (-s1y * (p1.x - p3.x) + s1x * (p1.y - p3.y)) / (-s2x * s1y + s1x * s2y);
		double t = (s2x * (p1.y - p3.y) - s2y * (p1.x - p3.x)) / (-s2x * s1y + s1x * s2y);
		if(0 < s && 0 <= t && t <= 1) {
			return t;
		} else {
			return -1.0;
		}
	}
	
	public static double angle(Point a, Point b) {
		return atan2(b.y - a.y, b.x - a.x);
	}
	
	public static double angle2(Point a, Point b, Point c) {
		double a1 = angle(a, b);
		double a2 = angle(b, c);
		double a3 = a1 - a2;
		if(a3 < 0) {
			a3 += 2*PI;
		}
		if(a3 > 2*PI) {
			a3 -= 2*PI;
		}
		return a3;
	}
	
	public static double distanceSquared(Point a, Point b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		return dx * dx + dy * dy;
	}
	
}
