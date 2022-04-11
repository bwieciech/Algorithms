import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private static class BoardWithPrevious {
        private final Board board;
        private final BoardWithPrevious previous;
        private final int distance;

        private BoardWithPrevious(Board board, BoardWithPrevious previous, int distance) {
            this.board = board;
            this.previous = previous;
            this.distance = distance;
        }

        private int distanceWithHeuristic() {
            return this.board.manhattan() + this.distance;
        }
    }

    private static class SolutionResult {
        private final boolean isSolvable;
        private final List<Board> solution;

        private SolutionResult(boolean isSolvable, List<Board> solution) {
            this.isSolvable = isSolvable;
            this.solution = solution;
        }

        private boolean isSolvable() {
            return this.isSolvable;
        }

        private Iterable<Board> solution() {
            if (!this.isSolvable) {
                throw new IllegalStateException("No solution for unsolvable board");
            }
            return this.solution;
        }
    }

    private final SolutionResult result;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Null initial board passed to constructor");
        }
        this.result = this.solve(initial);
    }

    public boolean isSolvable() {
        return this.result.isSolvable();
    }

    public int moves() {
        if (!this.result.isSolvable) {
            return -1;
        }
        return this.result.solution.size() - 1;
    }

    public Iterable<Board> solution() {
        if (!this.result.isSolvable()) {
            return null;
        }
        return this.result.solution();
    }

    public static void main(String[] args) {
        int[][] tiles = new int[][]{
                new int[]{1, 4, 3},
                new int[]{2, 6, 5},
                new int[]{7, 8, 0},
        };
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        System.out.println(solver.isSolvable());
        System.out.println(solver.moves());
        System.out.println(solver.solution());
    }

    private SolutionResult solve(Board initial) {
        Board twin = initial.twin();

        MinPQ<BoardWithPrevious> pq = new MinPQ<>(Comparator.comparingInt(BoardWithPrevious::distanceWithHeuristic));
        pq.insert(new BoardWithPrevious(initial, null, 0));
        MinPQ<BoardWithPrevious> pqTwin = new MinPQ<>(Comparator.comparingInt(BoardWithPrevious::distanceWithHeuristic));
        pqTwin.insert(new BoardWithPrevious(twin, null, 0));

        NaiveCache<Board> cache = new NaiveCache<>(100);
        NaiveCache<Board> twinCache = new NaiveCache<>(100);

        BoardWithPrevious solution = null;
        BoardWithPrevious twinSolution = null;

        while ((solution == null) && (twinSolution == null)) {
            solution = this.solveStep(pq, cache);
            twinSolution = this.solveStep(pqTwin, twinCache);
        }

        if (solution != null) {
            return new SolutionResult(true, this.extractSteps(solution));
        }
        return new SolutionResult(false, null);
    }

    private BoardWithPrevious solveStep(MinPQ<BoardWithPrevious> pq, NaiveCache<Board> cache) {
        if (pq.isEmpty()) {
            return null;
        }
        BoardWithPrevious boardWithPrevious = pq.delMin();
        if (cache.contains(boardWithPrevious.board)) {
            return null;
        }

        cache.push(boardWithPrevious.board);
        if (boardWithPrevious.board.isGoal()) {
            return boardWithPrevious;
        }
        boardWithPrevious.board.neighbors().forEach(n -> {
            if (cache.contains(n)) {
                return;
            }
            pq.insert(new BoardWithPrevious(n, boardWithPrevious, boardWithPrevious.distance + 1));
        });
        return null;
    }

    private List<Board> extractSteps(BoardWithPrevious boardWithPrevious) {
        List<Board> result = new ArrayList<>();
        while (boardWithPrevious != null) {
            result.add(boardWithPrevious.board);
            boardWithPrevious = boardWithPrevious.previous;
        }
        Collections.reverse(result);
        return result;
    }
}