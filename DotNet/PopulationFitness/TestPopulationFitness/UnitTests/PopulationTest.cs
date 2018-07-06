using PopulationFitness.Models;
using System;
using System.Collections.Generic;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class PopulationTest
    {
        private static readonly int INITIAL_POPULATION_SIZE = 30;
        private static readonly int BIRTH_YEAR = 1964;

        [TestCase]
        public void TestCreateNewNonEmptyPopulation()
        {
            // Given a small population
            Config config = new Config
            {
                InitialPopulation = INITIAL_POPULATION_SIZE
            };
            Population population = new Population(config);
            Epoch epoch = new Epoch(config, BIRTH_YEAR);

            // When the population is created from scratch
            population.AddNewIndividuals(epoch, BIRTH_YEAR);

            // Then it has the right number of individuals for the birth year
            // and each individual has a set of genes
            Assert.AreEqual(config.InitialPopulation, population.Individuals.Count);
            foreach (Individual i in population.Individuals)
            {
                Assert.AreEqual(BIRTH_YEAR, i.BirthYear);
                Assert.False(i.Genes.AreEmpty);
            }
        }

        [TestCase]
        public void TestKillOffUnfit()
        {
            // Given a population that contains just babies
            Config config = new Config();
            Epochs epochs = new Epochs();
            Epoch epoch = new Epoch(config, -50)
            {
                EnvironmentCapacity = INITIAL_POPULATION_SIZE,
                ExpectedMaxPopulation = INITIAL_POPULATION_SIZE
            };
            epochs.AddNextEpoch(epoch);
            config.InitialPopulation = INITIAL_POPULATION_SIZE;
            Population population = new Population(config);
            population.AddNewIndividuals(epoch, BIRTH_YEAR);

            // When we kill off the unfit
            int fatalities = population.KillThoseUnfitOrReadyToDie(BIRTH_YEAR, epochs.First);

            // Some remain and some were killed
            Assert.True(0 < population.Individuals.Count);
            Assert.True(0 < fatalities);
            Assert.True(INITIAL_POPULATION_SIZE > population.Individuals.Count);
            Assert.True(Math.Abs(1.0 - population.CapacityFactor()) < 0.1);
        }

        [TestCase]
        public void TestCreateANewGenerationFromTheCurrentOne()
        {
            // Given a population that is a mix of ages...
            Config config = new Config
            {
                InitialPopulation = INITIAL_POPULATION_SIZE
            };
            Population population = new Population(config);
            int current_year = BIRTH_YEAR + config.MaxBreedingAge;
            int breeding_birth_year = current_year - config.MinBreedingAge;
            Epoch epoch = new Epoch(config, BIRTH_YEAR);
            // .. some who will be too old to breed
            population.AddNewIndividuals(epoch, BIRTH_YEAR);
            // .. some who will be too young to breed
            population.AddNewIndividuals(epoch, current_year);
            // .. some who can breed
            population.AddNewIndividuals(epoch, breeding_birth_year);

            // When we create a new population
            List<Individual> babies = population.AddNewGeneration(new Epoch(config, current_year), current_year);

            // The right number of babies are produced and the babies are added
            Assert.True(babies.Count >= (config.InitialPopulation * config.ProbabilityOfBreeding / 2) - config.InitialPopulation / 5);
            Assert.True(babies.Count <= (config.InitialPopulation * config.ProbabilityOfBreeding) + config.InitialPopulation / 5);
            Assert.AreEqual((config.InitialPopulation * 3) + babies.Count, population.Individuals.Count);
        }
    }
}
