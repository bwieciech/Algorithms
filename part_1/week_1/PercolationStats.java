import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final int trials;
    private final double[] results;

    public PercolationStats(int n, int trials) {
        if (n < 1) {
            throw new IllegalArgumentException(String.format("n required to be positive, got %d", n));
        }
        if (trials < 1) {
            throw new IllegalArgumentException(String.format("trials required to be positive, got %d", trials));
        }

        this.trials = trials;
        this.results = new double[trials];
        for (int trial = 0; trial < trials; ++trial) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int randRow = StdRandom.uniform(1, n + 1);
                int randCol = StdRandom.uniform(1, n + 1);
                percolation.open(randRow, randCol);
            }
            results[trial] = (1.0 / (n * n)) * percolation.numberOfOpenSites();
        }
    }

    public double mean() {
        return StdStats.mean(this.results);
    }

    public double stddev() {
        return StdStats.stddev(this.results);
    }

    public double confidenceLo() {
        return this.mean() - (CONFIDENCE_95 * this.stddev()) / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        return this.mean() + (CONFIDENCE_95 * this.stddev()) / Math.sqrt(this.trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        System.out.printf("mean                     = %f\n", ps.mean());
        System.out.printf("stddev                   = %f\n", ps.stddev());
        System.out.printf("95%% confidence interval  = [%f, %f]", ps.confidenceLo(), ps.confidenceHi());
    }

}