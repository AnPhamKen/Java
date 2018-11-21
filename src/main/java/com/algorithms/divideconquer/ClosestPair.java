package com.algorithms.divideconquer;

import java.io.IOException;
import java.util.Comparator;

/**
 * For a set of points in a coordinates system (10000 maximum), ClosestPair class calculates the two
 * closest points.
 */
public final class ClosestPair {

    // Input data, maximum 10000
    public Point point1;
    public Point point2;

    private double globalMinDistance = Double.MAX_VALUE;
    private int secondCount = 0;
    private double minValue = 0; // Minimum length

    public void quickSort(final Point[] points, final Comparator<Point> comparator) {
        doQuickSort(points, 0, points.length - 1, comparator);
    }

    private void doQuickSort(final Point[] points, final int first, final int last, final Comparator<Point> comparator) {
        if (first < last) {
            final int pivot = partition(points, first, last, comparator);
            doQuickSort(points, first, pivot - 1, comparator);
            doQuickSort(points, pivot + 1, last, comparator);
        }
    }

    private int partition(final Point[] points, final int first, final int last, final Comparator<Point> comparator) {
        final int pivot = last;
        int firstGreaterThanPivot = first - 1;

        for (int i = first; i < last; i++) {
            if (comparator.compare(points[i], points[pivot]) <= 0) {
                firstGreaterThanPivot++;
                swap(points, firstGreaterThanPivot, i);
            }
        }

        firstGreaterThanPivot++;
        swap(points, firstGreaterThanPivot, pivot);

        return firstGreaterThanPivot;
    }

    private void swap(final Point[] points, final int first, final int second) {
        Point temp = points[first];
        points[first] = points[second];
        points[second] = temp;
    }

    public double findPairWithClosestDistance(final Point[] inputPoints) {
        return doFindPairWithClosestDistance(inputPoints, inputPoints.length);
    }

    /**
     * Finds closest pair
     *
     * @param a         array stored before divide
     * @param indexNum  number coordinates divideArray
     * @return minimum distance
     */
    private double doFindPairWithClosestDistance(final Point[] a, final int indexNum) {
        Point[] divideArray = new Point[indexNum];
        System.arraycopy(a, 0, divideArray, 0, indexNum); // Copy previous array

        int totalNum = indexNum; // number of coordinates in the divideArray
        int divideX = indexNum / 2; // Intermediate value for divide

        // divide - left array
        Point[] leftArray = new Point[divideX];
        // divide-right array
        Point[] rightArray = new Point[totalNum - divideX];

        if (indexNum <= 3) { // If the number of coordinates is 3 or less
            return bruteForce(divideArray);
        }
        // divide-left array
        System.arraycopy(divideArray, 0, leftArray, 0, divideX);
        // divide-right array
        System.arraycopy(divideArray, divideX, rightArray, 0, totalNum - divideX);

        double minLeftArea = 0; // Minimum length of left array
        double minRightArea = 0; // Minimum length of right array

        minLeftArea = doFindPairWithClosestDistance(leftArray, divideX); // recursive closestPair
        minRightArea = doFindPairWithClosestDistance(rightArray, totalNum - divideX);
        // window size (= minimum length)
        minValue = Math.min(minLeftArea, minRightArea);

        Point[] firstWindow = this.createWindow(divideArray, totalNum, divideX);

        doQuickSort(firstWindow, 0, secondCount - 1, Point.yComparator);

        this.coordinateWindow(firstWindow, minValue);

        secondCount = 0;
        return minValue;
    }

    /**
     * Coordinates window
     *
     * @param firstWindow   window to update
     * @param minValue      minimum length
     */
    private void coordinateWindow(Point[] firstWindow, double minValue) {
        // size comparison within window
        for (int i = 0; i < secondCount - 1; i++) {
            for (int j = (i + 1); j < secondCount; j++) {
                double xGap = Math.abs(firstWindow[i].x - firstWindow[j].x);
                double yGap = Math.abs(firstWindow[i].y - firstWindow[j].y);
                if (yGap < minValue) {
                    this.updateWindow(firstWindow, xGap, yGap, i, j);
                } else {
                    break;
                }
            }
        }
    }

    /**
     *  Updates window
     *
     * @param firstWindow   window to update
     * @param xGap          x-axis gap
     * @param yGap          y-axis gap
     * @param idx1          1st-loop index
     * @param idx2          2nd-loop index
     */
    private void updateWindow(Point[] firstWindow, double xGap, double yGap, int idx1, int idx2) {
        double length = 0;
        length = Math.sqrt(Math.pow(xGap, 2) + Math.pow(yGap, 2));
        // If measured distance is less than current min distance
        if (length < minValue) {
            // Change minimum distance to current distance
            minValue = length;
            // Conditional for registering final coordinate
            if (length < globalMinDistance) {
                globalMinDistance = length;
                point1 = firstWindow[idx1];
                point2 = firstWindow[idx2];
            }
        }
    }

    /**
     * Creates window.  Set the size for creating a window and creating a new array for the coordinates in the window
     *
     * @param divideArray   array to divide
     * @param totalNum      number of coordinates in the divideArray
     * @param divideX       intermediate value for divide
     * @return window
     */
    private Point[] createWindow(Point[] divideArray, int totalNum, int divideX) {
        for (int i = 0; i < totalNum; i++) {
            double xGap = Math.abs(divideArray[divideX].x - divideArray[i].x);
            if (xGap < minValue) {
                secondCount++; // size of the array
            } else {
                if (divideArray[i].x > divideArray[divideX].x) {
                    break;
                }
            }
        }

        // new array for coordinates in window
        Point[] firstWindow = new Point[secondCount];
        int k = 0;
        for (int i = 0; i < totalNum; i++) {
            double xGap = Math.abs(divideArray[divideX].x - divideArray[i].x);
            if (xGap < minValue) { // if it's inside a window
                firstWindow[k] = divideArray[i]; // put in an array
                k++;
            } else {
                if (divideArray[i].x > divideArray[divideX].x) {
                    break;
                }
            }
        }

        return firstWindow;
    }

    /**
     * The brute force function: When the number of coordinates is less than 3
     *
     * @param inputPoints   array stored before divide
     * @return minimum distance
     */
    private double bruteForce(final Point[] inputPoints) {
        if (inputPoints.length == 1) {
            return 0.0;
        }

        double minDistance = Double.MAX_VALUE;
        double distance;

        for (int i = 0; i < inputPoints.length - 1; i++) {
            for (int j = (i + 1); j < inputPoints.length; j++) {
                distance = Point.distance(inputPoints[i], inputPoints[j]);

                if (distance < minDistance) {
                    minDistance = distance;
                    if (distance < this.globalMinDistance) {
                        this.globalMinDistance = distance;
                        this.point1 = inputPoints[i];
                        this.point2 = inputPoints[j];
                    }
                }
            }
        }
        return minDistance;
    }

    /**
     * Main function: execute class
     *
     * @param args (IN Parameter) <br>
     * @throws IOException If an input or output exception occurred
     */
    public static void main(final String[] args) {
        // Input data consists of one x-coordinate and one y-coordinate
        Point[] inputPoints = new Point[12];
        inputPoints[0] = new Point(2, 3);
        inputPoints[1] = new Point(2, 16);
        inputPoints[2] = new Point(3, 9);
        inputPoints[3] = new Point(6, 3);
        inputPoints[4] = new Point(7, 7);
        inputPoints[5] = new Point(19, 4);
        inputPoints[6] = new Point(10, 11);
        inputPoints[7] = new Point(15, 2);
        inputPoints[8] = new Point(15, 19);
        inputPoints[9] = new Point(16, 11);
        inputPoints[10] = new Point(17, 13);
        inputPoints[11] = new Point(9, 12);

        ClosestPair cp = new ClosestPair();

        System.out.println("Input data");
        System.out.println("Number of points: " + inputPoints.length);
        for (int i = 0; i < inputPoints.length; i++) {
            System.out.println("x: " + inputPoints[i].x + ", y: " + inputPoints[i].y);
        }

        cp.quickSort(inputPoints, Point.xComparator); // Sorting by x value

        double result; // minimum distance

        result = cp.findPairWithClosestDistance(inputPoints);
        // ClosestPair start
        // minimum distance coordinates and distance output
        System.out.println("Output Data");
        System.out.println("(" + cp.point1.x + ", " + cp.point1.y + ")");
        System.out.println("(" + cp.point2.x + ", " + cp.point2.y + ")");
        System.out.println("Minimum Distance : " + result);
    }
}
