using PopulationFitness;
using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using PopulationFitness.Output;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class EpochsTest
    {
        private static readonly int UNDEFINED_YEAR = -1;

        [TestCase]
        public void TestCreateEpochs()
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
            epochs.SetFinalEpochYear(-50 + config.NumberOfYears - 1);

            // When we iterate over the epochs
            int first_year = UNDEFINED_YEAR;
            int last_year = UNDEFINED_YEAR;
            int number_of_years = 0;

            foreach (Epoch e in epochs.All)
            {
                for (int year = e.StartYear; year <= e.EndYear; year++)
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
            Assert.AreEqual(-50, first_year);
            Assert.AreEqual(-50 + config.NumberOfYears - 1, last_year);
            Assert.AreEqual(+config.NumberOfYears, number_of_years);
        }

        [TestCase]
        public void TestEpochFitnessFactors()
        {
            // Given a set of epochs
            Config config = new Config();
            Epochs epochs = new Epochs();

            epochs.AddNextEpoch(new Epoch(config, -50));
            epochs.AddNextEpoch(new Epoch(config, 400).Fitness(2.0));

            // When we iterate over the epochs we find the right Fitness factors
            Assert.AreEqual(1.0, epochs.First.Fitness(), 0.1);
            Assert.AreEqual(2.0, epochs.Last.Fitness(), 0.1);
        }

        [TestCase]
        public void TestEpochEnvironmentCapacity()
        {
            // Given a set of epochs
            Config config = new Config();
            Epochs epochs = new Epochs();
            epochs.AddNextEpoch(new Epoch(config, -50).Capacity(1000));
            epochs.AddNextEpoch(new Epoch(config, 400));

            // When we iterate over the epochs we find the right environment capacity
            Assert.AreEqual(1000, epochs.First.EnvironmentCapacity);
            Assert.AreEqual(epochs.First.EnvironmentCapacity, epochs.First.ExpectedMaxPopulation);
            Assert.False(epochs.First.IsCapacityUnlimited);
            Assert.AreEqual(Epoch.UNLIMITED_CAPACITY, epochs.Last.EnvironmentCapacity);
            Assert.True(epochs.Last.IsCapacityUnlimited);
            Assert.AreEqual(epochs.First.EnvironmentCapacity, epochs.First.PrevEnvironmentCapacity);
            Assert.AreEqual(epochs.First.EnvironmentCapacity, epochs.Last.PrevEnvironmentCapacity);
        }

        [TestCase]
        public void TestEnvironmentCapacityScales()
        {
            Config config = new Config();
            Epoch epoch = new Epoch(config, 1000);
            epoch.EndYear = 1500;
            epoch.PrevEnvironmentCapacity = 1000;
            epoch.EnvironmentCapacity = 2000;

            Assert.AreEqual(epoch.PrevEnvironmentCapacity, epoch.CapacityForYear(epoch.StartYear));
            Assert.AreEqual(epoch.EnvironmentCapacity, epoch.CapacityForYear(epoch.EndYear));
            Assert.AreEqual((epoch.EnvironmentCapacity + epoch.PrevEnvironmentCapacity) / 2, epoch.CapacityForYear((epoch.EndYear + epoch.StartYear) / 2));
        }

        [TestCase]
        public void TestEpochWriteAndRead()
        {
            // Given a set of epochs
            Config config = new Config();
            Epochs epochs = UkPopulationEpochs.Define(config);
            double delta = 1.0e-11;

            // When we write them and then read them
            string path = EpochsWriter.WriteCsv(Paths.PathOf("epochs"), Function.Undefined, config.NumberOfGenes, config.SizeOfEachGene, config.MutationsPerGene, epochs);
            Epochs found = new Epochs();
            found.All.AddRange(EpochsReader.ReadEpochs(config, path));

            // Then we get back the original epochs
            Assert.AreEqual(epochs.All.Count, found.All.Count);
            for (int i = 0; i < epochs.All.Count; i++)
            {
                Epoch expected = epochs.All[i];
                Epoch actual = found.All[i];

                Assert.AreEqual(expected.StartYear, actual.StartYear);
                Assert.AreEqual(expected.EndYear, actual.EndYear);
                Assert.AreEqual(expected.EnvironmentCapacity, actual.EnvironmentCapacity);
                Assert.AreEqual(expected.BreedingProbability(), actual.BreedingProbability(), delta);
                Assert.AreEqual(expected.Disease(), actual.Disease());
                Assert.AreEqual(expected.Fitness(), actual.Fitness(), delta);
                Assert.AreEqual(expected.ExpectedMaxPopulation, actual.ExpectedMaxPopulation);
                Assert.AreEqual(expected.MaxAge(), actual.MaxAge());
                Assert.AreEqual(expected.MaxBreedingAge(), actual.MaxBreedingAge());
            }
        }
    }
}

