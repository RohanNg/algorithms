package patternRecognition;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private LineSegment[] lines;

	public BruteCollinearPoints(Point[] points) {
		int lineCounts = 0;
		lines = new LineSegment[10];
		for (int a = 0; a < points.length; a++) {
			for (int b = a + 1; b < points.length; b++) {
				double slope = points[a].slopeTo(points[b]);
				// check if there are repeated points
				if (slope == Double.NEGATIVE_INFINITY)
					throw new IllegalArgumentException("Set of points must not contains repeated points");
				for (int c = b + 1; c < points.length; c++) {
					if (slope == points[a].slopeTo(points[c])) {
						for (int d = c + 1; d < points.length; d++) {
							if (slope == points[a].slopeTo(points[d])) {
								if (lineCounts == lines.length)
									lines = resize(lines, lineCounts, lineCounts * 2);
								lines[lineCounts++] = createLine(points[a], points[b], points[c], points[d]);
							}
						}
					}
				}
			}
		}
		lines = resize(lines, lineCounts, lineCounts);
	}

	public int numberOfSegments() {
		return lines.length;
	}

	public LineSegment[] segments() {
		return Arrays.copyOf(lines, lines.length);
	}

	private static LineSegment createLine(Point... points) {
		Point lowerBound = points[0];
		Point upperBound = points[1];
		if (lowerBound.compareTo(upperBound) > 0) {
			lowerBound = points[1];
			upperBound = points[0];
		}
		for (int x = 2; x < points.length; x++) {
			if (points[x].compareTo(upperBound) > 0)
				upperBound = points[x];
			if (points[x].compareTo(lowerBound) < 0)
				lowerBound = points[x];
		}
		return new LineSegment(lowerBound, upperBound);
	}

	private LineSegment[] resize(LineSegment[] arr, int copyTo, int length) {
		LineSegment[] copy = new LineSegment[length];
		for (int x = 0; x < copyTo; x++) {
			copy[x] = arr[x];
		}
		return copy;
	}

	public static void main(String[] args) {

		// read the n points from a file
		In in = new In("/home/rohan/workspace/algorithms/testData/collinear/myinput.txt");
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 10);
		StdDraw.setYscale(0, 10);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
