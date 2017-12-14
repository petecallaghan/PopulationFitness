package uk.edu.populationfitness.models.genes.cache;

import uk.edu.populationfitness.models.genes.GenesIdentifier;

/**
 * Identifies a bitset with a uniformly increasing long value.
 */
public class LongIdentifier implements GenesIdentifier {
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
