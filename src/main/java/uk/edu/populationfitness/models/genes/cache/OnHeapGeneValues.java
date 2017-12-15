package uk.edu.populationfitness.models.genes.cache;

import uk.edu.populationfitness.models.genes.GenesIdentifier;

import java.util.Collection;

/**
 * Emulates a GeneValues by creating a self identified bitset, which retains a reference to the bitset, allowing
 * the bitset to be retrieved given the identifier. This means that all bitsets created by the cache will live
 * on the heap
 */
public class OnHeapGeneValues implements GeneValues {
    @Override
    public GenesIdentifier add(long[] genesIntegers) {
        return new SelfIdentifiedGenes(genesIntegers);
    }

    @Override
    public long[] get(GenesIdentifier identifier) {
        return ((SelfIdentifiedGenes)identifier).integerGenes;
    }

    @Override
    public void retainOnly(Collection<GenesIdentifier> genesIdentifiers) {

    }

    @Override
    public void close() {

    }
}
