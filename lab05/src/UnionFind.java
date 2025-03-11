public class UnionFind {
    private int[] parent;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        parent = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        int root = find(v);
        return -parent(root);
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return parent[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v >= parent.length) {
            throw new IllegalArgumentException("Invalid index: " + v);
        }

        if (parent[v] < 0) {
            return v;
        }

        // recursive!
        return parent[v] = find(parent[v]);
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        int root_v1 = find(v1);
        int root_v2 = find(v2);

        if (root_v2 == root_v1) {
            return;
        }

        // get the positive result of size
        int size1 = -parent[root_v1];
        int size2 = -parent[root_v2];

        if (size1 > size2) {
            // update size
            parent[root_v1] += parent[root_v2];
            // update root
            parent[root_v2] = root_v1;
        } else {
            parent[root_v2] += parent[root_v1];
            parent[root_v1] = root_v2;
        }
    }
}

