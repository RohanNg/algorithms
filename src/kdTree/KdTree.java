package kdTree;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private Node root; // root of BST
	private int size = 0;

	private class Node {
		private Point2D point;
		private Node lb, rt; // left bottom and right top node

		public Node(Point2D point) {
			this.point = point;
		}
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void insert(Point2D p) {
		root = insert(root, p, true);
	}

	private Node insert(Node x, Point2D point, boolean isVerticalSplit) {
		if (x == null) {
			size++;
			return new Node(point);
		}

		int cmp = isVerticalSplit ? Double.compare(point.x(), x.point.x()) : Double.compare(point.y(), x.point.y());

		if (cmp < 0)
			x.lb = insert(x.lb, point, !isVerticalSplit);
		else if (cmp > 0)
			x.rt = insert(x.rt, point, !isVerticalSplit);
		// case one coordinate equals but other differs, set it to the left node
		else if (!point.equals(x.point)) {
			x.lb = insert(x.lb, point, !isVerticalSplit);
		}
		return x;
	}

	public boolean contains(Point2D p) {
		return contains(root, p, true);
	}

	private boolean contains(Node x, Point2D point, boolean isVerticalSplit) {
		if (x == null)
			return false;
		if (x.point.equals(point))
			return true;
		int cmp = isVerticalSplit ? Double.compare(point.x(), x.point.x()) : Double.compare(point.y(), x.point.y());
		if (cmp <= 0)
			return contains(x.lb, point, !isVerticalSplit);
		else
			return contains(x.rt, point, !isVerticalSplit);
	}

	public void draw() {
		draw(root, true, 0, 0, 1, 1);
	}

	private void draw(Node n, boolean isVerticalSplit, double xmin, double ymin, double xmax, double ymax) {
		if (n == null)
			return;
		double x = n.point.x();
		double y = n.point.y();
		assert x >= xmin && x <= xmax && y <= ymax && y >= ymin;
		// draw the point
		StdDraw.setPenRadius();
		if (isVerticalSplit) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x, ymin, x, ymax);
			draw(n.rt, !isVerticalSplit, x, ymin, xmax, ymax);
			draw(n.lb, !isVerticalSplit, xmin, ymin, x, ymax);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE); // blue for horizontal split
			StdDraw.line(xmin, y, xmax, y);
			draw(n.lb, !isVerticalSplit, xmin, ymin, xmax, y);
			draw(n.rt, !isVerticalSplit, xmin, y, xmax, ymax);
		}
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		n.point.draw();
	}

	public Iterable<Point2D> range(RectHV rect) {
		List<Point2D> inRange = new ArrayList<>();
		rangeSearch(root, rect, true, inRange);
		return inRange;
	}

	private static void rangeSearch(Node node, RectHV rect, boolean isVerticalSplit, List<Point2D> inrange) {
		if (node == null)
			return;
		Point2D point = node.point;

		if (rect.contains(point)) {
			inrange.add(point);
			rangeSearch(node.lb, rect, !isVerticalSplit, inrange);
			rangeSearch(node.rt, rect, !isVerticalSplit, inrange);
		} else if (isVerticalSplit) {
			// check left
			if (rect.xmin() <= point.x())
				rangeSearch(node.lb, rect, !isVerticalSplit, inrange);
			// check right
			if (rect.xmax() > point.x())
				rangeSearch(node.rt, rect, !isVerticalSplit, inrange);
		} else { // case not contains and horizontal split
			if (rect.ymin() <= point.y())
				rangeSearch(node.lb, rect, !isVerticalSplit, inrange);
			// if rect on the bottom
			if (rect.ymax() > point.y())
				rangeSearch(node.rt, rect, !isVerticalSplit, inrange);
		}
	}

	public Point2D nearest(Point2D point) {
		if (root == null)
			return null;
		return nearest(root, true, root.point, point);
	}

	private Point2D nearest(Node node, boolean isVerticalSplit, Point2D champion, Point2D target) {
		if (node == null)
			return champion;
		Point2D point = node.point;
		// for testing search path
		// StdDraw.setPenColor(StdDraw.CYAN);
		// StdDraw.setPenRadius(0.009);
		// StdDraw.point(point.x(), point.y());
		if (target.distanceSquaredTo(point) < target.distanceSquaredTo(champion)) {
			champion = point;
		}
		// search on the branch which may have candidate for new champion
		if (isVerticalSplit) {
			if (target.x() <= point.x()) {
				// target is on the left
				champion = nearest(node.lb, !isVerticalSplit, champion, target);
				// decision to check the right
				if (target.distanceSquaredTo(champion) > Math.pow(point.x() - target.x(), 2)) {
					champion = nearest(node.rt, !isVerticalSplit, champion, target);
				}
			} else {
				// target is on the right
				champion = nearest(node.rt, !isVerticalSplit, champion, target);
				// decision to check the left
				if (target.distanceSquaredTo(champion) > Math.pow(point.x() - target.x(), 2)) {
					champion = nearest(node.lb, !isVerticalSplit, champion, target);
				}
			}
		} else {
			if (target.y() <= point.y()) {
				// target is on the top
				champion = nearest(node.lb, !isVerticalSplit, champion, target);
				// decision to check the bottom
				if (target.distanceSquaredTo(champion) > Math.pow(point.y() - target.y(), 2)) {
					champion = nearest(node.rt, !isVerticalSplit, champion, target);
				}
			} else {
				// target is on the bottom
				champion = nearest(node.rt, !isVerticalSplit, champion, target);
				// decision to check the top
				if (target.distanceSquaredTo(champion) > Math.pow(point.y() - target.y(), 2)) {
					champion = nearest(node.lb, !isVerticalSplit, champion, target);
				}
			}
		}
		return champion;
	}

	public static void main(String[] args) {
		// KdTree kd = new KdTree();
		// for (int x = 0; x < 6; x++) {
		// double a = StdRandom.random();
		// double b = StdRandom.random();
		// Point2D pt = new Point2D(a, b);
		// kd.insert(pt);
		// System.out.println(kd.contains(pt));
		// }
		// kd.draw();

		String filename = "/home/rohan/workspace/algorithms/testData/kdtree/rangeTest.txt";
		In in = new In(filename);

		// initialize the two data structures with point from standard input
		KdTree kdtree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
		}
		kdtree.draw();
		RectHV rec = new RectHV(.404296875, .2890625, .6640625, .41796875);
		rec.draw();
		for (Point2D point : kdtree.range(rec)) {
			System.out.println("found:" + point);
		}

	}
}
