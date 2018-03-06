import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
  private final TreeSet<Point2D> points = new TreeSet<>();
  public PointSET() { }
  public boolean isEmpty() { return points.isEmpty(); }
  public int size() { return points.size(); }
  public void insert(Point2D p) { points.add(check(p)); }
  public boolean contains(Point2D p) { return points.contains(check(p)); }
  public void draw() {
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    for (Point2D p : points) StdDraw.point(p.x(), p.y());
  }
  public Iterable<Point2D> range(RectHV rect) {
    check(rect);
    final ArrayList<Point2D> pointsInRange = new ArrayList<>();
    for (Point2D prospect: points) {
      if (rect.contains(prospect)) pointsInRange.add(prospect);
    }
    return pointsInRange;
  }
  public Point2D nearest(Point2D p) {
    check(p);
    if (isEmpty()) return null;
    Point2D closest = points.first();
    double d2 = closest.distanceSquaredTo(p);
    for (Point2D point: points) {
      final double newD2 = point.distanceSquaredTo(p);
      final boolean isNewMin = newD2 < d2;
      d2 = isNewMin ? newD2 : d2;
      closest = isNewMin ? point : closest;
    }
    return closest;
  }

  private <T> T check(T e) {
    if (e == null) {
      throw new IllegalArgumentException();
    }
    return e;
  }

  public static void main(String[] args) { /* Used for Testing */ }
}
