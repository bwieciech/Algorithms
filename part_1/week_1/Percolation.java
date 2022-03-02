public class Percolation {

    private final boolean[] isOpen;
    private final int[] connections;
    private final int[] connectionsIgnoreVirtualLast;
    private int openSites;
    private final int n;

    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException(String.format("n required to be positive, got %d", n));
        }
        this.n = n;
        this.isOpen = new boolean[n * n + 2];  // n x n grid + 2 virtual sites
        for (int i = 1; i < 1 + n * n; ++i) {
            this.isOpen[i] = false;
        }
        this.connections = new int[n * n + 2];
        this.connectionsIgnoreVirtualLast = new int[n * n + 2];
        for (int i = 0; i < this.connections.length; ++i) {
            this.connections[i] = i;
            this.connectionsIgnoreVirtualLast[i] = i;
        }
        this.openSites = 0;
    }

    public void open(int row, int col) {
        this.assertCoordinatesValid(row, col);

        if (this.isOpen(row, col)) {
            return;
        }

        int idx = this.toSingleIndex(row, col);
        this.isOpen[idx] = true;

        for (int[] neighbor : this.getNeighbors(row, col)) {
            if (neighbor == null) {
                continue;
            }
            int neighRow = neighbor[0];
            int neighCol = neighbor[1];
            if (isOpen(neighRow, neighCol)) {
                int nIdx = this.toSingleIndex(neighRow, neighCol);
                this.connect(idx, nIdx, this.connections);
                this.connect(idx, nIdx, this.connectionsIgnoreVirtualLast);
            }
        }

        // potentially connect with virtual sites
        if (row == 1) {
            this.connect(0, idx, this.connections);
            this.connect(0, idx, this.connectionsIgnoreVirtualLast);
        }
        if (row == this.n) {
            this.connect(this.connections.length - 1, idx, this.connections);
        }
        this.openSites++;
    }

    public boolean isOpen(int row, int col) {
        this.assertCoordinatesValid(row, col);
        return this.isOpen[this.toSingleIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        this.assertCoordinatesValid(row, col);
        if (!this.isOpen(row, col)) {
            return false;
        }
        int idx = this.toSingleIndex(row, col);
        return this.hasSameRoot(0, idx, this.connectionsIgnoreVirtualLast);
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        return this.hasSameRoot(0, this.connections.length - 1, this.connections);
    }

    private int[][] getNeighbors(int row, int col) {
        int[][] result = new int[4][];  // explicitly told only to use java.lang and edu.princeton.cs.algs4 so no extendable lists I guess?
        if (this.existsEntry(row - 1, col)) {
            result[0] = new int[] {row - 1, col};
        }
        if (this.existsEntry(row + 1, col)) {
            result[1] = new int[] {row + 1, col};
        }
        if (this.existsEntry(row, col - 1)) {
            result[2] = new int[] {row, col - 1};
        }
        if (this.existsEntry(row, col + 1)) {
            result[3] = new int[] {row, col + 1};
        }
        return result;
    }

    private boolean existsEntry(int row, int col) {
        return row >= 1 && row <= this.n && col >= 1 && col <= this.n;
    }

    private void connect(int i1, int i2, int[] arr) {
        int e1 = i1;
        int c1 = 0;

        int e2 = i2;
        int c2 = 0;

        while (e1 != arr[e1]) {
            c1++;
            int tmp = e1;
            e1 = arr[e1];
            arr[tmp] = arr[e1];
        }

        while (e2 != arr[e2]) {
            c2++;
            int tmp = e2;
            e2 = arr[e2];
            arr[tmp] = arr[e2];
        }

        if (c1 > c2) {
            arr[e2] = e1;
        } else {
            arr[e1] = e2;
        }
    }

    private boolean hasSameRoot(int i1, int i2, int[] arr) {
        int e1 = i1;
        int e2 = i2;

        while (arr[e1] != e1 || arr[e2] != e2) {
            e1 = arr[e1];
            e2 = arr[e2];
        }
        return e1 == e2;
    }

    private int toSingleIndex(int row, int col) {
        return 1 + (row - 1) * this.n + (col - 1);
    }

    private void assertCoordinatesValid(int row, int col) {
        if (row < 1 || col < 1 || row > this.n || col > this.n) {
            throw new IllegalArgumentException(String.format("row and col required to be in range [1, n], got row = %d, col = %d", row, col));
        }
    }
}