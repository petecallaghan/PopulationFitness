package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

public abstract class InterpolatingBitSetGenes extends BitSetGenes {

    protected double interpolation_ratio;

    protected InterpolatingBitSetGenes(Config config, double maxInterpolatedValue) {
        super(config);
        interpolation_ratio = maxInterpolatedValue / maxInteger();
    }

    @Override
    public double[] asDoubles() {
        final long[] integer_values = asIntegers();
        final double[] double_values = new double[integer_values.length];
        for (int i = 0; i < integer_values.length; i++) {
            double_values[i] = interpolate(integer_values[i]);
        }
        return double_values;
    }

    private double interpolate(long integer_value){
        return interpolation_ratio * integer_value;
    }
}


