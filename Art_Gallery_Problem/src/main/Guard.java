package main;

import java.util.List;

import geometry.logic.VisibilityComputer;
import geometry.types.Point;

public final class Guard {

	public final Point point;
	public final List<Point> guardView;
	
	public Guard(Point point, Museum museumRoom) {
		this.point = point;
		this.guardView = VisibilityComputer.computeVisibilityPoints(museumRoom.room, point);
	}
	
	@Override
	public String toString() {
		return point.toString();
	}
}
