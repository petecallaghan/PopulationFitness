package uk.edu.populationfitness.models.genes.reference;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.RepeatableRandom;

public class RandomGenes extends FixedGenes {
    public RandomGenes(Config config) {
        super(config, 0.6 + RepeatableRandom.generateNext() / 4.0);
    }
}
