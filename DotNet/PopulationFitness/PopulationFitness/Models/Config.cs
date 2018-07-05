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
        public String Id = DateTime.Now.ToLongTimeString().Replace(":", "-");

        // Num genes per individual
        private int _numberOfGenes;

        // Number of codes per gene
        private int _sizeOfEachGene = 10;

        // The likely number of mutations per gene
        private double _mutationsPerGene;

        public Config()
        {
            NumberOfGenes = 4;
            SizeOfEachGene = 10;
            MutationsPerGene = 1;
            MaxAge = 90;
            MaxBreedingAge = 41;
            MinBreedingAge = 16;
            ProbabilityOfBreeding = 0.35; // Derived from Max Crude Birth Rate W&S 1981 1730-2009
            NumberOfYears = 2150;
            ScaleMutationsPerGeneFromBitCount(MutationScale);
        }

        public int NumberOfGenes
        {
            get
            {
                return _numberOfGenes;
            }

            set
            {
                _numberOfGenes = value;
                GeneSizeUpdated();
            }
        }

        public int SizeOfEachGene
        {
            get
            {
                return _sizeOfEachGene;
            }

            set
            {
                _sizeOfEachGene = value;
                GeneSizeUpdated();
            }
        }

        public int GeneBitCount { get; private set; }

        public double MutationsPerGene
        {
            get
            {
                return _mutationsPerGene;
            }

            set
            {
                _mutationsPerGene = value;
                GeneSizeUpdated();
            }
        }

        public void ScaleMutationsPerGeneFromBitCount(double scale)
        {
            MutationsPerGene = scale * GeneBitCount;
        }

        private void GeneSizeUpdated()
        {
            GeneBitCount = _numberOfGenes * _sizeOfEachGene;
            long excess_bits = GeneBitCount % Long.Size;
            LastMaxGeneValue = excess_bits == 0 ? long.MaxValue: Math.Min(long.MaxValue, (long)FastMaths.FastMaths.Pow(2, excess_bits) - 1);
            MaxGeneValue = GeneBitCount < Long.Size ? LastMaxGeneValue : long.MaxValue;
        }

        public int MaxAge { get; }

        public int MaxBreedingAge { get; }

        public int MinBreedingAge { get; set; }

        public double ProbabilityOfBreeding { get; }

        public int NumberOfYears { get; }

        public IGenesFactory GenesFactory { get; set; } = new BitSetGenesFactory();

        public int InitialPopulation { get; set; } = 4000;

        public long LastMaxGeneValue { get; private set; }

        public long MaxGeneValue { get; private set; }
    }
}
