package kdTree;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
	private final TreeSet<Point2D> rbtree;

	public PointSET() {
		rbtree = new TreeSet<>();
	}

	public boolean isEmpty() {
		return rbtree.isEmpty();
	}

	public int size() {
		return rbtree.size();
	}

	public void insert(Point2D p) {
		rbtree.add(p);
	}

	public boolean contains(Point2D p) {
		return rbtree.contains(p);
	}

	public void draw() {
		for (Point2D point : rbtree) {
			point.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect) {
		List<Point2D> list = new ArrayList<>();
		for (Point2D point : rbtree) {
			if (rect.contains(point))
				list.add(point);
		}
		return list;
	}

	public Point2D nearest(Point2D target) {
		if (rbtree.isEmpty())
			return null;
		Point2D champion = rbtree.first();
		double minDistance = target.distanceSquaredTo(champion);
		for (Point2D point : rbtree) {
			double distance = target.distanceSquaredTo(point);
			if (distance < minDistance) {
				champion = point;
				minDistance = distance;
			}
		}
		return champion;
	}

	public static void main(String[] args) { // unit testing of the methods
												// (optional)
	}
}