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

    /**
     * Optimised approximation to exp
     * @param power
     * @return an approximation of e raised to power
     */
    public static double exp(double power){
        // See https://martin.ankerl.com/2007/10/04/optimized-pow-approximation-for-java-and-c-c/
        final long tmp = (long) (1512775 * power + (1072693248 - 60801));
        return Double.longBitsToDouble(tmp << 32);
    }
}
