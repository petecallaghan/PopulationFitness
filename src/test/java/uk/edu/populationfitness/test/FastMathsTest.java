package uk.edu.populationfitness.test;

import org.junit.Assert;
import org.junit.Test;
import uk.edu.populationfitness.models.fastmaths.FastMaths;

import java.util.ArrayList;
import java.util.List;

public class FastMathsTest {
    Double offset = 1.1234e-1;

    @Test public void testTrendLine(){
        testSlope(0.75, 100);
        testSlope(0.0001, 100);
        testSlope(-0.75, 100);
        testSlope(-0.0001, 100);
        testSlope(0.0, 100);
    }

    private void testSlope(Double slope, long xValues) {
        List<Double> points = generateStraightLine(slope, xValues);

        Assert.assertEquals(slope, FastMaths.linearTrendLineSlope(points), 0.0000001);
    }

    private List<Double> generateStraightLine(Double slope, long xValues) {
        List<Double> points = new ArrayList<Double>();

        for(long x = 0; x < xValues; x++ ){
            final Double y = x * slope + offset;
            points.add(y);
        }
        return points;
    }
}

