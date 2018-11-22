package com.algorithms.divideconquer;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Point {

	double x = 0;
	double y = 0;
	private static final Pattern PARSE_PATTERN = Pattern.compile("\\(([-+]?\\d+(\\.\\d+)?),([-+]?\\d+(\\.\\d+)?)\\)");
	
	public Point(final double x, final double y) { //Save x, y coordinates
		this.x = x;
		this.y = y;
	}
	
	// parse (x,y) to Point object
	public static Point parse(String pointString) {
        Matcher matcher = PARSE_PATTERN.matcher(pointString);
        matcher.matches();
        
        double x = Double.parseDouble(matcher.group(1));
        double y = Double.parseDouble(matcher.group(3));
        return new Point(x, y);
	}
	
	public static double distance(Point point1, Point point2) {
	    double xGap = point1.x - point2.x;
        double yGap = point1.y - point2.y;
        return Math.sqrt(Math.pow(xGap, 2) + Math.pow(yGap, 2));
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", this.x, this.y);
    }
    
    public static final Comparator<Point> xComparator = Comparator.comparingDouble(point -> point.x);
    
    public static final Comparator<Point> yComparator = Comparator.comparingDouble(point -> point.y);

}