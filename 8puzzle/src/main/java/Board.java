import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {
  private final int hamming;
  private final int manhattan;
  private final short dimension;
  private final short[][] blocks;
  private short zeroBlockNum;
  private short randomBlock1;
  private short randomBlock2;

  public Board(int[][] blocks) {
    dimension = (short) blocks.length;
    this.blocks = new short[dimension][dimension];
    final int numOfBlocks = dimension * dimension;
    int hammingSum = 0;
    int manhattanSum = 0;
    for (int i = 0; i < blocks.length; i++) {
      for (int j = 0; j < blocks[i].length; j++) {
        this.blocks[i][j] = (short)blocks[i][j];
        if (blocks[i][j] == 0) {
          zeroBlockNum = coordinatesToNum(dimension, i, j);
        } else {
          boolean isCorrect = coordinatesToNum(dimension, i, j) == blocks[i][j];
          hammingSum += isCorrect ? 0 : 1;
          int[] correctPlace = numToCoordinates(dimension, blocks[i][j]);
          manhattanSum += (Math.abs(correctPlace[0] - i) + Math.abs(correctPlace[1] - j));
          if (blocks[i][j] == 0) zeroBlockNum = coordinatesToNum(dimension, i, j);
        }
      }
    }
    randomBlock1 = (short) StdRandom.uniform(numOfBlocks);
    randomBlock2 = (short) StdRandom.uniform(numOfBlocks);
    while (randomBlock1 == zeroBlockNum) randomBlock1 = (short) StdRandom.uniform(numOfBlocks);
    while (randomBlock2 == zeroBlockNum || randomBlock2 == randomBlock1) randomBlock2 = (short) StdRandom.uniform(numOfBlocks);
    this.hamming = hammingSum;
    this.manhattan = manhattanSum;
  }

  public int dimension() { return dimension; }

  public int hamming() { return hamming; }

  public int manhattan() { return manhattan; }

  public boolean isGoal() { return manhattan == 0; }

  public Board twin() {
    int[][] twin = copyBoard();
    swapBlocks(twin, randomBlock1, randomBlock2);
    return new Board(twin);
  }

  public boolean equals(Object y) {
    if (y == null) return false;
    if (y == this) return true;
    if (y.getClass() != this.getClass()) return false;
    Board otherBoard = (Board) y;
    if (this.dimension() != otherBoard.dimension()) return false;
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (this.blocks[i][j] != otherBoard.blocks[i][j]) return false;
      }
    }
    return true;
  }

  public String toString() {
    StringBuilder value = new StringBuilder();
    value.append(dimension + "\n");
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        value.append(" " + blocks[i][j]);
      }
      value.append("\n");
    }
    return value.toString();
  }

  public static void main(String[] args) { /* Used For Testing */ }

  public Iterable<Board> neighbors() {
    List<int[]> neighborSwaps = setupNeighborSwaps();
    List<Board> neighbors = new ArrayList<>(neighborSwaps.size());
    for (int[] swap : neighborSwaps) {
      int[][] board = copyBoard();
      swapBlocks(board, swap[0], swap[1]);
      neighbors.add(new Board(board));
    }
    return neighbors;
  }

  private List<int[]> setupNeighborSwaps() {
    final List<int[]> neighborSwaps = new ArrayList<>();
    final int numOfBlocks = dimension * dimension;
    final boolean zeroBlockOnRight = zeroBlockNum % dimension == 0;
    final boolean zeroBlockOnLeft = zeroBlockNum % dimension == 1;
    final boolean zeroBlockOnTop = zeroBlockNum == dimension || (zeroBlockNum / dimension == 0 && zeroBlockNum != 0);
    final boolean zeroBlockOnBottom = zeroBlockNum == 0 || (zeroBlockNum / dimension == (dimension - 1) && zeroBlockNum % dimension != 0);
    final int[] swapUp = {zeroBlockNum, mod(zeroBlockNum - dimension, numOfBlocks) };
    final int[] swapDown = {zeroBlockNum, mod(zeroBlockNum + dimension, numOfBlocks) };
    final int[] swapLeft = {zeroBlockNum, mod(zeroBlockNum - 1, numOfBlocks) };
    final int[] swapRight = {zeroBlockNum, mod(zeroBlockNum + 1, numOfBlocks) };
    if (!zeroBlockOnBottom) neighborSwaps.add(swapDown);
    if (!zeroBlockOnTop) neighborSwaps.add(swapUp);
    if (!zeroBlockOnLeft) neighborSwaps.add(swapLeft);
    if (!zeroBlockOnRight) neighborSwaps.add(swapRight);
    return neighborSwaps;
  }

  private static short coordinatesToNum(int dimension, int row, int col) {
    int num = (row * dimension + col + 1) % (dimension * dimension);
    return (short) num;
  }

  private static int[] numToCoordinates(int dimension, int value) {
    final int row = (value - 1) / dimension;
    final int column = (value - 1) % dimension;
    final int[] place = {
      value == 0 ? dimension - 1 : row,
      value == 0 ? dimension - 1 : column
    };
    return place;
  }

  private static void swapBlocks(int[][] blocks, int num1, int num2) {
    final int[] points1 = numToCoordinates(blocks.length, num1);
    final int[] points2 = numToCoordinates(blocks.length, num2);
    final int swap = blocks[points1[0]][points1[1]];
    blocks[points1[0]][points1[1]] = blocks[points2[0]][points2[1]];
    blocks[points2[0]][points2[1]] = swap;
  }

  private static int mod(int i, int base) {
    return i < 0 ? i % base + base : i % base;
  }

  private int[][] copyBoard() {
    int[][] copy = new int[dimension][dimension];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        copy[i][j] = blocks[i][j];
      }
    }
    return copy;
  }
}
