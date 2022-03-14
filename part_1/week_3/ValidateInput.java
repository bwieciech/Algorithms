public class ValidateInput {
    public static void validateSortedInput(Point[] sortedPoints) {
        for (int i = 0; i < sortedPoints.length; ++i) {
            if (sortedPoints[i] == null) {
                throw new IllegalArgumentException("Encountered null point in input");
            }
            if (i > 0 && sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0) {
                throw new IllegalArgumentException("Duplicate point found.");
            }
        }
    }
}
