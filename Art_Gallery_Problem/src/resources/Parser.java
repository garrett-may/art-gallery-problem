package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import geometry.MuseumRoom;
import geometry.Point;

public class Parser {
	
	public String getResource(String filename) {
		return getClass().getResource(filename).getFile();
	}
	
	public List<MuseumRoom> parse(String filename) throws FileNotFoundException {
		List<MuseumRoom> museumRooms = new ArrayList<>();
		File file = new File(filename);
		try(Scanner scanner = new Scanner(file)) {
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().substring(3).trim();
				MuseumRoom museumRoom = parseMuseumRoom(line);
				museumRooms.add(museumRoom);
			}
		}
		return museumRooms;
	}
	
	private MuseumRoom parseMuseumRoom(String string) {
		String[] half = string.trim().split(";");
		String[] museum = half[0].trim().split("\\), ");
		String[] guards = half.length > 1 ? half[1].trim().split("\\), \\(") : new String[0];
		
		MuseumRoom museumRoom = new MuseumRoom();
		for(String museumCoord : museum) {
			Point point = parsePoint(museumCoord);
			museumRoom.room.add(point);
		}
		for(String guardCoord : guards) {
			Point point = parsePoint(guardCoord);
			museumRoom.guards.add(point);
		}
		
		return museumRoom;
	}
	
	private Point parsePoint(String string) {
		string = string.replaceAll("[\\(\\) ]", " ").trim();
		String[] coords = string.split(",");
		return new Point(new Double(coords[0]), new Double(coords[1]));
	}	
}
