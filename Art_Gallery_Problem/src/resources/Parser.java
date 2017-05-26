package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import geometry.types.Point;
import main.Museum;

public class Parser {
	
	public String getResource(String filename) {
		return getClass().getResource(filename).getFile();
	}
	
	public List<Museum> parse(String filename) throws FileNotFoundException {
		List<Museum> museumRooms = new ArrayList<>();
		File file = new File(filename);
		try(Scanner scanner = new Scanner(file)) {
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().substring(3).trim();
				Museum museumRoom = parseMuseumRoom(line);
				museumRooms.add(museumRoom);
			}
		}
		return museumRooms;
	}
	
	private Museum parseMuseumRoom(String string) {
		String[] half = string.trim().split(";");
		String[] museum = half[0].trim().split("\\), ");
		String[] guards = half.length > 1 ? half[1].trim().split("\\), \\(") : new String[0];
		
		List<Point> points = new ArrayList<>();
		Set<Point> guardPoints = new HashSet<>();
		
		for(String museumCoord : museum) {
			Point point = parsePoint(museumCoord);
			points.add(point);
		}
		for(String guardCoord : guards) {
			Point point = parsePoint(guardCoord);
			guardPoints.add(point);
		}
		
		Museum museumRoom = new Museum(points, guardPoints);
		return museumRoom;
	}
	
	private Point parsePoint(String string) {
		string = string.replaceAll("[\\(\\) ]", " ").trim();
		String[] coords = string.split(",");
		return new Point(new Double(coords[0]), new Double(coords[1]));
	}	
}
