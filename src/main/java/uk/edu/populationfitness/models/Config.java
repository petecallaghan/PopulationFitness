package uk.edu.populationfitness.models;

import uk.edu.populationfitness.models.genes.GenesFactory;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenesFactory;
import uk.edu.populationfitness.models.genes.fitness.FitnessRange;

import java.time.Instant;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Config {
    private static final double ZERO = 0.0;
    private static final double PI = Math.PI;

    public static final double MutationScale = 100.0 / 20000000.0;

    // Unique identifier
    public String id = Instant.now().toString().replaceAll(":", "-");

    // No genes per individual
    private int number_of_genes;

    // Number of codes per gene
    private int size_of_each_gene;

    private int gene_bit_count;

    // The likely number of mutations per gene
    private double mutations_per_gene;

    // A function of mutations per gene and size of gene
    private int mutation_bit_interval;

    private final int max_age;

    private final int max_breeding_age;

    private int min_breeding_age;

    // The lower bound of the float conversion for genes
    private double float_lower;

    // The upper bound of the float conversion for genes
    private double float_upper;

    //  Probability that a pair will produce offspring in a year
    private final double probability_of_breeding;

    // Number of years in the simulation
    private final int number_of_years;

    // Defines genes for each individual
    private GenesFactory genesFactory = new BitSetGenesFactory();

    private final FitnessRange range = new FitnessRange();

    private int initial_population = 4000;

    public Config(){
        setNumberOfGenes(4);
        setSizeOfEachGene(10);
        setMutationsPerGene(1);
        max_age = 50;
        max_breeding_age = 35;
        setMinBreedingAge(16);
        setFloatLower(ZERO);
        setFloatUpper(PI);
        probability_of_breeding = 0.6;
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

    public double getMutationsPerGene() {
        return mutations_per_gene;
    }

    public void setMutationsPerGene(double mutations_per_gene) {
        this.mutations_per_gene = mutations_per_gene;
        geneSizeUpdated();
    }

    public void scaleMutationsPerGeneFromBitCount(double scale){
        setMutationsPerGene(scale * gene_bit_count);
    }

    private void geneSizeUpdated(){
        gene_bit_count = number_of_genes * size_of_each_gene;
        mutation_bit_interval = (int)(gene_bit_count / mutations_per_gene);
    }

    public int getMutationBitInterval(){
        return mutation_bit_interval;
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

    public double getFloatLower() {
        return float_lower;
    }

    public void setFloatLower(double float_lower) {
        this.float_lower = float_lower;
    }

    public double getFloatUpper() {
        return float_upper;
    }

    public void setFloatUpper(double float_upper) {
        this.float_upper = float_upper;
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

    public FitnessRange getRange() {
        return range;
    }

    public int getInitialPopulation() {
        return initial_population;
    }

    public void setInitialPopulation(int initial_population) {
        this.initial_population = initial_population;
    }
}