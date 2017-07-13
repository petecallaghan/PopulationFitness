package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

/**
 * Implement this to create new genes
 *
 * Created by pete.callaghan on 13/07/2017.
 */
public interface GenesFactory {
    Genes build(Config config);
}
