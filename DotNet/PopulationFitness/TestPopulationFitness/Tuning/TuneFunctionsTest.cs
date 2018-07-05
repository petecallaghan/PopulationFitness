using PopulationFitness;
using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using PopulationFitness.Models.Genes.Performance;
using PopulationFitness.Output;
using System.Diagnostics;
using TestPopulationFitness.Output;
using TestPopulationFitness.UnitTests;
using Xunit;

namespace TestPopulationFitness.Tuning
{
    public class TuneFunctionsTest
    {
        private const int NumberOfGenes = 20000;
        private const int SizeOfGenes = 1000;
        private const int PopulationRatio = 100;
        private static readonly string EpochsPath = Paths.PathOf("epochs");
        private static readonly string TuningPath = Paths.PathOf("tuning");
        private const int TuningPercentage = 15;
        private const double MutationsPerIndividual = 150;

        [Theory]
        [InlineData(Function.Rastrigin, 10.0, TuningPercentage)]
        [InlineData(Function.Sphere, 5, 25)]
        [InlineData(Function.StyblinksiTang, 10, 30)]
        [InlineData(Function.Schwefel226, 1.0, TuningPercentage)]
        [InlineData(Function.Rosenbrock, 10, TuningPercentage)]
        [InlineData(Function.SumOfPowers, 10, 30)]
        [InlineData(Function.SumSquares, 20, 25)]
        [InlineData(Function.Ackleys, 4, TuningPercentage)]
        [InlineData(Function.Alpine, 30, 40)]
        [InlineData(Function.Brown, 10, 25)]
        [InlineData(Function.ChungReynolds, 8, 40)]
        [InlineData(Function.DixonPrice, 8, 30)]
        [InlineData(Function.Exponential, 4, 20)]
        [InlineData(Function.Griewank, 400, TuningPercentage)]
        [InlineData(Function.Qing, 8, 25)]
        [InlineData(Function.Salomon, 4, TuningPercentage)]
        [InlineData(Function.SchumerSteiglitz, 8, 25)]
        [InlineData(Function.Schwefel220, 4, 25)]
        [InlineData(Function.Trid, 10.0, 25)]
        [InlineData(Function.Zakharoy, 100.0, 25)]
        public void Tune(Function function, double maxFactor, int tuningPercentage)
        {
            RepeatableRandom.ResetSeed();
            ConsoleRedirector.Redirect();

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
            Debug.Write("Tuned Disease fitness:");
            Debug.WriteLine(tuning.DiseaseFit);
            Debug.Write("Tuned Historic fitness:");
            Debug.WriteLine(tuning.HistoricFit);
            Debug.Write("Tuned Modern fitness:");
            Debug.WriteLine(tuning.ModernFit);
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
            Config config = new Config();
            config.GenesFactory.FitnessFunction = function;
            config.NumberOfGenes = NumberOfGenes;
            config.SizeOfEachGene = SizeOfGenes;
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
