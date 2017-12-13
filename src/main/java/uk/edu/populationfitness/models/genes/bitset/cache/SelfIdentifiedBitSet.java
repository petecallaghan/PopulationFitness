package uk.edu.populationfitness.models.genes.bitset.cache;

import java.util.BitSet;

/**
 * A bit set identifier that directly references the bitset.
 */
public class SelfIdentifiedBitSet extends IdentifiedBitSet implements BitSetIdentifier {
    public SelfIdentifiedBitSet(BitSet bitSet) {
        super(bitSet);
        identifier = this;
    }

    @Override
    public long asUniqueLong() {
        throw new Error("Cannot convert to long");
    }
}
