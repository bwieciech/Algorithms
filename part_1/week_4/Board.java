import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private static class BoardPositionPair {
        private final BoardPosition bp1;
        private final BoardPosition bp2;

        private BoardPositionPair(BoardPosition bp1, BoardPosition bp2) {
            this.bp1 = bp1;
            this.bp2 = bp2;
        }

        private boolean pairEqual() {
            return this.bp1.row == this.bp2.row && this.bp1.col == this.bp2.col;
        }

        private boolean anyIsZero(int[][] tiles) {
            return tiles[bp1.row][bp1.col] == 0 || tiles[bp2.row][bp2.col] == 0;
        }
    }

    private static class BoardPosition {
        private final int row;
        private final int col;

        private BoardPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private final int[][] tiles;

    private Integer hamming;
    private Integer manhattan;
    private BoardPosition positionOfZero;
    private Board twin;
    private Iterable<Board> neighbors;

    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dimension());
        for (int[] row : this.tiles) {
            sb.append("\n");
            for (int i = 0; i < row.length; ++i) {
                sb.append(row[i]);
                if (i < row.length - 1) {
                    sb.append("  ");
                }
            }
        }
        return sb.toString();
    }

    public int dimension() {
        return this.tiles.length;
    }

    public int hamming() {
        if (this.hamming != null) {
            return this.hamming;
        }

        int outOfPlaceCount = 0;
        for (int row = 0; row < this.dimension(); ++row) {
            for (int col = 0; col < this.dimension(); ++col) {
                int value = tiles[row][col];
                if (value == 0) {
                    if (this.positionOfZero == null) {
                        this.positionOfZero = new BoardPosition(row, col);
                    }
                    continue;
                }
                int expectedValue = this.dimension() * row + col + 1;
                if (expectedValue != value) {
                    outOfPlaceCount++;
                }
            }
        }
        this.hamming = outOfPlaceCount;
        return outOfPlaceCount;
    }

    public int manhattan() {
        if (this.manhattan != null) {
            return this.manhattan;
        }

        int manhattan = 0;
        for (int row = 0; row < this.dimension(); ++row) {
            for (int col = 0; col < this.dimension(); ++col) {
                int value = this.tiles[row][col];
                if (value == 0) {
                    if (this.positionOfZero == null) {
                        this.positionOfZero = new BoardPosition(row, col);
                    }
                    continue;
                }
                int valueEff = value - 1;
                int expectedRow = valueEff / this.dimension();
                int expectedCol = valueEff % this.dimension();
                manhattan += Math.abs(row - expectedRow);
                manhattan += Math.abs(col - expectedCol);
            }
        }
        this.manhattan = manhattan;
        return manhattan;
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null)
            return false;

        if (this.getClass() != other.getClass())
            return false;

        Board otherBoard = (Board) other;

        if ((this.hamming() != otherBoard.hamming()) || (this.manhattan() != otherBoard.manhattan())) {
            return false;
        }

        for (int row = 0; row < this.dimension(); ++row) {
            for (int col = 0; col < this.dimension(); ++col) {
                if (this.tiles[row][col] != otherBoard.tiles[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    //  Really poor requirement of the grader not to have this in the API...
    //  @Override
    //  public int hashCode() {
    //      return Arrays.deepHashCode(this.tiles);
    //  }

    public Iterable<Board> neighbors() {
        if (this.neighbors != null) {
            return this.neighbors;
        }
        List<Board> neighbors = new ArrayList<>();
        BoardPosition positionOfZero = this.getPositionOfZero();
        for (BoardPosition candidate : this.getNeighborCandidates(positionOfZero)) {
            if (!this.isInBounds(candidate.row, candidate.col)) {
                continue;
            }
            int[][] tilesCopy = this.copyTiles();
            this.swap(
                    tilesCopy,
                    positionOfZero.row,
                    positionOfZero.col,
                    candidate.row,
                    candidate.col
            );
            neighbors.add(new Board(tilesCopy));
        }
        this.neighbors = neighbors;
        return this.neighbors;
    }

    public Board twin() {
        if (this.twin != null) {
            return this.twin;
        }

        BoardPositionPair bpp = this.getRandomBoardPositionPair();
        while (bpp.pairEqual() || bpp.anyIsZero(this.tiles)) {
            bpp = this.getRandomBoardPositionPair();
        }

        int[][] tilesCopy = this.copyTiles();
        this.swap(tilesCopy, bpp.bp1.row, bpp.bp1.col, bpp.bp2.row, bpp.bp2.col);
        this.twin = new Board(tilesCopy);
        return this.twin;
    }

    private BoardPosition getPositionOfZero() {
        if (this.positionOfZero != null) {
            return this.positionOfZero;
        }

        for (int row = 0; row < this.dimension(); ++row) {
            for (int col = 0; col < this.dimension(); ++col) {
                if (this.tiles[row][col] == 0) {
                    this.positionOfZero = new BoardPosition(row, col);
                    return this.positionOfZero;
                }
            }
        }
        throw new IllegalStateException("Zero argument not found, board corrupt");
    }

    private void swap(int[][] arr, int r1, int c1, int r2, int c2) {
        int tmp = arr[r1][c1];
        arr[r1][c1] = arr[r2][c2];
        arr[r2][c2] = tmp;
    }

    private int[][] copyTiles() {
        return Arrays.stream(this.tiles).map(int[]::clone).toArray(int[][]::new);
    }

    private BoardPositionPair getRandomBoardPositionPair() {
        int row1 = StdRandom.uniform(this.dimension());
        int col1 = StdRandom.uniform(this.dimension());
        int row2 = StdRandom.uniform(this.dimension());
        int col2 = StdRandom.uniform(this.dimension());
        return new BoardPositionPair(
                new BoardPosition(row1, col1),
                new BoardPosition(row2, col2)
        );
    }

    private List<BoardPosition> getNeighborCandidates(BoardPosition bp) {
        List<BoardPosition> candidates = new ArrayList<>();
        candidates.add(new BoardPosition(bp.row - 1, bp.col));
        candidates.add(new BoardPosition(bp.row, bp.col - 1));
        candidates.add(new BoardPosition(bp.row + 1, bp.col));
        candidates.add(new BoardPosition(bp.row, bp.col + 1));
        return candidates;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < this.dimension() && col < this.dimension();
    }
}