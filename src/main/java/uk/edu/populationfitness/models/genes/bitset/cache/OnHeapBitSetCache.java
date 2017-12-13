package uk.edu.populationfitness.models.genes.bitset.cache;

import java.util.BitSet;

/**
 * Emulates a BitSetCache by creating a self identified bitset, which retains a reference to the bitset, allowing
 * the bitset to be retrieved given the identifier. This means that all bitsets created by the cache will live
 * on the heap
 */
public class OnHeapBitSetCache implements BitSetCache {
    @Override
    public IdentifiedBitSet createNew(int numberOfBits) {
        return new SelfIdentifiedBitSet(new BitSet(numberOfBits));
    }

    @Override
    public IdentifiedBitSet createNew(long[] source) {
        return new SelfIdentifiedBitSet(BitSet.valueOf(source));
    }

    @Override
    public BitSet get(BitSetIdentifier identifier) {
        return ((SelfIdentifiedBitSet)identifier).bitSet;
    }

    @Override
    public void remove(BitSetIdentifier identifier) {
    }
}
