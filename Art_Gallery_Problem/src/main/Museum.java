package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import geometry.types.Point;
import geometry.types.Polygon;

public class Museum {

	public final Polygon room;
	public final Set<Guard> guards;
	
	public Museum(List<Point> room, Set<Point> guards) {
		this.room = new Polygon(room);
		this.guards = new HashSet<Guard>();
		
		for(Point guard : guards) {
			this.guards.add(new Guard(guard, this));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		builder.append("Room: ");
		builder.append(room.points.size() == 0 ? "empty" : room.points.get(0));
		for(int i = 1; i < room.points.size(); i++) {
			builder.append(", ");
			builder.append(room.points.get(i));
		}
		builder.append("\n");
		
		Iterator<Guard> iterator = guards.iterator();
		builder.append("Guards: ");		
		builder.append(!iterator.hasNext() ? "none" : iterator.next());
		while(iterator.hasNext()) {
			builder.append(", ");
			builder.append(iterator.next());
		}
		
		return builder.toString();
	}
}
