using PopulationFitness.Models;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class IndividualTest
    {
        [TestCase]
        public void TSestOldGuyReadyToDie()
        {
            // Given an old individual
            Config config = new Config();
            int birth_year = 1964;
            int current_year = birth_year + config.MaxAge + 10;
            Epoch epoch = new Epoch(config, current_year);
            Individual individual = new Individual(epoch, birth_year);

            // Then they are ready to die
            Assert.True(individual.IsReadyToDie(current_year));
        }

        [TestCase]
        public void TestYoungGuyNotReadyToDie()
        {
            // Given a young individual
            Config config = new Config();
            int birth_year = 1964;
            int current_year = birth_year + config.MaxAge - 10;
            Epoch epoch = new Epoch(config, current_year);
            Individual individual = new Individual(epoch, birth_year);

            // Then they are not ready to die
            Assert.False(individual.IsReadyToDie(current_year));
        }

        [TestCase]
        public void TestCanBreed()
        {
            // Given a breeding age individual
            Config config = new Config();
            int birth_year = 1964;
            int current_year = birth_year + config.MinBreedingAge + 1;
            Epoch epoch = new Epoch(config, current_year);
            Individual individual = new Individual(epoch, birth_year);

            // Then they can breed
            Assert.True(individual.CanBreed(current_year));
        }

        [TestCase]
        public void TestYoungsterCannotBreed()
        {
            // Given a breeding age individual
            Config config = new Config();
            int birth_year = 1964;
            int current_year = birth_year + config.MinBreedingAge - 1;
            Epoch epoch = new Epoch(config, current_year);
            Individual individual = new Individual(epoch, birth_year);

            // Then they can breed
            Assert.False(individual.CanBreed(current_year));
        }

        [TestCase]
        public void TestOldsterCannotBreed()
        {
            // Given a breeding age individual
            Config config = new Config();
            int birth_year = 1964;
            int current_year = birth_year + config.MaxBreedingAge + 1;
            Epoch epoch = new Epoch(config, current_year);
            Individual individual = new Individual(epoch, birth_year);

            // Then they can breed
            Assert.False(individual.CanBreed(current_year));
        }
    }
}
