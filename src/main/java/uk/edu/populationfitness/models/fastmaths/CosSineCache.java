package uk.edu.populationfitness.models.fastmaths;

/**
 * Trades accuracy for speed by resolving to the nearest degree and caching the corresponding sin and cosine values
 */
public class CosSineCache {
    private static final int Degrees = 361;
    private static final int Entries = Degrees;
    private double[] cos = new double[Entries];
    private double[] sin = new double[Entries];

    private static final CosSineCache Values = new CosSineCache();

    private CosSineCache() {
        for (int i = 0; i < Entries ; i++) {
            double radians = Math.toRadians(i);
            cos[i] = Math.cos(radians);
            sin[i] = Math.sin(radians);
        }
    }

    public static double sin(double angle) {
        int angleCircle = (int)angle % 360;
        if (angle < 0){
            return 0-Values.sin[0-angleCircle];
        }
        return Values.sin[angleCircle];
    }

    public static double cos(double angle) {
        int angleCircle = (int)angle % 360;
        if (angle < 0){
            return 0-Values.cos[0-angleCircle];
        }
        return Values.cos[angleCircle];
    }
}
