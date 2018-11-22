package com.algorithms.divideconquer;

import java.io.IOException;
import java.util.Comparator;

/**
 * For a set of points in a coordinates system, ClosestPair class calculates the two closest points.
 */
public final class ClosestPair {

    private Point point1;
    private Point point2;

    private double globalMinDistance = Double.MAX_VALUE;
    private int secondCount = 0;
    private double minValue = 0;

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    /**
     * Quick Sort
     *
     * @param points        array of points
     * @param comparator    comparator based on x or y
     */
    public void quickSort(final Point[] points, final Comparator<Point> comparator) {
        doQuickSort(points, 0, points.length - 1, comparator);
    }

    /**
     * Quick Sort implementation
     *
     * @param points        array of points
     * @param first         first index (usually 0)
     * @param last          last index (usually points.length - 1)
     * @param comparator    comparator based on x or y
     */
    private void doQuickSort(final Point[] points, final int first, final int last, final Comparator<Point> comparator) {
        if (first < last) {
            final int pivot = partition(points, first, last, comparator);
            doQuickSort(points, first, pivot - 1, comparator);
            doQuickSort(points, pivot + 1, last, comparator);
        }
    }

    /**
     * Partitioning an array of points
     *
     * @param points        array of points
     * @param first         first index (usually 0)
     * @param last          last index (usually points.length - 1)
     * @param comparator    comparator based on x or y
     * @return pivot
     */
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

    /**
     * Swaps 2 points
     *
     * @param points    array of points
     * @param first     point 1
     * @param second    point 2
     */
    private void swap(final Point[] points, final int first, final int second) {
        Point temp = points[first];
        points[first] = points[second];
        points[second] = temp;
    }

    /**
     * Finds pair with closest distance
     *
     * @param inputPoints   array of points
     * @return closest distance
     */
    public double findPairWithClosestDistance(final Point[] inputPoints) {
        return doFindPairWithClosestDistance(inputPoints, inputPoints.length);
    }

    /**
     * Finds closest pair
     *
     * @param a         array of points
     * @param indexNum  number coordinates divideArray
     * @return minimum distance
     */
    private double doFindPairWithClosestDistance(final Point[] a, final int indexNum) {
        Point[] divideArray = new Point[indexNum];
        System.arraycopy(a, 0, divideArray, 0, indexNum);

        int totalNum = indexNum;        // number of coordinates in the divideArray
        int divideX = indexNum / 2;     // Intermediate value for divide

        // divide - left array
        Point[] leftArray = new Point[divideX];
        // divide-right array
        Point[] rightArray = new Point[totalNum - divideX];

        if (indexNum <= 3) {
            // If the number of coordinates is 3 or less
            return bruteForce(divideArray);
        }
        // divide-left array
        System.arraycopy(divideArray, 0, leftArray, 0, divideX);
        // divide-right array
        System.arraycopy(divideArray, divideX, rightArray, 0, totalNum - divideX);

        double minLeftArea = 0;         // Minimum length of left array
        double minRightArea = 0;        // Minimum length of right array

        minLeftArea = doFindPairWithClosestDistance(leftArray, divideX);    // recursive closestPair
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
     * @param divideArray   array of points to divide
     * @param totalNum      number of coordinates in the divideArray
     * @param divideX       intermediate value for divide
     * @return window
     */
    private Point[] createWindow(Point[] divideArray, int totalNum, int divideX) {
        for (int i = 0; i < totalNum; i++) {
            double xGap = Math.abs(divideArray[divideX].x - divideArray[i].x);
            if (xGap < minValue) {
                secondCount++;          // size of the array
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
            if (xGap < minValue) {
                // if it's inside a window
                firstWindow[k] = divideArray[i];    // put in an array
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
     * @param args
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

        // Print input data
        System.out.println("Input data");
        System.out.println("Number of points: " + inputPoints.length);
        for (int i = 0; i < inputPoints.length; i++) {
            System.out.println("x: " + inputPoints[i].x + ", y: " + inputPoints[i].y);
        }

        // Quick sorting by x value
        ClosestPair cp = new ClosestPair();
        cp.quickSort(inputPoints, Point.xComparator);
        // Print sort result
        System.out.println("Quick sort result:");
        for (int i = 0; i < inputPoints.length; i++) {
            System.out.println("x: " + inputPoints[i].x + ", y: " + inputPoints[i].y);
        }

        // Find pair with closest distance
        double result; // minimum distance
        result = cp.findPairWithClosestDistance(inputPoints);
        // Print minimum distance coordinates and distance output
        System.out.println("Output Data");
        System.out.println("(" + cp.point1.x + ", " + cp.point1.y + ")");
        System.out.println("(" + cp.point2.x + ", " + cp.point2.y + ")");
        System.out.println("Minimum Distance : " + result);
    }
}
