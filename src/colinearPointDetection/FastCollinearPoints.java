package colinearPointDetection;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	private LineSegment[] lines;
	private int linesCount = 0;

	public FastCollinearPoints(Point[] points) {

		if (points.length > 3) {
			Point[] copy = Arrays.copyOf(points, points.length);
			lines = new LineSegment[10];
			for (int x = 0; x < points.length; x++) {
				Arrays.sort(copy, points[x].slopeOrder());
				inferLineSegmentsThrough(points[x], copy);
			}
		} else {
			// there is no line segment
			// check for duplicate point
			for(int x = 0; x < points.length; x++) {
				for(int y = x + 1; y < points.length; y++) {
					if(points[x].compareTo(points[y]) == 0){
						throw new IllegalArgumentException("Duplicate found!");
					}
				}
			}
		}
		lines = resize(lines, linesCount, linesCount);
	}

	private void inferLineSegmentsThrough(Point throughPoint, Point[] points) {
		
		assert points[0].compareTo(throughPoint) == 0;
		double slope = throughPoint.slopeTo(points[1]);
		if (slope == Double.NEGATIVE_INFINITY)
			throw new IllegalArgumentException("Duplicate point found");
		int pointOnLineCount = 2;

		for (int x = 2; x < points.length; x++) { // the last item
			double currSlope = throughPoint.slopeTo(points[x]);

			if (currSlope == Double.NEGATIVE_INFINITY) {
				throw new IllegalArgumentException("Duplicate points found.");
			} else if (slope != currSlope) {
				if (pointOnLineCount > 3) {
					addLineSegment(throughPoint, points, x - pointOnLineCount + 1, x - 1);
				}
				pointOnLineCount = 2;
				slope = throughPoint.slopeTo(points[x]);
			} else {
				pointOnLineCount++;
				if (x == points.length - 1 && pointOnLineCount > 3) { 
					addLineSegment(throughPoint, points, x - pointOnLineCount + 2, x);
				}
			}
		}
	}

	private void addLineSegment(Point throughPoint, Point[] points, int from, int to) {
		LineSegment candidateLine = createLineSegment(throughPoint, points, from, to);
		if (candidateLine != null) {
			if (linesCount == lines.length)
				lines = resize(lines, linesCount, 2 * linesCount);
			lines[linesCount++] = candidateLine;
		}
	}

	public LineSegment[] segments() {
		return Arrays.copyOf(lines, lines.length);
	}

	public int numberOfSegments() {
		return lines.length;
	}

	private LineSegment[] resize(LineSegment[] arr, int copyTo, int length) {
		LineSegment[] copy = new LineSegment[length];
		for (int x = 0; x < copyTo; x++) {
			copy[x] = arr[x];
		}
		return copy;
	}

	private LineSegment createLineSegment(Point point, Point[] points, int from, int to) {

		Point lowerBound = point;
		Point upperBound = points[from];
		if (lowerBound.compareTo(upperBound) > 0) {
			lowerBound = points[from];
			upperBound = point;
		}
		for (int x = from + 1; x <= to; x++) {
			if (points[x].compareTo(upperBound) > 0)
				upperBound = points[x];
			if (points[x].compareTo(lowerBound) < 0)
				lowerBound = points[x];
		}
		return point == lowerBound ? new LineSegment(lowerBound, upperBound) : null;
	}

	public static void main(String[] args) {
		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 30000);
		StdDraw.setYscale(0, 30000);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
