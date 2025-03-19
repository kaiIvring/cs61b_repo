import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class Node {
        private K key;
        private V value;
        private Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private int size;
    public BSTMap() {
        root = null;
        size = 0;
    }

    public int compareRoots(BSTMap<K, V> other) {
        if (this.root == null && other.root == null) {
            return 0;
        } else if (this.root == null) {
            return -1;
        } else if (other.root == null) {
            return 1;
        }
        return this.root.key.compareTo(other.root.key);
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map already contains the specified key, replaces the key's mapping
     * with the value specified.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(root, key, value);
    }
    private Node putHelper(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
           node.left = putHelper(node.left, key, value);
        } else if (cmp > 0) {
           node.right = putHelper(node.right, key, value);
        } else {
            node.value = value;
        }

        return node;
    }


    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        return getHelper(root, key);
    }
    private V getHelper(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            return getHelper(node.right, key);
        } else if (cmp < 0) {
            return getHelper(node.left, key);
        } else {
            return node.value;
        }
    }


    /**
     * Returns whether this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        return containsHelper(root, key);
    }
    private boolean containsHelper(Node node, K key) {
        if (node == null) {
            return false;
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            return containsHelper(node.right, key);
        } else if (cmp < 0) {
            return containsHelper(node.left, key);
        } else {
            return true;
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes every mapping from this map.
     */
    @Override
    public void clear() {
        // simply set root and size, java will do the rest for you.
        root = null;
        size = 0;
    }

    /**
     * Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new TreeSet<>();
        collectKeys(root, keys);
        return keys;
    }
    private void collectKeys(Node node, Set<K> keys) {
        if (node == null) {
            return;
        }

        keys.add(node.key);
        collectKeys(node.left, keys);
        collectKeys(node.right, keys);
    }

    /**
     * Removes the mapping for the specified key from this map if present,
     * or null if there is no such mapping.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        V removeValue = get(key);
        if (removeValue != null) {
            root = removeHelper(root, key); // root may change
            size--;
        }
        return removeValue;
    }
    private Node removeHelper(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeHelper(node.left, key);
        } else if (cmp > 0) {
            node.right = removeHelper(node.right, key);
        } else {
            // find the remove node
            if (node.left == null) {
                return node.right; // only right childNode or no childNode
            } else if (node.right == null) {
                return node.left; // only left childNode
            } else {
                // two childNode exist
                // the minimum node of the right childTree
                Node successor = findMin(node.right);
                // replace current node with successor
                node.key = successor.key;
                node.value = successor.value;
                // remove successor
                node.right = removeHelper(node.right, successor.key);
            }
        }
        return node;
    }
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new BSTIterator(root);
    }

    private class BSTIterator implements Iterator<K> {
        private Stack<Node> stack;

        BSTIterator(Node root) {
            stack = new Stack<>();
            pushLeft(root);
        }

        private void pushLeft(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = stack.pop();
            pushLeft(node.right);

            return node.key;
        }
    }
}
