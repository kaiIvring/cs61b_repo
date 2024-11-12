package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    private Node sentinel;
    private int size;

    @Override
    public Iterator<T> iterator() {
        return new LLDIterator();
    }

    private class LLDIterator implements Iterator<T> {
        private Node currentNode;

        public LLDIterator() {
            currentNode = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return currentNode != sentinel;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T returnItem = currentNode.item;
            currentNode = currentNode.next;
            return returnItem;
        }
    }

    private class Node {
        public T item;
        public Node next;
        public Node prev;

        public Node (T i,Node n,Node p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node newNode = new Node(x,sentinel.next,sentinel);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    @Override
    public void addLast(T x) {
        Node newNode = new Node(x,sentinel,sentinel.prev);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node currNode = sentinel.next;
        while (currNode != sentinel) {
            returnList.add(currNode.item);
            currNode = currNode.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        Node currNode = sentinel.next;
        if (currNode == sentinel) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        Node currNode = sentinel.next;
        if(currNode == sentinel) {
            return null;
        }
        currNode.next.prev = sentinel;
        sentinel.next = currNode.next;
        size -= 1;
        return currNode.item;
    }

    @Override
    public T removeLast() {
        Node currNode = sentinel.prev;
        if (currNode == sentinel) {
            return null;
        }
        currNode.prev.next = sentinel;
        sentinel.prev = currNode.prev;
        size -= 1;
        return currNode.item;
    }

    @Override
    public T get(int index) {
        if(index < 0 || index > size - 1) {
            return null;
        }
        Node currNode = sentinel.next;
        while (index != 0) {
            currNode = currNode.next;
            index -= 1;
        }
        return currNode.item;
    }

    @Override
    public T getRecursive(int index) {
        if(index < 0 || index > size - 1) {
            return null;
        }
        Node currNode = sentinel.next;
        return getRecursiveHelper(index,currNode);
    }
    public T getRecursiveHelper(int index, Node currNode) {
        if (index == 0) {
            return currNode.item;
        }
        return getRecursiveHelper(index - 1, currNode.next);
    }
}
