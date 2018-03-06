import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private static final int TOP_NODE = 0;
  private final WeightedQuickUnionUF nodes;
  private final boolean[] isOpenSite;
  private int openSites = 0;
  private final int n;
  private final int numOfNodes;
  private final int bottomNode;

  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    this.numOfNodes = n * n;
    this.nodes = new WeightedQuickUnionUF(numOfNodes + 2);
    this.isOpenSite = new boolean[numOfNodes + 1];
    this.bottomNode = numOfNodes + 1;
  }

  public void open(int row, int col) {
    if (isOpen(row, col)) {
        return;
    }
    final int node = parseCoordinatesToNode(row, col);
    isOpenSite[node] = true;
    connectSurroundingNodes(node);
    openSites++;
  }

  public boolean isOpen(int row, int col) {
    final int node = parseCoordinatesToNode(row, col);
    return isOpenSite[node];
  }

  public boolean isFull(int row, int col) {
    final int node = parseCoordinatesToNode(row, col);
    return nodes.connected(0, node) && isOpenSite[node];
  }

  public int numberOfOpenSites() { return openSites; }

  public boolean percolates() {
    return nodes.connected(TOP_NODE, bottomNode);
  }

  private void connectSurroundingNodes(int i) {
    final boolean notLeftEdge = i % n != 1;
    final boolean notRightEdge = i % n != 0;
    final boolean isTopEdge = i / n == 0 || i == n;
    final boolean isBottomEdge = i / n == (n - 1) || i == numOfNodes;
    if (notLeftEdge && isOpenSite[i - 1]) nodes.union(i, i - 1);
    if (notRightEdge && isOpenSite[i + 1]) nodes.union(i, i + 1);
    if (!isTopEdge && isOpenSite[i - n]) nodes.union(i, i - n);
    if (!isBottomEdge && isOpenSite[i + n]) nodes.union(i, i + n);
    if (isBottomEdge) nodes.union(i, bottomNode);
    if (isTopEdge) nodes.union(i, TOP_NODE);
  }
  private int parseCoordinatesToNode(int row, int col) {
    boolean isInvalidRow = !isValidCoordinate(row);
    boolean isInvalidCol = !isValidCoordinate(col);
    if (isInvalidCol || isInvalidRow) {
      throw new IllegalArgumentException();
    }
    return coordinatesToNode(row, col);
  }
  private boolean isValidCoordinate(int x) { return x >= 1 && x <= n; }
  private int coordinatesToNode(int row, int col) { return (row-1) * n + col; }
}
