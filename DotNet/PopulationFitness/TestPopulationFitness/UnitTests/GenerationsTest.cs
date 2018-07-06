using PopulationFitness;
using PopulationFitness.Models;
using PopulationFitness.Output;
using System.Collections.Generic;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class GenerationsTest
    {
        [TestCase]
        public void TestProduceGenerationHistory()
        {
            // Given a standard configuration ...
            Config config = new Config
            {
                MinBreedingAge = 1 // so we get some babies
            };
            Population population = new Population(config);
            Generations generations = new Generations(population);
            // ... with some epochs
            Epochs epochs = new Epochs();
            epochs.AddNextEpoch(new Epoch(config, -50).Fitness(1.0).Capacity(4000));
            epochs.SetFinalEpochYear(-40);

            // When the simulation runs through the epochs
            generations.CreateForAllEpochs(epochs);

            // Then we get a history of the simulation
            Assert.AreEqual(11, generations.History.Count);
        }

        [TestCase]
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
            double delta = 1.0e-6;

            Assert.AreEqual(result.Year, first.Year);
            Assert.AreEqual(result.Year, second.Year);
            Assert.AreEqual(result.Epoch.StartYear, first.Epoch.StartYear);
            Assert.AreEqual(result.Epoch.EndYear, first.Epoch.EndYear);
            Assert.AreEqual(result.Epoch.ExpectedMaxPopulation, first.Epoch.ExpectedMaxPopulation + second.Epoch.ExpectedMaxPopulation);
            Assert.AreEqual(result.Epoch.EnvironmentCapacity, first.Epoch.EnvironmentCapacity + second.Epoch.EnvironmentCapacity);
            Assert.AreEqual(result.Population, first.Population + second.Population);
            Assert.AreEqual(result.NumberBorn, first.NumberBorn + second.NumberBorn);
            Assert.AreEqual(result.NumberKilled, first.NumberKilled + second.NumberKilled);
            Assert.AreEqual(result.CapacityFactor,
                    (first.CapacityFactor * first.Population +
                            second.CapacityFactor * second.Population) / result.Population, delta);
            Assert.AreEqual(result.AverageAge,
                    (first.AverageAge * first.Population +
                            second.AverageAge * second.Population) / result.Population, delta);
            Assert.AreEqual(result.AverageLifeExpectancy,
                    (first.AverageLifeExpectancy * first.NumberKilled +
                            second.AverageLifeExpectancy * second.NumberKilled) / result.NumberKilled, delta);
        }

        [TestCase]
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

        [TestCase]
        public void WriteAndReadGenerationStatistics()
        {
            // Given a standard configuration ...
            Config config = new Config
            {
                MinBreedingAge = 1 // so we get some babies
            };
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
            Assert.AreEqual(expected.Count, actual.Count);
            for (int i = 0; i < expected.Count; i++)
            {
                AssertAreEqual(expected[i], actual[i]);
            }
        }

        private void AssertAreEqual(GenerationStatistics expected, GenerationStatistics actual)
        {
            Assert.AreEqual(expected.Epoch.StartYear, actual.Epoch.StartYear);
            Assert.AreEqual(expected.Epoch.EndYear, actual.Epoch.EndYear);
            Assert.AreEqual(expected.Epoch.EnvironmentCapacity, actual.Epoch.EnvironmentCapacity);
            Assert.AreEqual(expected.Epoch.IsFitnessEnabled, actual.Epoch.IsFitnessEnabled);
            Assert.AreEqual(expected.Epoch.BreedingProbability(), actual.Epoch.BreedingProbability(), 1.0e-2);
            Assert.AreEqual(expected.Year, actual.Year);
            Assert.AreEqual(expected.Epoch.Fitness(), actual.Epoch.Fitness(), 1.0e-7);
            Assert.AreEqual(expected.Epoch.ExpectedMaxPopulation, actual.Epoch.ExpectedMaxPopulation);
            Assert.AreEqual(expected.Population, actual.Population);
            Assert.AreEqual(expected.NumberBorn, actual.NumberBorn);
            Assert.AreEqual(expected.NumberKilled, actual.NumberKilled);
            Assert.AreEqual(expected.BornTime / 100, actual.BornTime / 100);
            Assert.AreEqual(expected.KillTime / 100, actual.KillTime / 100);
            Assert.AreEqual(expected.BornElapsedInHundredths(), actual.BornElapsedInHundredths(), 1.0);
            Assert.AreEqual(expected.KillElapsedInHundredths(), actual.KillElapsedInHundredths(), 1.0);
            Assert.AreEqual(expected.AverageFitness, actual.AverageFitness, 1.0e-3);
            Assert.AreEqual(expected.AverageFactoredFitness, actual.AverageFactoredFitness, 1.0e-3);
            Assert.AreEqual(expected.FitnessDeviation, actual.FitnessDeviation, 1.0e-3);
            Assert.AreEqual(expected.AverageAge, actual.AverageAge, 1.0e-3);
            Assert.AreEqual(expected.CapacityFactor, actual.CapacityFactor, 1.0e-3);
            Assert.AreEqual(expected.AverageMutations, actual.AverageMutations, 1.0e-3);
            Assert.AreEqual(expected.AverageLifeExpectancy, actual.AverageLifeExpectancy, 1.0e-3);
        }
    }
}
