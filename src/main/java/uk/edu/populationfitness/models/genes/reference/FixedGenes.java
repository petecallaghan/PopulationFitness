package uk.edu.populationfitness.models.genes.reference;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenes;

public class FixedGenes extends BitSetGenes {
    private final double fixedValue;
    protected FixedGenes(Config config, double value) {
        super(config);
        fixedValue = value;
    }

    @Override
    public double fitness() {
        return fixedValue;
    }

    public double[] asDoubles() {
        return new double[0];
    }
}
