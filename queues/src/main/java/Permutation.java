import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class Permutation {
  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    RandomizedQueue<String> outputLines = new RandomizedQueue<>();
    String item;
    boolean isNotEnd = true;
    while (isNotEnd) {
      try {
        item = StdIn.readString();
        outputLines.enqueue(item);
        if (outputLines.size() > k) outputLines.dequeue();
      } catch (NoSuchElementException e) {
        isNotEnd = false;
      }
    }
    for (String outputLine : outputLines) {
      StdOut.println(outputLine);
    }
  }
}
