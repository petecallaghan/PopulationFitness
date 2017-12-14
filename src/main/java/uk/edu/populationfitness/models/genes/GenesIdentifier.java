package uk.edu.populationfitness.models.genes;

/**
 * Identifies a set of genes
 */
public interface GenesIdentifier {
    /**
     *
     * @return a long value that uniquely identifies the genes.
     */
    long asUniqueLong();
}
