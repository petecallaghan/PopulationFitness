package uk.edu.populationfitness.models.fastmaths;

import java.util.List;

public class FastMaths {
    /**
     * Optimised version of power
     * @param base
     * @param power
     * @return base raised to the power
     */
    public static double pow(double base, long power) {
        double result = 1;
        while (power > 0) {
            if ((power & 1) == 1) {
                result *= base;
            }
            power >>= 1;
            base *= base;
        }
        return result;
    }

    public static double linearTrendLineSlope(List<Double> values){
        final long n = values.size();
        final long sumOfX = sumOfNaturalNumbersToN(n);
        final long sumOfXSquared = sumOfNaturalNumbersToNSquared(n);
        final double sumOfY = values.stream().mapToDouble(y -> y).sum();

        double sumOfXY = 0.0;
        for(int x = 1; x <= n; x++){
            sumOfXY += x * values.get(x - 1);
        }

        final double alpha = (n * sumOfXY - sumOfX * sumOfY) / (n * sumOfXSquared - (sumOfX * sumOfX));
        return alpha;
    }

    public static double linearTrendLineSlopeAsPercentOfAverage(List<Double> values){
        final double slope = linearTrendLineSlope(values);
        final double average = values.stream().mapToDouble(v -> v).average().getAsDouble();
        return slope * 100.0 / average;
    }

    /**
     * https://brilliant.org/wiki/sum-of-n-n2-or-n3/
     * @param n
     * @return
     */
    public static long sumOfNaturalNumbersToN(long n){
        return (n * n + n) / 2;
    }

    /**
     * https://brilliant.org/wiki/sum-of-n-n2-or-n3/
     * @param n
     * @return
     */
    public static long sumOfNaturalNumbersToNSquared(long n){
        return (n * (n + 1) * (2 * n + 1)) / 6;
    }
}
