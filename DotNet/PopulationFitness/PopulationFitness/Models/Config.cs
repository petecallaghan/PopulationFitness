using PopulationFitness.Models.Genes;
using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models
{
    /**
     * Created by pete.callaghan on 03/07/2017.
     */
    public class Config
    {
        public static readonly double MutationScale = 1.0 / 30.0;

        // Unique identifier
        public String id = DateTime.Now.ToLongTimeString().Replace(":", "-");

        // No genes per individual
        private int number_of_genes;

        // Number of codes per gene
        private int size_of_each_gene = 10;

        private int gene_bit_count;

        private long max_gene_value;

        private long last_max_gene_value;

        // The likely number of mutations per gene
        private double mutations_per_gene;

        private readonly int max_age;

        private readonly int max_breeding_age;

        private int min_breeding_age;

        //  Probability that a pair will produce offspring in a year
        private readonly double probability_of_breeding;

        // Number of years in the simulation
        private readonly int number_of_years;

        // Defines genes for each individual
        private IGenesFactory genesFactory = new BitSetGenesFactory();

        private int initial_population = 4000;

        public Config()
        {
            SetNumberOfGenes(4);
            SetSizeOfEachGene(10);
            SetMutationsPerGene(1);
            max_age = 90;
            max_breeding_age = 41;
            SetMinBreedingAge(16);
            probability_of_breeding = 0.35; // Derived from Max Crude Birth Rate W&S 1981 1730-2009
            number_of_years = 2150;
            ScaleMutationsPerGeneFromBitCount(MutationScale);
        }

        public int GetNumberOfGenes()
        {
            return number_of_genes;
        }

        public void SetNumberOfGenes(int number_of_genes)
        {
            this.number_of_genes = number_of_genes;
            GeneSizeUpdated();
        }

        public int GetSizeOfEachGene()
        {
            return size_of_each_gene;
        }

        public void SetSizeOfEachGene(int size_of_each_gene)
        {
            this.size_of_each_gene = size_of_each_gene;
            GeneSizeUpdated();
        }

        public int GetGeneBitCount()
        {
            return gene_bit_count;
        }

        public double GetMutationsPerGene()
        {
            return mutations_per_gene;
        }

        public void SetMutationsPerGene(double mutations_per_gene)
        {
            this.mutations_per_gene = mutations_per_gene;
            GeneSizeUpdated();
        }

        public void ScaleMutationsPerGeneFromBitCount(double scale)
        {
            SetMutationsPerGene(scale * gene_bit_count);
        }

        private void GeneSizeUpdated()
        {
            gene_bit_count = number_of_genes * size_of_each_gene;
            long excess_bits = gene_bit_count % Genes.BitSet.Long.Size;
            last_max_gene_value = excess_bits == 0 ? long.MaxValue: Math.Min(long.MaxValue, (long)FastMaths.FastMaths.Pow(2, excess_bits) - 1);
            max_gene_value = gene_bit_count < Genes.BitSet.Long.Size ? last_max_gene_value : long.MaxValue;
        }

        public int GetMaxAge()
        {
            return max_age;
        }

        public int GetMaxBreedingAge()
        {
            return max_breeding_age;
        }

        public int GetMinBreedingAge()
        {
            return min_breeding_age;
        }

        public void SetMinBreedingAge(int min_breeding_age)
        {
            this.min_breeding_age = min_breeding_age;
        }

        public double GetProbabilityOfBreeding()
        {
            return probability_of_breeding;
        }

        public int GetNumberOfYears()
        {
            return number_of_years;
        }

        public IGenesFactory GetGenesFactory()
        {
            return genesFactory;
        }

        public void SetGenesFactory(IGenesFactory genesFactory)
        {
            this.genesFactory = genesFactory;
        }

        public int GetInitialPopulation()
        {
            return initial_population;
        }

        public void SetInitialPopulation(int initial_population)
        {
            this.initial_population = initial_population;
        }

        public long GetLastMaxGeneValue()
        {
            return last_max_gene_value;
        }

        public long GetMaxGeneValue()
        {
            return max_gene_value;
        }
    }
}
