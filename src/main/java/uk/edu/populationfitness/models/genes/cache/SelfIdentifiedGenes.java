package uk.edu.populationfitness.models.genes.cache;

import uk.edu.populationfitness.models.genes.GenesIdentifier;

/**
 * A bit set identifier that directly references the bitset.
 */
public class SelfIdentifiedGenes implements GenesIdentifier {
    public final long[] integerGenes;

    public SelfIdentifiedGenes(long[] integerGenes) {
        this.integerGenes = integerGenes;
    }

    @Override
    public long asUniqueLong() {
        throw new Error("Cannot convert to long");
    }
}
