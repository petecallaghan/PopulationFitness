using PopulationFitness.Models;
using System;
using System.Collections.Generic;
using Xunit;

namespace TestPopulationFitness.UnitTests
{
    public class PopulationTest
    {
        private static readonly int INITIAL_POPULATION_SIZE = 30;
        private static readonly int BIRTH_YEAR = 1964;

        [Fact]
        public void TestCreateNewNonEmptyPopulation()
        {
            // Given a small population
            Config config = new Config();
            config.SetInitialPopulation(INITIAL_POPULATION_SIZE);
            Population population = new Population(config);
            Epoch epoch = new Epoch(config, BIRTH_YEAR);

            // When the population is created from scratch
            population.AddNewIndividuals(epoch, BIRTH_YEAR);

            // Then it has the right number of individuals for the birth year
            // and each individual has a set of genes
            Assert.Equal(config.GetInitialPopulation(), population.individuals.Count);
            foreach (Individual i in population.individuals)
            {
                Assert.Equal(BIRTH_YEAR, i.birth_year);
                Assert.False(i.genes.AreEmpty());
            }
        }

        [Fact]
        public void TestKillOffUnfit()
        {
            // Given a population that contains just babies
            Config config = new Config();
            Epochs epochs = new Epochs();
            Epoch epoch = new Epoch(config, -50)
            {
                environment_capacity = INITIAL_POPULATION_SIZE,
                expected_max_population = INITIAL_POPULATION_SIZE
            };
            epochs.AddNextEpoch(epoch);
            config.SetInitialPopulation(INITIAL_POPULATION_SIZE);
            Population population = new Population(config);
            population.AddNewIndividuals(epoch, BIRTH_YEAR);

            // When we kill off the unfit
            int fatalities = population.KillThoseUnfitOrReadyToDie(BIRTH_YEAR, epochs.First);

            // Some remain and some were killed
            Assert.True(0 < population.individuals.Count);
            Assert.True(0 < fatalities);
            Assert.True(INITIAL_POPULATION_SIZE > population.individuals.Count);
            Assert.True(Math.Abs(1.0 - population.CapacityFactor()) < 0.1);
        }

        [Fact]
        public void TestCreateANewGenerationFromTheCurrentOne()
        {
            // Given a population that is a mix of ages...
            Config config = new Config();
            config.SetInitialPopulation(INITIAL_POPULATION_SIZE);
            Population population = new Population(config);
            int current_year = BIRTH_YEAR + config.GetMaxBreedingAge();
            int breeding_birth_year = current_year - config.GetMinBreedingAge();
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
            Assert.True(babies.Count >= (config.GetInitialPopulation() * config.GetProbabilityOfBreeding() / 2) - config.GetInitialPopulation() / 5);
            Assert.True(babies.Count <= (config.GetInitialPopulation() * config.GetProbabilityOfBreeding()) + config.GetInitialPopulation() / 5);
            Assert.Equal((config.GetInitialPopulation() * 3) + babies.Count, population.individuals.Count);
        }
    }
}
