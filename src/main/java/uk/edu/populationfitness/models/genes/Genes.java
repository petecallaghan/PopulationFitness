package uk.edu.populationfitness.models.genes;

/**
 * Implement this to provide genes
 *
 * Created by pete.callaghan on 13/07/2017.
 */
public interface Genes {

    /**
     * Builds a set of genes with none set
     */
    void buildEmpty();

    /**
     * Builds a set of genes with all set
     */
    void buildFull();

    /**
     * Gets the value for a single code
     *
     * @param index
     * @return
     */
    int getCode(int index);

    /**
     * Builds a set of genes with random values
     */
    void buildFromRandom();

    /**
     * The number of codes in the gene
     *
     * @return
     */
    int numberOfBits();

    /**
     * Randomly mutate
     */
    void mutate();

    /**
     * Inherit from parents
     *
     * @param mother
     * @param father
     */
    void inheritFrom(Genes mother, Genes father);

    /**
     *
     * @return true if none of the codes are set
     */
    boolean areEmpty();

    /**
     * Calculate the fitness in a range of 0..1
     *
     * @param fitness_factor
     * @return
     */
    double fitness(double fitness_factor);

    /**
     *
     * @param other
     * @return true if the encodings are all the same
     */
    boolean isEqual(Genes other);

    /**
     * @return the class implementing the interface
     */
    Genes getImplementation();
}

