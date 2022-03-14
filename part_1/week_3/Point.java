import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (that == null) {
            throw new NullPointerException("Cannot compare with null point");
        }

        if (this.y > that.y) {
            return 1;
        } else if (this.y < that.y) {
            return -1;
        }

        if (this.x > that.x) {
            return 1;
        } else if (this.x < that.x) {
            return -1;
        }
        return 0;
    }

    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (this.y == that.y) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        return 0.0 + ((double) that.y - this.y) / (that.x - this.x);
    }

    public Comparator<Point> slopeOrder() {
        return (Point p1, Point p2) -> {
            double slope1 = this.slopeTo(p1);
            double slope2 = this.slopeTo(p2);
            return Double.compare(slope1, slope2);
        };
    }
}