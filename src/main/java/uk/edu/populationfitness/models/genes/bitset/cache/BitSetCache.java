package uk.edu.populationfitness.models.genes.bitset.cache;

import java.util.BitSet;

public interface BitSetCache {
    /**
     * Creates a new BitSet and returns the combination of the BitSet and its identifier.
     *
     * @param numberOfBits
     * @return the bitset and its identifier
     */
    public IdentifiedBitSet createNew(int numberOfBits);
    /**
     * Creates a new BitSet as a copy of the source and returns the combination of the BitSet and its identifier.
     *
     * @param source
     * @return the bitset and its identifier
     */
    public IdentifiedBitSet createNew(long[] source);

    /***
     * @param identifier a bitset identifier for a previously created bitset and identifier
     * @return the identified BitSet
     */
    public BitSet get(BitSetIdentifier identifier);

    /**
     * Removes the BitSet. Subsequent calls to get will return null.
     * @param identifier a bitset identifier for a previously created bitset and identifier
     */
    public void remove(BitSetIdentifier identifier);
}
