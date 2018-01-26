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

    private double stored_fitness_factor = 0;

    private final int size_of_genes;

    BitSetGenes(Config config) {
        this.config = config;
        size_of_genes = config.number_of_genes * config.size_of_each_gene;
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
        BitSet genes = new BitSet(size_of_genes);
        genes.clear();
        storeGenesInCache(genes);
    }

    public void buildFull() {
        BitSet genes = new BitSet(size_of_genes);
        genes.set(0, size_of_genes - 1);
        storeGenesInCache(genes);
    }

    @Override
    public void buildFromRandom() {
        BitSet genes = new BitSet(size_of_genes);
        genes.clear();

        for (int i = 0; i < size_of_genes; i++) {
            if (RepeatableRandom.generateNext() < HALF_PROBABILITY) {
                genes.flip(i);
            }
        }
        storeGenesInCache(genes);
        stored_fitness_factor = 0;
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
        if (config.mutations_per_gene < 1) {
            return;
        }

        int interval = size_of_genes / config.mutations_per_gene;

        for (int i = RepeatableRandom.generateNextInt(interval); i < size_of_genes; i += RepeatableRandom.generateNextInt(interval) + 1) {
            genes.flip(i);
        }
        stored_fitness_factor = 0;
        storeGenesInCache(genes);
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
    double storeScaledFitness(double fitness_factor, double fitness) {
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

    @Override
    public void inheritFrom(Genes mother, Genes father) {
        long[] motherEncoding = ((BitSetGenes) mother.getImplementation()).getGenesFromCache();
        long[] fatherEncoding = ((BitSetGenes) father.getImplementation()).getGenesFromCache();

        // Randomly picks the code index that crosses over from mother to father
        int cross_over_index = 1 + RepeatableRandom.generateNextInt(size_of_genes - 1);
        long[] encoding = new long[Math.max(motherEncoding.length, fatherEncoding.length)];
        int cross_over_word = Math.min(cross_over_index / Long.SIZE, encoding.length - 1);
        int cross_over_word_start = cross_over_word * Long.SIZE;
        int cross_over_word_end = cross_over_word_start + Long.SIZE;
        int mother_length = Math.min(cross_over_word + 1, motherEncoding.length);
        int father_length = Math.min(encoding.length - cross_over_word - 1, fatherEncoding.length - cross_over_word - 1);

        System.arraycopy(motherEncoding, 0, encoding, 0, mother_length);
        if (father_length > 0){
            System.arraycopy(fatherEncoding, cross_over_word + 1, encoding, cross_over_word + 1, father_length);
        }

        BitSet genes = BitSet.valueOf(encoding);
        BitSet fatherGenes = BitSet.valueOf(fatherEncoding);

        // Now do the bitwise copy from the father from the cross over index for just that word
        for (int i = cross_over_index; i < cross_over_word_end; i++) {
            genes.set(i, fatherGenes.get(i));
        }

        mutateAndStore(genes);
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
