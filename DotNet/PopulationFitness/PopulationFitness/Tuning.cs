using PopulationFitness.Models.Genes;
using System;

namespace PopulationFitness
{
    public class Tuning
    {
        public String Id;
        public Function Function = Function.Undefined;
        public double HistoricFit = 1.0;
        public double DiseaseFit = 50;
        public double ModernFit = 0.01;
        public double ModernBreeding = 0.07;
        public int NumberOfGenes = 10;
        public int SizeOfGenes = 4;
        public double MutationsPerGene = 0.2;
        public int SeriesRuns = 1;
        public int ParallelRuns = 1;
    }
}
