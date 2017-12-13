package uk.edu.populationfitness.models.genes.bitset.cache;

/**
 * Identifies a Bitset
 */
public interface BitSetIdentifier {
    /**
     *
     * @return a long value that uniquely identifies the bitset.
     */
    long asUniqueLong();
}
