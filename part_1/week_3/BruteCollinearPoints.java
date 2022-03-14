import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> listOfSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Null input not allowed");
        }
        List<LineSegment> listOfSegmentsTmp = new ArrayList<>();
        Point[] sortedPoints = Arrays.stream(points).sorted(this::pointsFirstRunComparator).toArray(Point[]::new);
        ValidateInput.validateSortedInput(sortedPoints);
        for (int i = 0; i < sortedPoints.length; ++i) {
            for (int j = i + 1; j < sortedPoints.length; ++j) {
                for (int k = j + 1; k < sortedPoints.length; ++k) {
                    for (int ll = k + 1; ll < sortedPoints.length; ++ll) {
                        LineSegment segment = this.findSegmentForSortedPoints(
                                sortedPoints[i],
                                sortedPoints[j],
                                sortedPoints[k],
                                sortedPoints[ll]
                        );
                        if (segment != null) {
                            listOfSegmentsTmp.add(segment);
                        }
                    }
                }
            }
        }
        this.listOfSegments = listOfSegmentsTmp;
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.listOfSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return this.listOfSegments.toArray(LineSegment[]::new);
    }

    private int pointsFirstRunComparator(Point p1, Point p2) {
        if (p1 == null) {
            return 1;
        } else if (p2 == null) {
            return -1;
        }
        return p1.compareTo(p2);
    }

    private LineSegment findSegmentForSortedPoints(Point p1, Point p2, Point p3, Point p4) {
        double slope1 = p1.slopeTo(p2);
        double slope2 = p1.slopeTo(p3);
        double slope3 = p1.slopeTo(p4);

        if ((slope1 == slope2) && (slope1 == slope3)) {
            return new LineSegment(p1, p4);  // takes advantage of points sorting
        }
        return null;
    }
}