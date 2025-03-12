import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    // TODO: Add any necessary instance variables.
    private int N;
    private boolean[][] grid; // true->open, false->blocked
    private WeightedQuickUnionUF uf;
    private int virtualTop;
    private int virtualBottom;
    private int openSites;

    public Percolation(int N) {
        // TODO: Fill in this constructor.
        if (N <= 0) {
            throw new IllegalArgumentException("Grid must be greater than 0!");
        }
        this.N = N;
        this.grid = new boolean[N][N];
        this.uf = new WeightedQuickUnionUF(N * N + 2); // extra two virtual sites
        this.virtualTop = N * N;
        this.virtualBottom = N * N + 1;
        this.openSites = 0;
    }

    private int xyTo1D(int row, int col) {
        return row * N + col;
    }

    private void validIndex(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        }
    }

    public void open(int row, int col) {
        // TODO: Fill in this method.
        validIndex(row, col);
        if (isOpen(row, col)) {
            return;
        }

        grid[row][col] = true;
        openSites++; // update openSites
        int index = xyTo1D(row, col);

        // if the site is at the first row, connect it to the virtualTop sites
        if (row == 0) {
            uf.union(index, virtualTop);
        }

        // same for the last row
        if (row == N - 1) {
            uf.union(index, virtualBottom);
        }

        // check the four directions, connecting to the "opened" neighbor
        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(index,xyTo1D(row - 1, col)); // up
        }
        if (row < N - 1 && isOpen(row + 1, col)) {
            uf.union(index, xyTo1D(row + 1, col)); // down
        }
        if (col > 0 && isOpen(row, col -1)) {
            uf.union(index, xyTo1D(row, col - 1)); // left
        }
        if (col < N - 1 && isOpen(row, col + 1)) {
            uf.union(index, xyTo1D(row, col + 1)); // right
        }
    }

    public boolean isOpen(int row, int col) {
        // TODO: Fill in this method.
        validIndex(row, col);
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        // TODO: Fill in this method.
        validIndex(row, col);
        return isOpen(row, col) && uf.connected(xyTo1D(row, col), virtualTop);
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return openSites;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return uf.connected(virtualTop, virtualBottom);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.

}
