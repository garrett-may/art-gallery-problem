package main;

import java.awt.Polygon;
import java.io.FileNotFoundException;
import java.util.List;

import draw.Window;
import geometry.MuseumRoom;
import resources.Parser;

public class Main {

	public static void main(String[] args) {
		
		
		Window window = new Window();
		Parser parser = new Parser();
		try {
			List<MuseumRoom> museumRooms = parser.parse(parser.getResource("guards.txt"));	
			window.drawMuseumRoom(museumRooms.get(2));
		} catch (FileNotFoundException e) {
			
		}
	}
}
