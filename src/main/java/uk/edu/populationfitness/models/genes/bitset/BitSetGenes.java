package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.RepeatableRandom;
import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.bitset.cache.BitSetCache;
import uk.edu.populationfitness.models.genes.bitset.cache.BitSetIdentifier;
import uk.edu.populationfitness.models.genes.bitset.cache.IdentifiedBitSet;
import uk.edu.populationfitness.models.genes.bitset.cache.OnHeapBitSetCache;

import java.util.BitSet;

import static java.lang.Math.abs;

/**
 * Extend this to provide different fitness functions that use a bit set
 *
 * Created by pete.callaghan on 03/07/2017.
 */
public abstract class BitSetGenes implements Genes {
    private static BitSetCache bitsetCache = new OnHeapBitSetCache();

    protected Config config;

    private BitSetIdentifier genesIdentifier;

    private BitSet genes;

    private static final double HALF_PROBABILITY = 0.5;

    private double stored_fitness = 0;

    private double stored_fitness_factor = 0;

    private final int size_of_genes;

    public BitSetGenes(Config config) {
        this.config = config;
        size_of_genes = config.number_of_genes * config.size_of_each_gene;
    }

    private BitSet getBitSetGenes() {
        return bitsetCache.get(genesIdentifier);
    }

    private BitSet createNewBitSetForTheseGenes(){
        IdentifiedBitSet newBitSet = bitsetCache.createNew(size_of_genes);
        genesIdentifier = newBitSet.identifier;
        return newBitSet.bitSet;
     }

    private BitSet createNewBitSetForTheseGenes(long[] source){
        IdentifiedBitSet newBitSet = bitsetCache.createNew(source);
        genesIdentifier = newBitSet.identifier;
        return newBitSet.bitSet;    }

    public void buildEmpty() {
        createNewBitSetForTheseGenes().clear();
    }

    public void buildFull() {
        createNewBitSetForTheseGenes().set(0, size_of_genes - 1);
    }

    public long[] asIntegers() {
        return getBitSetGenes().toLongArray();
    }

    public int getCode(int index) {
        return getBitSetGenes().get(index) ? 1 : 0;
    }

    public void buildFromRandom() {
        buildEmpty();

        BitSet genes = getBitSetGenes();
        for (int i = 0; i < size_of_genes; i++) {
            if (RepeatableRandom.generateNext() < HALF_PROBABILITY) {
                genes.flip(i);
            }
        }
        stored_fitness_factor = 0;
    }

    public int numberOfBits() {
        return size_of_genes;
    }

    public void mutate() {
        if (config.mutations_per_gene < 1) {
            return;
        }

        int interval = size_of_genes / config.mutations_per_gene;
        BitSet genes = getBitSetGenes();

        for (int i = RepeatableRandom.generateNextInt(interval); i < size_of_genes; i += RepeatableRandom.generateNextInt(interval) + 1) {
            genes.flip(i);
        }
        stored_fitness_factor = 0;
    }

    /**
     * Call this to scale and then store the fitness
     *
     * @param fitness_factor
     * @param fitness
     * @return the scaled stored fitness
     */
    protected double scaleAndStoreFitness(double fitness_factor, double fitness) {
        return storeScaledFitness(fitness_factor, config.range.toScale(fitness));
    }

    /**
     * Call this to store a scaled fitness
     *
     * @param fitness_factor
     * @param fitness
     * @return
     */
    protected double storeScaledFitness(double fitness_factor, double fitness) {
        stored_fitness_factor = fitness_factor;
        stored_fitness = fitness;
        return stored_fitness;
    }

    /**
     * @return the stored fitness
     */
    protected double storedFitness() {
        return stored_fitness;
    }

    /**
     * @param fitness_factor
     * @return true if the fitness factor is the same as the stored factor
     */
    protected boolean isSameFitnessFactor(double fitness_factor) {
        return (abs(fitness_factor - stored_fitness_factor) < 0.000001);
    }

    public void inheritFrom(Genes mother, Genes father) {
        BitSet motherGenes = ((BitSetGenes) mother.getImplementation()).getBitSetGenes();
        BitSet fatherGenes = ((BitSetGenes) father.getImplementation()).getBitSetGenes();
        long[] motherEncoding = motherGenes.toLongArray();
        long[] fatherEncoding = fatherGenes.toLongArray();

        // Randomly picks the code index that crosses over from mother to father
        int cross_over_index = 1 + RepeatableRandom.generateNextInt(size_of_genes - 1);
        int cross_over_word = cross_over_index / Long.SIZE;
        int cross_over_word_start = cross_over_word * Long.SIZE;
        int cross_over_word_end = cross_over_word_start + Long.SIZE;
        int last_word = Math.min(Math.min(size_of_genes / Long.SIZE, motherEncoding.length), fatherEncoding.length);

        if (motherEncoding.length > fatherEncoding.length) {
            // copy father to the end of the mother
            for (int i = cross_over_word + 1; i < last_word; i++) {
                motherEncoding[i] = fatherEncoding[i];
            }

            // Create the genes from the mother, with the father copied on the end
            BitSet genes = createNewBitSetForTheseGenes(motherEncoding);

            // Now do the bitwise copy from the father from the cross over index for just that word
            for (int i = cross_over_index; i < cross_over_word_end; i++) {
                genes.set(i, fatherGenes.get(i));
            }
        } else {
            // copy mother to the start of the father
            for (int i = 0; i < cross_over_word; i++) {
                fatherEncoding[i] = motherEncoding[i];
            }

            // Create the genes from the father, with the mother copied from the start
            BitSet genes = createNewBitSetForTheseGenes(fatherEncoding);

            // Now do the bitwise copy from the mother to the cross over index for just that word
            for (int i = cross_over_word_start; i < cross_over_index; i++) {
                genes.set(i, motherGenes.get(i));
            }
        }
    }

    public boolean areEmpty() {
        return getBitSetGenes().isEmpty();
    }

    public boolean isEqual(Genes other) {
        return getBitSetGenes().equals(((BitSetGenes) other).getBitSetGenes());
    }

    @Override
    public Genes getImplementation() {
        return this;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            bitsetCache.remove(genesIdentifier);
        } catch (Throwable t) {
            throw t;
        } finally {
            super.finalize();
        }
    }
}
