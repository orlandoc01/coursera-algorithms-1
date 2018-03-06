import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class FastCollinearPoints {

  private static final int NUM_OF_POINTS = 4;
  private final ArrayList<LineSegment> segments;

  public FastCollinearPoints(Point[] points) {
    // Sort points, ensure no null or duplicate values;
    Point[] pointsRef = validatePoints(points);
    segments = new ArrayList<>();

    // Setup storage for endpoints and a clone of points
    final ArrayList<Point[]> endpoints = new ArrayList<>();
    Point[] pointsClone = pointsRef.clone();

    // Iterate through each point, sort the clone of points by slope order to each point, check for duplicates
    // after sorting, and only add the endpoints if the duplicates occur more than NUM_OF_POINT times
    for (Point p: pointsRef) {
      Arrays.sort(pointsClone, p.slopeOrder());
      int j;
      for (int i = 1; i < pointsClone.length; i = i + j) {
        j = 1;
        while (j < pointsClone.length - i && p.slopeTo(pointsClone[i]) == p.slopeTo(pointsClone[i+j])) j++;
        if (j >= NUM_OF_POINTS - 1) endpoints.add(getEndpoints(p, Arrays.copyOfRange(pointsClone, i, i + j)));
      }
    }

    // Sort the resulting endpoints by the first point, and then the second
    Comparator<Point[]> byEndpoints = Comparator.comparing((Point[] end) -> end[0]).thenComparing(end -> end[1]);
    endpoints.sort(byEndpoints);

    // Only convert the distinct endpoints to line segments and add them to the final result
    for (int i = 0; i < endpoints.size(); i++) {
      Point[] currentEnds = endpoints.get(i);
      if (i == endpoints.size() - 1) segments.add(new LineSegment(currentEnds[0], currentEnds[1]));
      else if (byEndpoints.compare(currentEnds, endpoints.get(i + 1)) != 0) {
        segments.add(new LineSegment(currentEnds[0], currentEnds[1]));
      }
    }
  }
  private Point[] validatePoints(Point[] points) {
    if (points == null) throw new IllegalArgumentException();
    Point[] pointsCopy = points.clone();
    try {
      Arrays.sort(pointsCopy);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException(e);
    }
    if (pointsCopy.length > 0 && pointsCopy[0] == null) throw new IllegalArgumentException();
    for (int i = 1; i < pointsCopy.length; i++) {
      if (pointsCopy[i].compareTo(pointsCopy[i-1]) == 0) throw new IllegalArgumentException();
    }
    return pointsCopy;

  }
  public int numberOfSegments() {
    return segments.size();
  }
  public LineSegment[] segments() {
    return segments.toArray(new LineSegment[segments.size()]);
  }

  // Get the min and max endpoints of an array of endpoints
  private Point[] getEndpoints(Point p, Point[] points) {
    Point max = p;
    Point min = p;
    for (Point point : points) {
      max = max.compareTo(point) < 0 ? point : max;
      min = min.compareTo(point) > 0 ? point : min;
    }
    final Point[] endpoints = {min, max};
    return endpoints;
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
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
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