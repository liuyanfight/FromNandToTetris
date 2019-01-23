import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_196 = 1.96;
    private final double[] x;
    private final double avg;
    private final double dev;

    public PercolationStats(int n,
                            int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Invalid input : trials must > 0 !");
        x = new double[trials];
        while (trials > 0) {
            Percolation per = new Percolation(n);
            while (!per.percolates()) {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                if (per.isOpen(i, j)) continue;
                per.open(i, j);
            }
            x[trials - 1] = (double) per.numberOfOpenSites() / n / n;
            trials--;
        }
        avg = StdStats.mean(x);
        dev = StdStats.stddev(x);
    }

    public double mean()                          // sample mean of percolation threshold
    {
        return avg;
    }

    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return dev;
    }

    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return avg - (CONFIDENCE_196 * dev / Math.sqrt(x.length));
    }

    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return avg + (CONFIDENCE_196 * dev / Math.sqrt(x.length));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stat = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + stat.mean());
        StdOut.println("stddev                  = " + stat.stddev());
        StdOut.println(
                "95% confidence interval = [" + stat.confidenceLo() + ", " + stat.confidenceHi()
                        + "]");
    }
}
