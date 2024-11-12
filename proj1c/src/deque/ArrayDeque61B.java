package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayDeque61B<T> implements Deque61B<T> {
    private T[] items;
    private int size;
    // pointer, the position before and after the inserted element
    private int nextFirst;
    private int nextLast;

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int currentIndex;
        private int itemsIterated;

        public ArrayDequeIterator() {
            // Start from the first element in the deque
            currentIndex = Math.floorMod(nextFirst + 1, items.length);
            itemsIterated = 0;
        }

        @Override
        public boolean hasNext() {
            return itemsIterated < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
               throw new NoSuchElementException();
            }
            T item = items[currentIndex];
            currentIndex = (currentIndex + 1) % items.length;
            itemsIterated++;
            return item;
        }
    }

    public ArrayDeque61B() {
        items = (T[]) new Object[8];
        size = 0;
        // set nextFirst and nextLast as you like.
        nextFirst = 3;
        nextLast = 4;
    }

    public void resizeUp(int capacity) {
        T[] newList = (T[]) new Object[capacity];
        int start = Math.floorMod(nextFirst + 1, items.length);
        for (int i = 0; i < size; i++) {
            newList[i] = items[(start + i) % items.length];
        }
        items = newList;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    public void resizeDown(int capacity) {
        if (capacity < size) {
            throw new IllegalArgumentException("new capacity must be greater than or equal to the number of elements!");
        }
        T[] newList = (T[]) new Object[capacity];
        int start = Math.floorMod(nextFirst + 1, items.length);
        for (int i = 0; i < size; i++){
            newList[i] = items[(start + i) % items.length];
        }
        items = newList;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T x) {
        if (items.length == size) {
            resizeUp(items.length * 2);
        }
        items[nextFirst] = x;
        nextFirst = Math.floorMod(nextFirst - 1, items.length);
        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (items.length == size) {
            resizeUp(items.length * 2);
        }
        items[nextLast] = x;
        nextLast = Math.floorMod(nextLast + 1,items.length);
        size += 1;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int index = Math.floorMod(nextFirst + 1 + i, items.length);
            returnList.add(this.items[index]);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (items.length > 15 && size <= items.length / 4) {
            resizeDown(items.length / 2);
        }
        if (isEmpty()) {
            return null;
        }

        nextFirst = (nextFirst + 1) % items.length;
        T removeItem = items[nextFirst];
        items[nextFirst] = null;
        size -= 1;

        return removeItem;
    }

    @Override
    public T removeLast() {
        if (items.length > 15 && size <= items.length / 4) {
            resizeDown(items.length / 2);
        }
        if (isEmpty()) {
            return null;
        }
        nextLast = Math.floorMod(nextLast - 1, items.length);
        T removeItem = items[nextLast];
        items[nextLast] = null;
        size -= 1;
        return removeItem;
    }

    @Override
    public T get(int index) {
        if (index > items.length || index < 0) {
            return null;
        }
        return items[(nextFirst + index + 1) % items.length];
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

}
