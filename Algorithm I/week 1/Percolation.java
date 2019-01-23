/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF wquf;
    private boolean[][] openFlag;
    private int numOfOpenSites;
    private final int rowOfGrid;
    private final int colOfGrid;

    public Percolation(int n)                // create n-by-n grid, with all sites blocked
    {
        if (n <= 0) throw new IllegalArgumentException("Invalid input : n must > 0 !");
        // PercolationVisualizer start from 1!
        colOfGrid = n + 1;
        rowOfGrid = colOfGrid;
        wquf = new WeightedQuickUnionUF(rowOfGrid * colOfGrid);
        openFlag = new boolean[rowOfGrid][colOfGrid];
        numOfOpenSites = 0;
        for (int i = 0; i < colOfGrid; i++) {
            wquf.union(0, i);
            openFlag[0][i] = true;
            // wquf.union((rowOfGrid - 2) * colOfGrid + 1, (rowOfGrid - 2) * colOfGrid + 1 + i);
            // openFlag[rowOfGrid - 1][i] = true;
        }
    }

    public void open(int row, int col)    // open site (row, col) if it is not open already
    {
        validate(row, col);
        if (col == 0 || isOpen(row, col)) {
            return;
        }
        openFlag[row][col] = true;
        numOfOpenSites++;
        int position = getPosition(row, col);
        unionSite(position, row - 1, col); // up
        unionSite(position, row + 1, col); // down
        unionSite(position, row, col - 1); // left
        unionSite(position, row, col + 1); // right
    }

    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        validate(row, col);
        return openFlag[row][col];
    }

    public boolean isFull(int row, int col)  // is site (row, col) full?
    {
        validate(row, col);
        return wquf.connected(0, getPosition(row, col)) && openFlag[row][col];
    }

    public int numberOfOpenSites()       // number of open sites
    {
        return numOfOpenSites;
    }

    public boolean percolates()              // does the system percolate?
    {
        for (int i = 0; i < colOfGrid; i++) {
            if (wquf.connected(0, rowOfGrid * colOfGrid - 1 - i)) {
                return true;
            }
        }
        return false;
        // return wquf.connected(0, (rowOfGrid - 1) * colOfGrid - 1);
    }

    private void unionSite(int position, int i, int j) {
        if (i >= 0 && i < rowOfGrid && j >= 0 && j < colOfGrid && openFlag[i][j]) {
            wquf.union(position, getPosition(i, j));
        }
    }

    private static void printGrid(Percolation per) {
        for (int i = 0; i < (per.rowOfGrid) * per.colOfGrid; i++) {
            StdOut.printf("%3d ", per.wquf.find(i));
            if (i % per.colOfGrid == (per.colOfGrid - 1)) {
                StdOut.println();
            }
        }
    }

    private void validate(int i, int j) {
        if (i <= 0 || j <= 0 || i >= rowOfGrid || j >= colOfGrid)
            throw new IllegalArgumentException("Invalid rowcol!");
    }

    private int getPosition(int i, int j) {
        return i * (colOfGrid) + j;
    }

    public static void main(String[] args)   // test client (optional)
    {
        In in = new In(args[0]);
        int n = in.readInt();
        Percolation per = new Percolation(n);
        printGrid(per);

        while (!per.percolates() && !in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            if (per.isOpen(i, j)) continue;
            per.open(i, j);
            StdOut.println("========================");
            printGrid(per);
        }
    }
}
