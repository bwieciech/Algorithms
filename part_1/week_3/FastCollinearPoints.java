import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FastCollinearPoints {

    private static final int MIN_SLIDING_WINDOW_SIZE = 4;

    private final List<LineSegment> listOfSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        List<LineSegment> listOfSegmentsTmp = new ArrayList<>();
        Point[] sortedPoints = Arrays.stream(points).sorted(this::pointsFirstRunComparator).toArray(Point[]::new);
        ValidateInput.validateSortedInput(sortedPoints);
        Point[] slopeOrderedPoints = Arrays.stream(sortedPoints).toArray(Point[]::new);
        for (Point referencePoint : sortedPoints) {
            Arrays.sort(slopeOrderedPoints, referencePoint.slopeOrder());
            listOfSegmentsTmp.addAll(this.getLineSegmentsContainingReferencePoint(referencePoint, slopeOrderedPoints));
            Arrays.sort(slopeOrderedPoints);
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

    private List<LineSegment> getLineSegmentsContainingReferencePoint(Point referencePoint, Point[] slopeOrderedPoints) {
        List<LineSegment> lineSegments = new ArrayList<>();
        int minSlidingWindowSize = MIN_SLIDING_WINDOW_SIZE - 1;  // -1 to account for the reference point
        int lo = 1;
        int hi = lo + minSlidingWindowSize;

        while (hi <= slopeOrderedPoints.length) {
            if (hi - lo == minSlidingWindowSize) {
                boolean isCollinear = this.isCollinear(referencePoint, slopeOrderedPoints, lo, hi);
                if (!isCollinear) {
                    lo++;
                }
                hi++;
            } else {
                boolean isCollinear = this.isPreviousAndCurrentPointCollinear(referencePoint, slopeOrderedPoints, hi);
                if (isCollinear) {
                    hi++;
                } else {
                    Point[] sortedSubset = this.copyAndSortSubset(slopeOrderedPoints, lo, hi - 1, referencePoint);
                    if (sortedSubset[0] == referencePoint) {  // check if reference point constitutes line beginning to avoid duplicates
                        lineSegments.add(new LineSegment(referencePoint, sortedSubset[sortedSubset.length - 1]));
                    }
                    lo = hi - 1;
                    hi = lo + minSlidingWindowSize;
                }
            }
        }

        if (this.boundsConstituteInvalidSegment(slopeOrderedPoints, lo, hi, referencePoint, minSlidingWindowSize)) {
            return lineSegments;
        }
        Point[] sortedSubset = this.copyAndSortSubset(slopeOrderedPoints, lo, slopeOrderedPoints.length, referencePoint);
        if (sortedSubset[0] == referencePoint) {
            lineSegments.add(new LineSegment(referencePoint, sortedSubset[sortedSubset.length - 1]));
        }
        return lineSegments;
    }

    private boolean boundsConstituteInvalidSegment(
            Point[] slopeOrderedPoints,
            int lo,
            int hi,
            Point referencePoint,
            int minSlidingWindowSize
    ) {
        return ((hi - lo) == minSlidingWindowSize && (hi > slopeOrderedPoints.length)) ||
               (hi > slopeOrderedPoints.length + 1) ||
               (
                   (hi - lo == minSlidingWindowSize) &&
                   !this.isCollinear(referencePoint, slopeOrderedPoints, hi - minSlidingWindowSize, hi - 1)
               );
    }

    private boolean isPreviousAndCurrentPointCollinear(Point referencePoint, Point[] slopeOrderedPoints, int idx) {
        return this.isCollinear(referencePoint, slopeOrderedPoints, idx - 2, idx);
    }

    private boolean isCollinear(
            Point point,
            Point[] slopeOrderedPoints,
            int lo,
            int hi
    ) {
        double slope = point.slopeTo(slopeOrderedPoints[lo]);
        for (int i = lo; i < hi; ++i) {
            if (point.slopeTo(slopeOrderedPoints[i]) != slope) {
                return false;
            }
        }
        return true;
    }

    private Point[] copyAndSortSubset(Point[] arr, int lo, int hi, Point referencePoint) {
        return Stream.concat(
                Arrays.stream(Arrays.copyOfRange(arr, lo, hi)),
                Stream.of(referencePoint)
        ).sorted().toArray(Point[]::new);
    }
}
