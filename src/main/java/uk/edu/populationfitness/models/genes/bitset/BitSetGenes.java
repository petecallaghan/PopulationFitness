package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.RepeatableRandom;
import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.GenesIdentifier;
import uk.edu.populationfitness.models.genes.cache.*;

import java.util.BitSet;

import static java.lang.Math.abs;

/**
 * Extend this to provide different fitness functions that use a bitset
 *
 * Created by pete.callaghan on 03/07/2017.
 */
public abstract class BitSetGenes implements Genes {
    protected final Config config;

    private GenesIdentifier genesIdentifier;

    private static final double HALF_PROBABILITY = 0.5;

    private double stored_fitness = 0;

    private boolean fitness_stored = false;

    private final int size_of_genes;

    private final int mutation_bit_interval;

    BitSetGenes(Config config) {
        this.config = config;
        size_of_genes = config.getGeneBitCount();
        mutation_bit_interval = config.getMutationBitInterval();
    }

    private long[] getGenesFromCache() {
        return SharedCache.cache().get(genesIdentifier);
    }

    private BitSet getBitSetGenesFromCache() {
        return BitSet.valueOf(SharedCache.cache().get(genesIdentifier));
    }

    private void storeGenesInCache(BitSet genes) {
        genesIdentifier = SharedCache.cache().add(genes.toLongArray());
    }

    @Override
    public void buildEmpty() {
        final BitSet genes = new BitSet(size_of_genes);
        genes.clear();
        storeGenesInCache(genes);
    }

    public void buildFull() {
        final BitSet genes = new BitSet(size_of_genes);
        genes.set(0, size_of_genes - 1);
        storeGenesInCache(genes);
    }

    @Override
    public void buildFromRandom() {
        final BitSet genes = new BitSet(size_of_genes);
        genes.clear();

        for (int i = 0; i < size_of_genes; i++) {
            if (RepeatableRandom.generateNext() < HALF_PROBABILITY) {
                genes.flip(i);
            }
        }
        storeGenesInCache(genes);
        fitness_stored = false;
    }

    @Override
    public long[] asIntegers() {
        return SharedCache.cache().get(genesIdentifier);
    }

    @Override
    public int getCode(int index) {
        return getBitSetGenesFromCache().get(index) ? 1 : 0;
    }

    @Override
    public int numberOfBits() {
        return size_of_genes;
    }

    @Override
    public void mutate(){
        mutateAndStore(getBitSetGenesFromCache());
    }

    private void mutateAndStore(BitSet genes) {
        flipRandomBits(genes);
        fitness_stored = false;
        storeGenesInCache(genes);
    }

    private void flipRandomBits(BitSet genes) {
        for (int i = RepeatableRandom.generateNextInt(mutation_bit_interval);
             i < size_of_genes;
             i += RepeatableRandom.generateNextInt(mutation_bit_interval) + 1) {
            genes.flip(i);
        }
    }

    /**
     * Call this to scale and then store the fitness
     *
     * @param fitness
     * @return the scaled stored fitness
     */
    protected double scaleAndStoreFitness(double fitness) {
        return storeFitness(config.getRange().toScale(fitness));
    }

    /**
     * Call this to store a fitness
     *
     * @param fitness
     * @return
     */
    double storeFitness(double fitness) {
        stored_fitness = fitness;
        fitness_stored = true;
        return stored_fitness;
    }

    /**
     * @return the stored fitness
     */
    protected double storedFitness() {
        return stored_fitness;
    }

    protected boolean isFitnessStored(){
        return fitness_stored;
    }

    @Override
    public void inheritFrom(Genes mother, Genes father) {
        final long[] motherEncoding = ((BitSetGenes) mother.getImplementation()).getGenesFromCache();
        final long[] fatherEncoding = ((BitSetGenes) father.getImplementation()).getGenesFromCache();
        final long[] babyEncoding = new long[Math.max(motherEncoding.length, fatherEncoding.length)];

        // Randomly picks the code index that crosses over from mother to father
        final int cross_over_word = Math.min(1 + RepeatableRandom.generateNextInt(babyEncoding.length - 1), babyEncoding.length - 1);
        final int mother_length = Math.min(cross_over_word + 1, motherEncoding.length);
        final int father_length = Math.min(babyEncoding.length - cross_over_word - 1, fatherEncoding.length - cross_over_word - 1);

        System.arraycopy(motherEncoding, 0, babyEncoding, 0, mother_length);
        if (father_length > 0){
            System.arraycopy(fatherEncoding, cross_over_word + 1, babyEncoding, cross_over_word + 1, father_length);
        }

        mutateAndStore(BitSet.valueOf(babyEncoding));
    }

    @Override
    public boolean areEmpty() {
        return getBitSetGenesFromCache().isEmpty();
    }

    @Override
    public boolean isEqual(Genes other) {
        return getBitSetGenesFromCache().equals(((BitSetGenes) other).getBitSetGenesFromCache());
    }

    @Override
    public Genes getImplementation() {
        return this;
    }

    @Override
    public GenesIdentifier identifier(){
        return genesIdentifier;
    }
}
