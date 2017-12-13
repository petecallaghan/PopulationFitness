package uk.edu.populationfitness.models.genes.bitset.cache;

/**
 * Identifies a bitset with a uniformly increasing long value.
 */
public class LongIdentifier implements BitSetIdentifier {
    private static long globalIdentifier = 0;

    private final long value;

    public LongIdentifier(){
        value = ++globalIdentifier;
    }
    @Override
    public long asUniqueLong() {
        return value;
    }
}
