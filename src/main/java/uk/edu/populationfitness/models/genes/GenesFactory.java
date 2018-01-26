package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

/**
 * Implement this to create new genes
 *
 * Created by pete.callaghan on 13/07/2017.
 */
public interface GenesFactory {
    /**
     * Builds a set of genes using the fitness function specified
     * @param config
     * @return the built genes
     */
    Genes build(Config config);

    /**
     * Defines the fitness function to use when building genes
     *
     * @param function
     */
    void useFitnessFunction(Function function);

    /***
     *
     * @return the function used when building genes
     */
    Function getFitnessFunction();
}
