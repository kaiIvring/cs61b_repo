import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationQFStats {
    private final double mean;
    private final double stddev;
    private final double T;

    public PercolationQFStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        double[] ratio = new double[T];
        for (int i = 0; i < T; i += 1) {
            PercolationQF pqf = new PercolationQF(N);
            while (!pqf.percolates()) {
                int randRow = StdRandom.uniform(N);
                int randCol = StdRandom.uniform(N);
                pqf.open(randRow, randCol);
            }
            ratio[i] = ((double) pqf.numberOfOpenSites()) / (N * N);
        }

        this.mean = StdStats.mean(ratio);
        this.stddev = StdStats.stddev(ratio);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return mean - 1.96 * stddev / Math.sqrt(T);
    }

    public double confidenceHigh() {
        return mean + 1.96 * stddev / Math.sqrt(T);
    }

    public static void main(String[] args) {
        int trials = 100;
        for (int gridSize = 50; gridSize <= 300; gridSize += 50) {
            Stopwatch sw = new Stopwatch();
            PercolationQFStats pqfs = new PercolationQFStats(gridSize, trials);

            double elapsedTime = sw.elapsedTime();

            System.out.printf("Grid Size: %d x %d | Number of Trials: %d%n", gridSize, gridSize, trials);
            System.out.printf("The mean percolation threshold is %.2f%n", pqfs.mean());
            System.out.printf("The standard deviation of the percolation threshold is %.2f.%n", pqfs.stddev());
            System.out.printf("The 95%% confidence interval is [%.3f, %.3f].%n", pqfs.confidenceLow(), pqfs.confidenceHigh());
            System.out.printf("Total time taken: %.2f seconds.%n%n", elapsedTime);
        }
    }
}
