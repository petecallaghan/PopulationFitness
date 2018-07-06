using PopulationFitness;
using PopulationFitness.Models;
using PopulationFitness.Output;
using System.Collections.Generic;
using Xunit;

namespace TestPopulationFitness.UnitTests
{
    public class GenerationsTest
    {
        [Fact]
        public void TestProduceGenerationHistory()
        {
            // Given a standard configuration ...
            Config config = new Config();
            config.MinBreedingAge = 1; // so we get some babies
            Population population = new Population(config);
            Generations generations = new Generations(population);
            // ... with some epochs
            Epochs epochs = new Epochs();
            epochs.AddNextEpoch(new Epoch(config, -50).Fitness(1.0).Capacity(4000));
            epochs.SetFinalEpochYear(-40);

            // When the simulation runs through the epochs
            generations.CreateForAllEpochs(epochs);

            // Then we get a history of the simulation
            Assert.Equal(11, generations.History.Count);
        }

        [Fact]
        public void AddGenerationStatitics()
        {
            // Given two sets of generation statistics
            Config config = new Config();
            Epoch epoch = new Epoch(config, -50);
            var first = new GenerationStatistics(epoch, epoch.StartYear, 100, 10, 20, 12, 13, 1.0, 2.0);
            var second = new GenerationStatistics(epoch, epoch.StartYear, 23, 1, 5, 120, 78, 1.0, 2.0);

            // When they are added
            GenerationStatistics result = GenerationStatistics.Add(first, second);

            // Then the results are correct
            AssertAreAdded(first, second, result);
        }

        private static void AssertAreAdded(GenerationStatistics first, GenerationStatistics second, GenerationStatistics result)
        {
            Assert.Equal(result.Year, first.Year);
            Assert.Equal(result.Year, second.Year);
            Assert.Equal(result.Epoch.StartYear, first.Epoch.StartYear);
            Assert.Equal(result.Epoch.EndYear, first.Epoch.EndYear);
            Assert.Equal(result.Epoch.ExpectedMaxPopulation, first.Epoch.ExpectedMaxPopulation + second.Epoch.ExpectedMaxPopulation);
            Assert.Equal(result.Epoch.EnvironmentCapacity, first.Epoch.EnvironmentCapacity + second.Epoch.EnvironmentCapacity);
            Assert.Equal(result.Population, first.Population + second.Population);
            Assert.Equal(result.NumberBorn, first.NumberBorn + second.NumberBorn);
            Assert.Equal(result.NumberKilled, first.NumberKilled + second.NumberKilled);
            Assert.Equal(result.CapacityFactor,
                    (first.CapacityFactor * first.Population +
                            second.CapacityFactor * second.Population) / result.Population, 6);
            Assert.Equal(result.AverageAge,
                    (first.AverageAge * first.Population +
                            second.AverageAge * second.Population) / result.Population, 6);
            Assert.Equal(result.AverageLifeExpectancy,
                    (first.AverageLifeExpectancy * first.NumberKilled +
                            second.AverageLifeExpectancy * second.NumberKilled) / result.NumberKilled, 6);
        }

        [Fact]
        public void AddCollectionsOfGenerationStatistics()
        {
            // Given two collections of statistics
            Config config = new Config();
            Epoch epoch = new Epoch(config, -50);
            GenerationStatistics first = new GenerationStatistics(epoch, epoch.StartYear, 100, 10, 20, 12, 13, 1.0, 2.0);
            GenerationStatistics second = new GenerationStatistics(epoch, epoch.StartYear, 23, 1, 5, 120, 78, 1.0, 2.0);
            var firstSet = new List<GenerationStatistics>
            {
                first
            };
            var secondSet = new List<GenerationStatistics>
            {
                second
            };

            first.AverageAge = 10.5;
            second.AverageAge = 20.3;
            first.AverageLifeExpectancy = 50.5;
            second.AverageLifeExpectancy = 60.76;

            // When they are added
            var result = GenerationStatistics.Add(firstSet, secondSet);

            // Then we have a collection of additions
            AssertAreAdded(first, second, result[0]);
        }

        [Fact]
        public void WriteAndReadGenerationStatistics()
        {
            // Given a standard configuration ...
            Config config = new Config();
            config.MinBreedingAge = 1; // so we get some babies
            Population population = new Population(config);
            Generations generations = new Generations(population);
            // ... with some epochs ...
            Epochs epochs = new Epochs();
            epochs.AddNextEpoch(new Epoch(config, -25).Fitness(1.0).Capacity(4000));
            epochs.AddNextEpoch(new Epoch(config, 0).Fitness(1.0).Capacity(4000));
            epochs.AddNextEpoch(new Epoch(config, 25).Fitness(1.0).Capacity(4000));
            epochs.SetFinalEpochYear(50);
            //  ... and some results written to a file
            generations.CreateForAllEpochs(epochs);
            var path = GenerationsWriter.WriteCsv(Paths.PathOf("test-results.csv"), generations, new PopulationFitness.Tuning());

            // When the results are read back
            var readResult = GenerationsReader.ReadGenerations(config, path);

            // Then they are the same as those written
            AssertAreEqual(generations.History, readResult);
        }

        private void AssertAreEqual(List<GenerationStatistics> expected, List<GenerationStatistics> actual)
        {
            Assert.Equal(expected.Count, actual.Count);
            for (int i = 0; i < expected.Count; i++)
            {
                AssertAreEqual(expected[i], actual[i]);
            }
        }

        private void AssertAreEqual(GenerationStatistics expected, GenerationStatistics actual)
        {
            Assert.Equal(expected.Epoch.StartYear, actual.Epoch.StartYear);
            Assert.Equal(expected.Epoch.EndYear, actual.Epoch.EndYear);
            Assert.Equal(expected.Epoch.EnvironmentCapacity, actual.Epoch.EnvironmentCapacity);
            Assert.Equal(expected.Epoch.IsFitnessEnabled, actual.Epoch.IsFitnessEnabled);
            Assert.Equal(expected.Epoch.BreedingProbability(), actual.Epoch.BreedingProbability(), 2);
            Assert.Equal(expected.Year, actual.Year);
            Assert.Equal(expected.Epoch.Fitness(), actual.Epoch.Fitness(), 7);
            Assert.Equal(expected.Epoch.ExpectedMaxPopulation, actual.Epoch.ExpectedMaxPopulation);
            Assert.Equal(expected.Population, actual.Population);
            Assert.Equal(expected.NumberBorn, actual.NumberBorn);
            Assert.Equal(expected.NumberKilled, actual.NumberKilled);
            Assert.Equal(expected.BornTime / 100, actual.BornTime / 100);
            Assert.Equal(expected.KillTime / 100, actual.KillTime / 100);
            Assert.Equal(expected.BornElapsedInHundredths(), actual.BornElapsedInHundredths(), 0);
            Assert.Equal(expected.KillElapsedInHundredths(), actual.KillElapsedInHundredths(), 0);
            Assert.Equal(expected.AverageFitness, actual.AverageFitness, 3);
            Assert.Equal(expected.AverageFactoredFitness, actual.AverageFactoredFitness, 3);
            Assert.Equal(expected.FitnessDeviation, actual.FitnessDeviation, 3);
            Assert.Equal(expected.AverageAge, actual.AverageAge, 3);
            Assert.Equal(expected.CapacityFactor, actual.CapacityFactor, 3);
            Assert.Equal(expected.AverageMutations, actual.AverageMutations, 3);
            Assert.Equal(expected.AverageLifeExpectancy, actual.AverageLifeExpectancy, 3);
        }
    }
}
