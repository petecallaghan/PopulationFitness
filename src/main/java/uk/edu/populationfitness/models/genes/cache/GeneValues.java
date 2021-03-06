package uk.edu.populationfitness.models.genes.cache;

import uk.edu.populationfitness.models.genes.GenesIdentifier;

import java.util.Collection;

public interface GeneValues {
    /**
     * Adds the genesIntegers and returns its identifier.
     *
     * @param genesIntegers
     * @return the genesIntegers identifier
     */
    GenesIdentifier add(long[] genesIntegers);

    /***
     * @param identifier an identifier for a previously added set of genes
     * @return the identified genes
     */
    long[] get(GenesIdentifier identifier);

    /**
     * Discard all in the cache other than those indicated
     *
     * @param genesIdentifiers
     */
    void retainOnly(Collection<GenesIdentifier> genesIdentifiers);

    /**
     * Called to release any values
     */
    void close();

    /**
     * @return true if the cache is flushable
     */
    boolean isFlushable();
}
