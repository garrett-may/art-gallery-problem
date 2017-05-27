package geometry.logic;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import geometry.types.Line;
import geometry.types.Point;
import geometry.types.Polygon;

public final class MissingAreasComputer {

	private MissingAreasComputer() {
		
	}
	
	private static class PolygonPathIterator implements PathIterator {

		private final Polygon polygon;
		private int counter;
		
		public PolygonPathIterator(Polygon polygon) {
			this.polygon = polygon;
			this.counter = 0;
		}
		
		@Override
		public int currentSegment(float[] coords) {
			double[] dcoords = new double[6];
			int returnValue = currentSegment(dcoords);
			for(int i = 0; i < coords.length; i++) {
				coords[i] = (float) dcoords[i];
			}
			return returnValue;
		}

		@Override
		public int currentSegment(double[] coords) {
			if(counter >= polygon.points.size()) {
				return SEG_CLOSE;
			}
			Point p = polygon.points.get(counter);
			coords[0] = p.x;
			coords[1] = p.y;
			return counter == 0 ? SEG_MOVETO : SEG_LINETO;
		}

		@Override
		public int getWindingRule() {
			return WIND_EVEN_ODD;
		}

		@Override
		public boolean isDone() {
			return counter == polygon.segments.size() + 1;
		}

		@Override
		public void next() {
			counter += 1;
		}
		
	}
	
	private static class PolygonShape implements Shape {

		private final Polygon polygon;
		
		public PolygonShape(Polygon polygon) {
			this.polygon = polygon;
		}
		
		@Override
		public boolean contains(Point2D p) {
			return polygon.contains(new Point(p.getX(), p.getY()));
		}

		@Override
		public boolean contains(Rectangle2D r) {
			return contains(r.getMinX(), r.getMinY())
					&& contains(r.getMinX(), r.getMaxY())
					&& contains(r.getMaxX(), r.getMinY())
					&& contains(r.getMaxX(), r.getMaxY());
		}

		@Override
		public boolean contains(double x, double y) {
			return polygon.contains(new Point(x, y));
		}

		@Override
		public boolean contains(double x, double y, double w, double h) {
			Point p1 = new Point(x, y);
			Point p2 = new Point(x, y + h);
			Point p3 = new Point(x + w, y);
			Point p4 = new Point(x + w, y + h);
			return polygon.contains(p1)
					&& polygon.contains(p2)
					&& polygon.contains(p3)
					&& polygon.contains(p4);
		}

		@Override
		public Rectangle getBounds() {
			double xMin = 0;
			double yMin = 0;
			double xMax = 0;
			double yMax = 0;		
			for(Point point : polygon.points) {
				xMin = point.x < xMin ? point.x : xMin;
				yMin = point.y < yMin ? point.y : yMin;
				xMax = point.x > xMax ? point.x : xMax;
				yMax = point.y > yMax ? point.y : yMax;
			}
			return new Rectangle((int) xMin, (int) yMin, (int) (xMax - xMin), (int) (yMax - yMin));
		}

		@Override
		public Rectangle2D getBounds2D() {
			return getBounds();
		}

		@Override
		public PathIterator getPathIterator(AffineTransform at) {
			return new PolygonPathIterator(polygon);
		}

		@Override
		public PathIterator getPathIterator(AffineTransform at, double flatness) {
			return new PolygonPathIterator(polygon);
		}

		@Override
		public boolean intersects(Rectangle2D r) {
			return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
		}

		@Override
		public boolean intersects(double x, double y, double w, double h) {
			Point p1 = new Point(x, y);
			Point p2 = new Point(x, y + h);
			Point p3 = new Point(x + w, y);
			Point p4 = new Point(x + w, y + h);
			return !polygon.intersections(new Line(p1, p2)).isEmpty()
					|| !polygon.intersections(new Line(p2, p3)).isEmpty()
					|| !polygon.intersections(new Line(p3, p4)).isEmpty()
					|| !polygon.intersections(new Line(p4, p1)).isEmpty();
		}
		
	}
	
	public static List<List<Point>> computeMissingAreas(Polygon room, Set<List<Point>> guardViews) {
		Area polygonArea = new Area(new PolygonShape(room));
		for(List<Point> guardView : guardViews) {
			Polygon guardPolygon = new Polygon(guardView);
			Area guardArea = new Area(new PolygonShape(guardPolygon));
			polygonArea.subtract(guardArea);
		}
		
		List<List<Point>> polygons = new ArrayList<>();
		PathIterator iterator = polygonArea.getPathIterator(null);
		double[] coords = new double[6];
		List<Point> polygon = null;
		while(!iterator.isDone()) {
			int type = iterator.currentSegment(coords);
			switch(type) {
				case PathIterator.SEG_MOVETO:
					polygon = new ArrayList<>();
					polygon.add(new Point(coords[0], coords[1]));
					break;
				case PathIterator.SEG_LINETO:
					polygon.add(new Point(coords[0], coords[1]));
					break;
				case PathIterator.SEG_CLOSE:
					if(polygon != null) {
						polygons.add(polygon);
					}
					polygon = null;
					break;
				default:
					break;
			}
			iterator.next();
		}
		return polygons;	
	}
	
}
