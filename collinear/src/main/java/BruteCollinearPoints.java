import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
  private final ArrayList<LineSegment> segments;

  public BruteCollinearPoints(Point[] points) {
    segments = new ArrayList<>();
    Point[] pointsCopy = validatePoints(points);

    for (int p = 0; p < pointsCopy.length - 3; p++) {
      final Comparator<Point> slopeOrder = pointsCopy[p].slopeOrder();
      for (int q = p + 1; q < pointsCopy.length - 2; q++) {
        for (int r = q + 1; r < pointsCopy.length - 1; r++) {
          boolean firstThreeCollinear = slopeOrder.compare(pointsCopy[q], pointsCopy[r]) == 0;
          if (!firstThreeCollinear) continue;
          for (int s = r + 1; s < pointsCopy.length; s++) {
            boolean allCollinear = slopeOrder.compare(pointsCopy[q], pointsCopy[s]) == 0;
            if (allCollinear) {
              LineSegment segment = getSegment(pointsCopy[p], pointsCopy[q], pointsCopy[r], pointsCopy[s]);
              segments.add(segment);
            }
          }
        }
      }
    }
  }

  public int numberOfSegments() {
    return segments.size();
  }
  public LineSegment[] segments() {
    return segments.toArray(new LineSegment[segments.size()]);
  }
  private LineSegment getSegment(Point...points) {
    Point max = points[0];
    Point min = points[0];
    for (Point point : points) {
      max = max.compareTo(point) < 0 ? point : max;
      min = min.compareTo(point) > 0 ? point : min;
    }
    return new LineSegment(min, max);
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
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
