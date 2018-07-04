using PopulationFitness;
using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using PopulationFitness.Output;
using System.IO;
using Xunit;

namespace TestPopulationFitness.UnitTests
{
    public class EpochsTest
    {
        private static readonly int UNDEFINED_YEAR = -1;

        [Fact]
        public void testCreateEpochs()
        {
            // Given a set of epochs
            Config config = new Config();
            Epochs epochs = new Epochs();

            epochs.AddNextEpoch(new Epoch(config, -50));
            epochs.AddNextEpoch(new Epoch(config, 400));
            epochs.AddNextEpoch(new Epoch(config, 550));
            epochs.AddNextEpoch(new Epoch(config, 1086));
            epochs.AddNextEpoch(new Epoch(config, 1300));
            epochs.AddNextEpoch(new Epoch(config, 1348));
            epochs.AddNextEpoch(new Epoch(config, 1400));
            epochs.AddNextEpoch(new Epoch(config, 2016));
            epochs.AddNextEpoch(new Epoch(config, 2068));
            epochs.SetFinalEpochYear(-50 + config.GetNumberOfYears() - 1);

            // When we iterate over the epochs
            int first_year = UNDEFINED_YEAR;
            int last_year = UNDEFINED_YEAR;
            int number_of_years = 0;

            foreach (Epoch e in epochs.epochs)
            {
                for (int year = e.start_year; year <= e.end_year; year++)
                {
                    if (first_year == UNDEFINED_YEAR)
                    {
                        first_year = year;
                    }
                    last_year = year;
                    number_of_years++;
                }
            }

            // Then we traverse all the years
            Assert.Equal(-50, first_year);
            Assert.Equal(-50 + config.GetNumberOfYears() - 1, last_year);
            Assert.Equal(+config.GetNumberOfYears(), number_of_years);
        }

        [Fact]
        public void testEpochFitnessFactors()
        {
            // Given a set of epochs
            Config config = new Config();
            Epochs epochs = new Epochs();

            epochs.AddNextEpoch(new Epoch(config, -50));
            epochs.AddNextEpoch(new Epoch(config, 400).Fitness(2.0));

            // When we iterate over the epochs we find the right Fitness factors
            Assert.Equal(1.0, epochs.First.Fitness(), 1);
            Assert.Equal(2.0, epochs.Last.Fitness(), 1);
        }

        [Fact]
        public void testEpochEnvironmentCapacity()
        {
            // Given a set of epochs
            Config config = new Config();
            Epochs epochs = new Epochs();
            epochs.AddNextEpoch(new Epoch(config, -50).Capacity(1000));
            epochs.AddNextEpoch(new Epoch(config, 400));

            // When we iterate over the epochs we find the right environment capacity
            Assert.Equal(1000, epochs.First.environment_capacity);
            Assert.Equal(epochs.First.environment_capacity, epochs.First.expected_max_population);
            Assert.False(epochs.First.IsCapacityUnlimited);
            Assert.Equal(Epoch.UNLIMITED_CAPACITY, epochs.Last.environment_capacity);
            Assert.True(epochs.Last.IsCapacityUnlimited);
            Assert.Equal(epochs.First.environment_capacity, epochs.First.prev_environment_capacity);
            Assert.Equal(epochs.First.environment_capacity, epochs.Last.prev_environment_capacity);
        }

        [Fact]
        public void testEnvironmentCapacityScales()
        {
            Config config = new Config();
            Epoch epoch = new Epoch(config, 1000);
            epoch.end_year = 1500;
            epoch.prev_environment_capacity = 1000;
            epoch.environment_capacity = 2000;

            Assert.Equal(epoch.prev_environment_capacity, epoch.CapacityForYear(epoch.start_year));
            Assert.Equal(epoch.environment_capacity, epoch.CapacityForYear(epoch.end_year));
            Assert.Equal((epoch.environment_capacity + epoch.prev_environment_capacity) / 2, epoch.CapacityForYear((epoch.end_year + epoch.start_year) / 2));
        }

        [Fact]
        public void TestEpochWriteAndRead()
        {
            // Given a set of epochs
            Config config = new Config();
            Epochs epochs = UkPopulationEpochs.Define(config);
            int delta = 11;

            // When we write them and then read them
            string path = EpochsWriter.WriteCsv(Paths.PathOf("epochs"), Function.Undefined, config.GetNumberOfGenes(), config.GetSizeOfEachGene(), config.GetMutationsPerGene(), epochs);
            Epochs found = new Epochs();
            found.epochs.AddRange(EpochsReader.ReadEpochs(config, path));

            // Then we get back the original epochs
            Assert.Equal(epochs.epochs.Count, found.epochs.Count);
            for (int i = 0; i < epochs.epochs.Count; i++)
            {
                Epoch expected = epochs.epochs[i];
                Epoch actual = found.epochs[i];

                Assert.Equal(expected.start_year, actual.start_year);
                Assert.Equal(expected.end_year, actual.end_year);
                Assert.Equal(expected.environment_capacity, actual.environment_capacity);
                Assert.Equal(expected.BreedingProbability(), actual.BreedingProbability(), delta);
                Assert.Equal(expected.Disease(), actual.Disease());
                Assert.Equal(expected.Fitness(), actual.Fitness(), delta);
                Assert.Equal(expected.expected_max_population, actual.expected_max_population);
                Assert.Equal(expected.MaxAge(), actual.MaxAge());
                Assert.Equal(expected.MaxBreedingAge(), actual.MaxBreedingAge());
            }
        }
    }
}

