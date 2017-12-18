package uk.edu.populationfitness.models.fastmaths;

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
}
