package uk.edu.populationfitness.models;

import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.genes.GenesFactory;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenesFactory;

import java.time.Instant;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Config {
    private static final double ZERO = 0.0;
    private static final double PI = Math.PI;

    public static final double MutationScale = 1.0 / 30.0;

    // Unique identifier
    public String id = Instant.now().toString().replaceAll(":", "-");

    // No genes per individual
    private int number_of_genes;

    // Number of codes per gene
    private int size_of_each_gene = 10;

    private int gene_bit_count;

    private long max_gene_value;

    private long last_max_gene_value;

    // The likely number of mutations per gene
    private double mutations_per_individual;

    private final int max_age;

    private final int max_breeding_age;

    private int min_breeding_age;

    //  Probability that a pair will produce offspring in a year
    private final double probability_of_breeding;

    // Number of years in the simulation
    private final int number_of_years;

    // Defines genes for each individual
    private GenesFactory genesFactory = new BitSetGenesFactory();

    private int initial_population = 4000;

    private double max_fitness = 1.0;

    public Config(){
        setNumberOfGenes(4);
        setSizeOfEachGene(64);
        setMutationsPerIndividual(1);
        max_age = 85;
        max_breeding_age = max_age; //41;
        setMinBreedingAge(16);
        probability_of_breeding = 2 * 0.035; // Derived from Max Crude Birth Rate W&S 1981 1730-2009
        number_of_years = 2150;
        scaleMutationsPerGeneFromBitCount(MutationScale);
    }

    public int getNumberOfGenes() {
        return number_of_genes;
    }

    public void setNumberOfGenes(int number_of_genes) {
        this.number_of_genes = number_of_genes;
        geneSizeUpdated();
    }

    public int getSizeOfEachGene() {
        return size_of_each_gene;
    }

    public void setSizeOfEachGene(int size_of_each_gene) {
        this.size_of_each_gene = size_of_each_gene;
        geneSizeUpdated();
    }

    public int getGeneBitCount(){
        return gene_bit_count;
    }

    public double getMutationsPerIndividual() {
        return mutations_per_individual;
    }

    public void setMutationsPerIndividual(double mutations_per_individual) {
        this.mutations_per_individual = mutations_per_individual;
        geneSizeUpdated();
    }

    public void scaleMutationsPerGeneFromBitCount(double scale){
        setMutationsPerIndividual(scale * gene_bit_count);
    }

    private void geneSizeUpdated(){
        gene_bit_count = number_of_genes * size_of_each_gene;
        final long excess_bits = gene_bit_count % Long.SIZE;
        last_max_gene_value = excess_bits == 0 ? Long.MAX_VALUE : Math.min(Long.MAX_VALUE, FastMaths.pow(2, excess_bits)-1);
        max_gene_value = gene_bit_count < Long.SIZE ? last_max_gene_value : Long.MAX_VALUE;
    }

    public int getMaxAge() {
        return max_age;
    }

    public int getMaxBreedingAge() {
        return max_breeding_age;
    }

    public int getMinBreedingAge() {
        return min_breeding_age;
    }

    public void setMinBreedingAge(int min_breeding_age) {
        this.min_breeding_age = min_breeding_age;
    }

    public double getProbabilityOfBreeding() {
        return probability_of_breeding;
    }

    public int getNumberOfYears() {
        return number_of_years;
    }

    public GenesFactory getGenesFactory() {
        return genesFactory;
    }

    public void setGenesFactory(GenesFactory genesFactory) {
        this.genesFactory = genesFactory;
    }

    public int getInitialPopulation() {
        return initial_population;
    }

    public void setInitialPopulation(int initial_population) {
        this.initial_population = initial_population;
    }

    public long getLastMaxGeneValue(){
        return last_max_gene_value;
    }

    public long getMaxGeneValue(){
        return max_gene_value;
    }

    public void setMaxFitness(double max) {
        this.max_fitness = max;
    }

    public double getMaxFitness() {
        return this.max_fitness;
    }
}