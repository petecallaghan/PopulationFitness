package uk.edu.populationfitness.models.genes.reference;

import uk.edu.populationfitness.models.Config;

public class FixedZeroGenes extends FixedGenes {
    public FixedZeroGenes(Config config) {
        super(config, 0.0);
    }
}
