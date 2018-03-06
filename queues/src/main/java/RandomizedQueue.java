import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private int size;
  private Object[] items;
  public RandomizedQueue() {
    size = 0;
    items = new Object[4];
  }
  public boolean isEmpty() { return size == 0; }
  public int size() { return size; }
  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    items[size++] = item;
    boolean needsDoubling = size > items.length / 2;
    if (needsDoubling) changeLength(items.length*2);
  }
  public Item dequeue() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    int newInt = StdRandom.uniform(size);
    swap(newInt, size - 1);
    Item dequeuedItem = (Item) items[--size];
    boolean needsHalfing = size < items.length / 4;
    if (needsHalfing) changeLength(items.length / 4);
    return dequeuedItem;
  }
  public Item sample() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    int newInt = StdRandom.uniform(size);
    swap(newInt, size - 1);
    return (Item) items[size - 1];
  }
  public Iterator<Item> iterator() { return new RandomizedDequedIterator(); }
  public static void main(String[] args) { /* Empty main method for testing*/ }

  private class RandomizedDequedIterator implements Iterator<Item> {
    int index;
    final Object[] values;
    private RandomizedDequedIterator() {
      shuffle();
      index = 0;
      values = new Object[size];
      for (int i = 0; i < size; i++) {
        values[i] = items[i];
      }
    }
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return (Item) values[index++];
    }
    public boolean hasNext() {
      return index < values.length && values[index] != null;
    }
  }
  private void swap(int i, int j) {
    Item swap = (Item) items[i];
    items[i] = items[j];
    items[j] = swap;
  }
  private void changeLength(int newLength) {
    Object[] oldItems = items;
    items = new Object[newLength];
    int lastIndex = Math.min(newLength, oldItems.length) - 1;
    for (int i = 0; i <= lastIndex; i++) {
      items[i] = oldItems[i];
    }
  }
  private void shuffle() {
    for (int i = 0; i < size; i++) {
      int randomIndex = StdRandom.uniform(i, size);
      swap(i, randomIndex);
    }
  }
}
