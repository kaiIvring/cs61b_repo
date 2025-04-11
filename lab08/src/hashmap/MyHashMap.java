package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private final int INITIALCAPACITY = 16;
    private final double LOADFACTOR = 0.75;
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double loadfactor;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = (Collection<Node>[]) new Collection[INITIALCAPACITY];
        size = 0;
        loadfactor = LOADFACTOR;
    }

    public MyHashMap(int initialCapacity) {
        buckets = new Collection[initialCapacity];
        size = 0;
        loadfactor = LOADFACTOR;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        buckets = new Collection[initialCapacity];
        size = 0;
        this.loadfactor = loadFactor;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
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
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null!");
        }
        int index = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }
        //  if the key has already exist
        for (Node tmpNode : buckets[index]) {
            if (tmpNode.key.equals(key)) {
                tmpNode.value = value;
                return;
            }
        }

        // add new node
        Node newNode = new Node(key, value);
        buckets[index].add(newNode);
        size++;
        // resize up
        if ( (double) size / buckets.length >= loadfactor) {
            // buckets = resize(buckets);
            resize();
        }
    }

    private void resize() {
        MyHashMap<K, V> newMap = new MyHashMap<>(buckets.length * 2, loadfactor);
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    newMap.put(node.key, node.value);
                }
            }
        }
        this.buckets = newMap.buckets;
        this.size = newMap.size;
    }

    private Collection<Node>[] resize(Collection<Node>[] buckets) {
        MyHashMap<K, V> newMap = new MyHashMap<>(buckets.length * 2);
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] != null) {
                for (Node tmpNode : buckets[i]) {
                    newMap.put(tmpNode.key, tmpNode.value);
                }
            }
        }
        return newMap.buckets;
    }


    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        // find the bucket
        int index = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return null;
        }

        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }

        return null;
    }

    /**
     * Returns whether this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return false;
        }

        for (Node node : bucket) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
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
        this.size = 0;
        this.buckets = new Collection[buckets.length];
    }

    /**
     * Returns a Set view of the keys contained in this map. Not required for this lab.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        Set<K> returnSet = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    returnSet.add(node.key);
                }
            }
        }
        return returnSet;
    }

    /**
     * Removes the mapping for the specified key from this map if present,
     * or null if there is no such mapping.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> bucket = buckets[index];
        if (bucket == null) {
            return null;
        }

        // use iterator to iterate through the bucket and remove the key
        Iterator<Node> iter = bucket.iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            if (node.key.equals(key)) {
                iter.remove();
                size--;
                return node.value;
            }
        }
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private int bucketIndex; // the "bucketIndexth" bucket
        private Iterator<Node> bucketIterator; // iterator for current bucket(Collection<Node>), iterate through current bucket

        public MyHashMapIterator() {
            bucketIndex = 0;
            advanceToNextNonEmptyBucket();
        }

        private void advanceToNextNonEmptyBucket() {
            while (bucketIndex < buckets.length) {
                if (buckets[bucketIndex] != null && !buckets[bucketIndex].isEmpty()) {
                    bucketIterator = buckets[bucketIndex].iterator();
                    return; // once we find the non-empty bucket, we return.
                }
                bucketIndex++;
            }
            bucketIterator = null; // all nodes have been visited
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
            if (bucketIterator == null) {
                return false;
            }
            if (bucketIterator.hasNext()) {
                return true;
            }
            bucketIndex++;
            advanceToNextNonEmptyBucket();
            return bucketIterator != null && bucketIterator.hasNext();
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
            return bucketIterator.next().key;
        }
    }
    // Your code won't compile until you do so!
}
