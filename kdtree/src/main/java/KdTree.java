import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;

public class KdTree {
  private KdNode<Point2D> root;

  public KdTree() { }
  public boolean isEmpty() { return root == null; }
  public int size() { return isEmpty() ? 0 : root.size(); }
  public void insert(Point2D p) {
    check(p);
    root = root == null ? new KdNode<>(p, Point2D.X_ORDER, Point2D.Y_ORDER) : root;
    root.insert(p);
  }
  public boolean contains(Point2D p) {
    check(p);
    return !isEmpty() && root.contains(p);
  }
  public void draw() {
    for (KdNode<Point2D> p : root.set()) {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      StdDraw.point(p.value.x(), p.value.y());
      StdDraw.setPenRadius();
      if (p.primaryComparator() == Point2D.X_ORDER) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(p.value.x(), 0, p.value.x(), 1);
      } else {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(0, p.value.y(), 1, p.value.y());
      }
    }
  }
  public Iterable<Point2D> range(RectHV rect) {
    final Point2D lowerLeft = new Point2D(check(rect).xmin(), rect.ymin());
    final Point2D upperRight = new Point2D(rect.xmax(), rect.ymax());
    return !isEmpty() ? root.range(lowerLeft, upperRight) : new ArrayList<>();
  }
  public Point2D nearest(Point2D p) {
    check(p);
    return !isEmpty() ? getClosestInTree(root, p) : null;
  }

  private Point2D getClosestInTree(KdNode<Point2D> tree, Point2D p) {
    final boolean inLeftSubtree = tree.primaryComparator().compare(tree.value, p) > 0;
    double minDistance = Double.POSITIVE_INFINITY;
    KdNode<Point2D> firstNode = inLeftSubtree ? tree.left : tree.right;
    KdNode<Point2D> secondNode = inLeftSubtree ? tree.right : tree.left;
    Point2D closest = tree.value;
    if (firstNode != null) {
      Point2D bestFirst = getClosestInTree(firstNode, p);
      double distanceToBestFirst = p.distanceSquaredTo(bestFirst);
      double axisDistance = tree.primaryComparator() == Point2D.X_ORDER ?
          (tree.value.x() - p.x()) * (tree.value.x() - p.x()) :
          (tree.value.y() - p.y()) * (tree.value.y() - p.y());
      if (distanceToBestFirst <= axisDistance) return bestFirst;
      final double currentDistance = p.distanceSquaredTo(tree.value);
      closest = currentDistance < distanceToBestFirst ? closest : bestFirst;
      minDistance = currentDistance < distanceToBestFirst ? currentDistance : distanceToBestFirst;
    }
    minDistance = minDistance == Double.POSITIVE_INFINITY ? p.distanceSquaredTo(tree.value) : minDistance;
    if (secondNode != null) {
      Point2D bestSecond = getClosestInTree(secondNode, p);
      double distanceToBestSecond = p.distanceSquaredTo(bestSecond);
      closest = minDistance < distanceToBestSecond ?  closest : bestSecond;
    }
    return closest;
  }

  private <T> T check(T e) {
    if (e == null) throw new IllegalArgumentException();
    return e;
  }

  private static class KdNode<T> {
    private final Comparator<T> comp1;
    private final Comparator<T> comp2;
    private final T value;
    private KdNode<T> left;
    private KdNode<T> right;
    private int size;
    public KdNode(T value, Comparator<T> comp1, Comparator<T> comp2) {
      this.value = value;
      this.comp1 = comp1;
      this.comp2 = comp2;
      this.size = 1;
    }
    public Iterable<KdNode<T>> set() {
      final ArrayList<KdNode<T>> vals = new ArrayList<>();
      if (this.left != null) {
        for (KdNode<T> val: left.set()) vals.add(val);
      }
      vals.add(this);
      if (this.right != null) {
        for (KdNode<T> val: right.set()) vals.add(val);
      }
      return vals;
    }
    public void insert(T newVal) {
      if (value.equals(newVal)) return;
      boolean isGreater = comp1.compare(value, newVal) > 0;
      if (isGreater) {
        left = left == null ? new KdNode<>(newVal, comp2, comp1) : left;
        left.insert(newVal);
      } else {
        right = right == null ? new KdNode<>(newVal, comp2, comp1) : right;
        right.insert(newVal);
      }
      size = 1 + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
    }
    public boolean contains(T val) {
      if (value.equals(val)) return true;
      boolean isGreater = comp1.compare(value, val) > 0;
      KdNode<T> searchNode = isGreater ? left : right;
      return searchNode != null && searchNode.contains(val);
    }

    public Iterable<T> range(T bottomLeft, T upperRight) {
      final boolean isGteLower = comp1.compare(value, bottomLeft) >= 0;
      final boolean isLteUpper = comp1.compare(value, upperRight) <= 0;
      final boolean isIn2ndRange = comp2.compare(value, bottomLeft) >= 0 && comp2.compare(value, upperRight) <= 0;
      final boolean isInRange = isGteLower && isLteUpper && isIn2ndRange;
      final ArrayList<T> vals = new ArrayList<>();
      if (isLteUpper && right != null) {
        for (T lowerVal : right.range(bottomLeft, upperRight))
          vals.add(lowerVal);
      }
      if (isInRange) vals.add(this.value);
      if (isGteLower && left != null) {
        for (T higherVal: left.range(bottomLeft, upperRight))
          vals.add(higherVal);
      }
      return vals;
    }

    public int size() { return size; }
    public Comparator<T> primaryComparator() { return comp1; }

    public static void main(String[] args) { /* Used for Testing */ }
  }
}
