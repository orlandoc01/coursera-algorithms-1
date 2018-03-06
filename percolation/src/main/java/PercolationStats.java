import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double INTERVAL_CONSTANT = 1.96;
  private final double mean;
  private final double stddev;
  private final double confidenceLo;
  private final double confidenceHi;

  public PercolationStats(int n, int trials) {
    double[] trialResults = new double[trials];
    for (int t = 0; t < trials; t++) {
      trialResults[t] = runTrial(n);
    }
    this.mean = StdStats.mean(trialResults);
    this.stddev = StdStats.stddev(trialResults);
    this.confidenceLo = mean - INTERVAL_CONSTANT * stddev * (1/Math.sqrt(trials));
    this.confidenceHi = mean + INTERVAL_CONSTANT * stddev * (1/Math.sqrt(trials));
  }
  public double mean() {
    return mean;
  }
  public double stddev() {
    return stddev;
  }
  public double confidenceLo() {
    return confidenceLo;
  }
  public double confidenceHi() {
    return confidenceHi;
  }

  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int t = Integer.parseInt(args[1]);
    PercolationStats results = new PercolationStats(n, t);
    StdOut.println("mean\t\t\t= " + results.mean());
    StdOut.println("stddev\t\t\t= " + results.stddev());
    StdOut.println("95% confidence interval\t= [" + results.confidenceLo() + ", " + results.confidenceHi() + "]");
  }

  private double runTrial(int n) {
    Percolation board = new Percolation(n);
    while (!board.percolates()) {
      int row = StdRandom.uniform(1, n+1);
      int col = StdRandom.uniform(1, n+1);
      board.open(row, col);
    }
    return (double) (board.numberOfOpenSites())/(double) (n * n);
  }
}
