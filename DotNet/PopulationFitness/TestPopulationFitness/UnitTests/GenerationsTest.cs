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
            config.SetMinBreedingAge(1); // so we get some babies
            Population population = new Population(config);
            Generations generations = new Generations(population);
            // ... with some epochs
            Epochs epochs = new Epochs();
            epochs.AddNextEpoch(new Epoch(config, -50).Fitness(1.0).Capacity(4000));
            epochs.SetFinalEpochYear(-40);

            // When the simulation runs through the epochs
            generations.CreateForAllEpochs(epochs);

            // Then we get a history of the simulation
            Assert.Equal(11, generations.history.Count);
        }

        [Fact]
        public void AddGenerationStatitics()
        {
            // Given two sets of generation statistics
            Config config = new Config();
            Epoch epoch = new Epoch(config, -50);
            var first = new GenerationStatistics(epoch, epoch.start_year, 100, 10, 20, 12, 13, 1.0, 2.0);
            var second = new GenerationStatistics(epoch, epoch.start_year, 23, 1, 5, 120, 78, 1.0, 2.0);

            // When they are added
            GenerationStatistics result = GenerationStatistics.Add(first, second);

            // Then the results are correct
            AssertAreAdded(first, second, result);
        }

        private static void AssertAreAdded(GenerationStatistics first, GenerationStatistics second, GenerationStatistics result)
        {
            Assert.Equal(result.year, first.year);
            Assert.Equal(result.year, second.year);
            Assert.Equal(result.epoch.start_year, first.epoch.start_year);
            Assert.Equal(result.epoch.end_year, first.epoch.end_year);
            Assert.Equal(result.epoch.expected_max_population, first.epoch.expected_max_population + second.epoch.expected_max_population);
            Assert.Equal(result.epoch.environment_capacity, first.epoch.environment_capacity + second.epoch.environment_capacity);
            Assert.Equal(result.population, first.population + second.population);
            Assert.Equal(result.number_born, first.number_born + second.number_born);
            Assert.Equal(result.number_killed, first.number_killed + second.number_killed);
            Assert.Equal(result.capacity_factor,
                    (first.capacity_factor * first.population +
                            second.capacity_factor * second.population) / result.population, 6);
            Assert.Equal(result.average_age,
                    (first.average_age * first.population +
                            second.average_age * second.population) / result.population, 6);
            Assert.Equal(result.average_life_expectancy,
                    (first.average_life_expectancy * first.number_killed +
                            second.average_life_expectancy * second.number_killed) / result.number_killed, 6);
        }

        [Fact]
        public void AddCollectionsOfGenerationStatistics()
        {
            // Given two collections of statistics
            Config config = new Config();
            Epoch epoch = new Epoch(config, -50);
            GenerationStatistics first = new GenerationStatistics(epoch, epoch.start_year, 100, 10, 20, 12, 13, 1.0, 2.0);
            GenerationStatistics second = new GenerationStatistics(epoch, epoch.start_year, 23, 1, 5, 120, 78, 1.0, 2.0);
            var firstSet = new List<GenerationStatistics>
            {
                first
            };
            var secondSet = new List<GenerationStatistics>
            {
                second
            };

            first.average_age = 10.5;
            second.average_age = 20.3;
            first.average_life_expectancy = 50.5;
            second.average_life_expectancy = 60.76;

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
            config.SetMinBreedingAge(1); // so we get some babies
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
            AssertAreEqual(generations.history, readResult);
        }

        private void AssertAreEqual(List<GenerationStatistics> expected, List<GenerationStatistics> actual)
        {
            Assert.Equal(expected.Count, actual.Count);
            for (int i = 0; i < expected.Count; i++)
            {
                AssertAreEqual(expected[i], actual[i]);
            }
        }

        private void AssertAreEqual(GenerationStatistics e, GenerationStatistics a)
        {
            Assert.Equal(e.epoch.start_year, a.epoch.start_year);
            Assert.Equal(e.epoch.end_year, a.epoch.end_year);
            Assert.Equal(e.epoch.environment_capacity, a.epoch.environment_capacity);
            Assert.Equal(e.epoch.IsFitnessEnabled, a.epoch.IsFitnessEnabled);
            Assert.Equal(e.epoch.BreedingProbability(), a.epoch.BreedingProbability(), 2);
            Assert.Equal(e.year, a.year);
            Assert.Equal(e.epoch.Fitness(), a.epoch.Fitness(), 7);
            Assert.Equal(e.epoch.expected_max_population, a.epoch.expected_max_population);
            Assert.Equal(e.population, a.population);
            Assert.Equal(e.number_born, a.number_born);
            Assert.Equal(e.number_killed, a.number_killed);
            Assert.Equal(e.BornElapsedInHundredths(), a.BornElapsedInHundredths(), 3);
            Assert.Equal(e.KillElapsedInHundredths(), a.KillElapsedInHundredths(), 3);
            Assert.Equal(e.average_fitness, a.average_fitness, 3);
            Assert.Equal(e.average_factored_fitness, a.average_factored_fitness, 3);
            Assert.Equal(e.fitness_deviation, a.fitness_deviation, 3);
            Assert.Equal(e.average_age, a.average_age, 3);
            Assert.Equal(e.capacity_factor, a.capacity_factor, 3);
            Assert.Equal(e.average_mutations, a.average_mutations, 3);
            Assert.Equal(e.average_life_expectancy, a.average_life_expectancy, 3);
        }
    }
}
