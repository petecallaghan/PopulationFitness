package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class BitSetGenesFactory implements GenesFactory {
    @Override
    public Genes build(Config config) {
        return new SinPiOver2BitSetGenes(config);
    }
}
