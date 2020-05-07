package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.RepeatableRandom;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.GenesIdentifier;
import uk.edu.populationfitness.models.genes.cache.*;

import java.util.Arrays;
import java.util.BitSet;

import static java.lang.Math.abs;

/**
 * Extend this to provide different fitness functions that use a bitset
 *
 * Created by pete.callaghan on 03/07/2017.
 */
public abstract class BitSetGenes implements Genes {
    private static class BitCountCalculator implements ValueCalculator<Long> {
        @Override
        public Long calculateValue(long bitCount) {
            if (bitCount == Long.SIZE){
                return Long.MAX_VALUE;
            }
            return FastMaths.pow(2, bitCount)-1;
        }
    }

    private static final ExpensiveCalculatedValues<Long> BitCounts = new ExpensiveCalculatedValues(new BitSetGenes.BitCountCalculator());

    protected final Config config;

    private GenesIdentifier genesIdentifier;

    private static final double HALF_PROBABILITY = 0.5;

    private double stored_fitness = 0;

    private boolean fitness_stored = false;

    private final int size_of_genes;

    private final int number_of_integers;

    private final int bits_per_integer;

    private final long max_integer;

    public BitSetGenes(Config config) {
        this.config = config;
        size_of_genes = config.getGeneBitCount();
        number_of_integers = numberOfIntegers();
        bits_per_integer = bitsPerInteger();
        max_integer = BitCounts.findOrCalculate(bits_per_integer);
    }

    private int bitsPerInteger(){
        // The smaller of the max bits per long, or the size of each gene
        return Math.min(Long.SIZE, config.getSizeOfEachGene());
    }

    /**
     * The number of integers needed to hold the genes
     * @return
     */
    protected int numberOfIntegers(){
        // If the number of genes is bigger than the number of longs needed to fit the total bit count, go for the larger number
        // otherwise use the number of longs needed to hold the entire bit count
        return Math.max((int)((size_of_genes / Long.SIZE) + (size_of_genes % Long.SIZE == 0 ? 0 : 1)), config.getNumberOfGenes());
    }

    /**
     * The max value of any integer
     * @return
     */
    protected long maxInteger(){
        return max_integer;
    }

    private long[] getGenesFromCache() {
        return SharedCache.cache().get(genesIdentifier);
    }

    protected BitSet getBitSetGenesFromCache() {
        return BitSet.valueOf(SharedCache.cache().get(genesIdentifier));
    }

    private void storeGenesInCache(long[] genes) {
        genesIdentifier = SharedCache.cache().add(genes);
        fitness_stored = false;
    }

    @Override
    public void buildEmpty() {
        final long[] genes = new long[number_of_integers];
        Arrays.fill(genes, 0);
        storeGenesInCache(genes);
    }

    public void buildFull() {
        final long[] genes = new long[number_of_integers];
        Arrays.fill(genes, -1L);
        storeGenesInCache(genes);
    }

    @Override
    public void buildFromRandom() {
        final long[] genes = new long[number_of_integers];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = RepeatableRandom.generateNextLong(0, max_integer);
        }
        storeGenesInCache(genes);
    }

    /**
     *
     * @return the bit array as a compact array of integers
     */
    protected long[] asIntegers() {
        return SharedCache.cache().get(genesIdentifier);
    }

    @Override
    public int getCode(int index) {
        return getBitSetGenesFromCache().get(index) ? 1 : 0;
    }

    @Override
    public int numberOfBits() {
        return number_of_integers * Long.SIZE;
    }

    @Override
    public int mutate(){
        return mutateAndStore(asIntegers());
    }

    private int mutateAndStore(long[] genes){
        final int mutatedCount = mutateGenes(genes);
        storeGenesInCache(genes);
        return mutatedCount;
    }

    private int mutateGenes(long[] genes) {
        if (config.getMutationsPerIndividual() < 1){
            return 0;
        }

        final long mutations_per_gene = Math.max((long)(config.getMutationsPerIndividual() / genes.length), 1);

        final long mutation_genes_interval = (long)(genes.length / config.getMutationsPerIndividual());

        int mutatedCount = 0;

        // A simple loop for when we are mutating every gene
        if (mutation_genes_interval < 2){
            for (int i = 0; i < genes.length && mutatedCount <= config.getMutationsPerIndividual(); i++) {
                genes[i] = getMutatedValue(genes[i], mutations_per_gene);
                mutatedCount += mutations_per_gene;
            }
            return mutatedCount;
        }

        // Otherwise we are mutating just some genes
        for (int i = RepeatableRandom.generateNextInt(mutation_genes_interval);
             i < genes.length && mutatedCount <= config.getMutationsPerIndividual();
             i += Math.max(1, (int)RepeatableRandom.generateNextLong(1, mutation_genes_interval+1))) {
            genes[i] = getMutatedValue(genes[i], mutations_per_gene);
            mutatedCount += mutations_per_gene;
        }

        return mutatedCount;
    }

    private long getMutatedValue(long gene, long mutations_per_gene) {
        int mutatedCount = 0;
        long interval = (bits_per_integer - 1) / mutations_per_gene;
        if (interval < 1){
            // Mutate every bit
            return RepeatableRandom.generateNextLong(0, max_integer);
        }
        for (int bit = RepeatableRandom.generateNextInt(interval);
             bit < bits_per_integer - 1 && mutatedCount < mutations_per_gene;
             bit += Math.max(1, (int)RepeatableRandom.generateNextLong(1, interval+1))) {
            gene ^= (1L << bit);
            mutatedCount ++;
        }
        return gene;
    }

    /**
     * Call this to store the fitness, inverted to 1- fitness
     *
     * @param fitness
     * @return the scaled stored fitness
     */
    double storeInvertedFitness(double fitness){
        return storeFitness(1 - fitness);
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
    double storedFitness() {
        return stored_fitness;
    }

    boolean isFitnessStored(){
        return fitness_stored;
    }

    @Override
    public int inheritFrom(Genes mother, Genes father) {
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

        return mutateAndStore(babyEncoding);
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
