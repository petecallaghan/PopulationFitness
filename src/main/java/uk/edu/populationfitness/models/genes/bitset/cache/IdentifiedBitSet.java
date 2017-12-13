package uk.edu.populationfitness.models.genes.bitset.cache;

import java.util.BitSet;

/**
 * Combines a bitset and its identifier
 */
public class IdentifiedBitSet {
    public BitSetIdentifier identifier;

    public BitSet bitSet;

    public IdentifiedBitSet(BitSetIdentifier identifier, BitSet bitSet) {
        this.identifier = identifier;
        this.bitSet = bitSet;
    }

    public IdentifiedBitSet(BitSet bitSet) {
        this.identifier = null;
        this.bitSet = bitSet;
    }
}
