using PopulationFitness;
using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using PopulationFitness.Models.Genes.Performance;
using PopulationFitness.Output;
using TestPopulationFitness.UnitTests;
using NUnit.Framework;
using System;

namespace TestPopulationFitness.Tuning
{
    [TestFixture]
    public class TuneFunctionsTest
    {
        private const int NumberOfGenes = 20000;
        private const int SizeOfGenes = 1000;
        private const int PopulationRatio = 100;
        private static readonly string EpochsPath = Paths.PathOf("epochs");
        private static readonly string TuningPath = Paths.PathOf("tuning");
        private const int TuningPercentage = 15;
        private const double MutationsPerIndividual = 150;

        [TestCase(Function.Rastrigin, 10.0, TuningPercentage)]
        [TestCase(Function.Sphere, 5, 25)]
        [TestCase(Function.StyblinksiTang, 10, 30)]
        [TestCase(Function.Schwefel226, 1.0, TuningPercentage)]
        [TestCase(Function.Rosenbrock, 10, TuningPercentage)]
        [TestCase(Function.SumOfPowers, 10, 30)]
        [TestCase(Function.SumSquares, 20, 25)]
        [TestCase(Function.Ackleys, 4, TuningPercentage)]
        [TestCase(Function.Alpine, 30, 40)]
        [TestCase(Function.Brown, 10, 25)]
        [TestCase(Function.ChungReynolds, 8, 40)]
        [TestCase(Function.DixonPrice, 8, 30)]
        [TestCase(Function.Exponential, 4, 20)]
        [TestCase(Function.Griewank, 400, TuningPercentage)]
        [TestCase(Function.Qing, 8, 25)]
        [TestCase(Function.Salomon, 4, TuningPercentage)]
        [TestCase(Function.SchumerSteiglitz, 8, 25)]
        [TestCase(Function.Schwefel220, 4, 25)]
        [TestCase(Function.Trid, 10.0, 25)]
        [TestCase(Function.Zakharoy, 100.0, 25)]
        public void Tune(Function function, double maxFactor, int tuningPercentage)
        {
            RepeatableRandom.ResetSeed();

            var config = BuildTimedTuningConfig(function);
            var epochs = UkPopulationEpochs.Define(config);
            var generations = new Generations(new Population(config));

            epochs.ReducePopulation(PopulationRatio);
            config.InitialPopulation = epochs.First.EnvironmentCapacity;
            config.MutationsPerGene = MutationsPerIndividual;

            var result = generations.TuneFitnessFactorsForAllEpochs(epochs, 0.0, maxFactor, 0.000001, tuningPercentage);
            var tuning = CreateTuningFromEpochs(config, epochs);

            ShowResults(tuning);

            WriteResults(function, config, epochs, tuning);

            AssertTuned(result, tuning);
        }

        private void ShowResults(PopulationFitness.Tuning tuning)
        {
            ShowTuning(tuning);
            GenesTimer.ShowAll();
        }

        private void ShowTuning(PopulationFitness.Tuning tuning)
        {
            Console.Write("Tuned Disease fitness:");
            Console.WriteLine(tuning.DiseaseFit);
            Console.Write("Tuned Historic fitness:");
            Console.WriteLine(tuning.HistoricFit);
            Console.Write("Tuned Modern fitness:");
            Console.WriteLine(tuning.ModernFit);
        }

        private void AssertTuned(PopulationComparison result, PopulationFitness.Tuning tuning)
        {
            // Ensure that we successfully tuned
            Assert.True(result == PopulationComparison.WithinRange);

            // Ensure that the tuning result is what we expect
            Assert.True(tuning.DiseaseFit < tuning.ModernFit);
        }

        private void WriteResults(Function function, Config config, Epochs epochs, PopulationFitness.Tuning tuning)
        {
            EpochsWriter.WriteCsv(EpochsPath, function, config.NumberOfGenes, config.SizeOfEachGene, config.MutationsPerGene, epochs);
            TuningWriter.WriteInPath(TuningPath, tuning);
        }

        private void SetUpGeneTimers(Config config)
        {
            config.GenesFactory = new GenesTimerFactory(config.GenesFactory);
            GenesTimer.ResetAll();
        }

        private Config BuildTimedTuningConfig(Function function)
        {
            Config config = new Config
            {
                NumberOfGenes = NumberOfGenes,
                SizeOfEachGene = SizeOfGenes
            };
            config.GenesFactory.FitnessFunction = function;
            config.ScaleMutationsPerGeneFromBitCount(Config.MutationScale);
            SetUpGeneTimers(config);
            return config;
        }

        private PopulationFitness.Tuning CreateTuningFromEpochs(Config config, Epochs epochs)
        {
            var tuning = new PopulationFitness.Tuning
            {
                Function = config.GenesFactory.FitnessFunction,
                SizeOfGenes = config.SizeOfEachGene,
                NumberOfGenes = config.NumberOfGenes,
                ParallelRuns = 1,
                SeriesRuns = 1,
                MutationsPerGene = config.MutationsPerGene
            };

            Epoch diseaseEpoch = FindDiseaseEpoch(epochs);
            Epoch historicalEpoch = FindHistoricalEpoch(epochs);
            Epoch modernEpoch = FindModernEpoch(epochs);

            tuning.HistoricFit = historicalEpoch.AverageCapacityFactor() * historicalEpoch.Fitness();
            tuning.ModernFit = modernEpoch.AverageCapacityFactor() * modernEpoch.Fitness();
            tuning.ModernBreeding = modernEpoch.BreedingProbability();
            tuning.DiseaseFit = diseaseEpoch.AverageCapacityFactor() * diseaseEpoch.Fitness();

            return tuning;
        }

        private Epoch FindModernEpoch(Epochs epochs)
        {
            // Find the modern epoch with the max fitness factor
            Epoch modern = epochs.Last;
            double max = modern.Fitness() * modern.AverageCapacityFactor();

            for (int i = epochs.All.Count - 1; i > epochs.All.Count - 6; i--)
            {
                Epoch current = epochs.All[i];
                double currentFitness = current.Fitness() * current.AverageCapacityFactor();
                if (max < currentFitness)
                {
                    max = currentFitness;
                    modern = current;
                }
            }
            return modern;
        }

        private Epoch FindHistoricalEpoch(Epochs epochs)
        {
            foreach (Epoch epoch in epochs.All)
            {
                if (epoch.StartYear >= 1451)
                {
                    // Use this epoch as the historical epoch
                    return epoch;
                }
            }
            return null;
        }

        private Epoch FindDiseaseEpoch(Epochs epochs)
        {
            foreach (Epoch epoch in epochs.All)
            {
                if (epoch.Disease())
                {
                    return epoch;
                }
            }
            return null;
        }
    }
}
