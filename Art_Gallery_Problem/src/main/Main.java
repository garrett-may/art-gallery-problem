package main;

import java.io.FileNotFoundException;
import java.util.List;

import draw.Window;
import resources.Parser;

public class Main {

	public static void main(String[] args) {
		Window window = new Window();
		Parser parser = new Parser();
		try {
			List<Museum> museumRooms = parser.parse(parser.getResource("guards.txt"));	
			window.drawMuseumRoom(museumRooms.get(0));
		} catch (FileNotFoundException e) {
			
		}
	}
}
