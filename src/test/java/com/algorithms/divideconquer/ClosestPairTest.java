package com.algorithms.divideconquer;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ClosestPairTest {
	
	@Parameters
    public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
		    { "(1,2)", "", "", 0.0 },
		    { "(1,2);(4,6)", "(1,2)", "(4,6)", 5.0 },
		    { "(1,2);(4,6);(1000,1000);(100,100)", "(1,2)", "(4,6)", 5.0 },
		    { "(1,100);(2,50);(100,100)", "(1,100)", "(2,50)", 50.009999 },
		    { "(1,150);(1,100);(50,100);(50,150)", "(1,100)", "(50,100)", 49.0 },
			{ "(2,3);(2,16);(3,9);(6,3);(7,7);(19,4);(10,11);(15,2);(15,19);(16,11);(17,13);(9,12)", "(10,11)", "(9,12)", 1.4142135623730951 },
		});
    }
    
    private ClosestPair closestPair;
    private Point[] inputPoints;
	private Double expectedDistance;
	private Point expectedPoint1;
	private Point expectedPoint2;
    
    public ClosestPairTest(String locationsString, String expectedPoint1String, String expectedPoint2String, double expectedDistance) {
    	this.inputPoints = buildLocations(locationsString);
    	this.expectedDistance = expectedDistance;
    	
    	if (!expectedPoint1String.isEmpty()) {
    	    this.expectedPoint1 = Point.parse(expectedPoint1String);
    	}
    	
    	if (!expectedPoint2String.isEmpty()) {
    	    this.expectedPoint2 = Point.parse(expectedPoint2String);
    	}
    	
    	this.closestPair = new ClosestPair();
    }

	private Point[] buildLocations(String locationsString) {
	    if (locationsString.isEmpty()) {
	        return new Point[] {};
	    }
	    
		return Arrays.stream(locationsString.split(";"))
			.map(locationString -> Point.parse(locationString))
			.toArray(Point[]::new);
	}

	@Test
	public void closestPair_validInput_validResult() {
	    this.closestPair.quickSort(this.inputPoints, Point.xComparator);
		double actualDistance = closestPair.findPairWithClosestDistance(inputPoints);
		System.out.println(String.format("%s %s %s", this.closestPair.point1, this.closestPair.point2, actualDistance));
		
		assertEquals(expectedDistance, actualDistance, 0.0001);
		assertEquals(expectedPoint1, closestPair.point1);
		assertEquals(expectedPoint2, closestPair.point2);
	}
	
}
