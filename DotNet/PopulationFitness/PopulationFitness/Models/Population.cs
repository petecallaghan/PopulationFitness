using System;
using System.Collections.Generic;

namespace PopulationFitness.Models
{
    /**
     * Created by pete.callaghan on 03/07/2017.
     */
    public class Population
    {
        public readonly Config config;

        public List<Individual> individuals;

        public double average_age = 0.0;
        public double average_mutations = 0.0;
        public double average_life_expectancy = 0.0;

        private double total_fitness = 0.0;
        private double total_age_at_death = 0.0;
        private double total_factored_fitness = 0.0;
        private int checked_fitness = 0;
        private List<double> fitnesses;
        private double environment_capacity = 0.0;

        public Population(Config config)
        {
            this.config = config;
            individuals = new List<Individual>();
            fitnesses = new List<double>();
        }

        public Population(Population source) : this(source.config)
        {
            individuals.AddRange(source.individuals);
            fitnesses.AddRange(source.fitnesses);
        }

        /**
         * Adds new individuals to the population. Adds the number configured as the population size.
         *
         * @param birth_year
         */
        public void AddNewIndividuals(Epoch epoch, int birth_year)
        {
            for (int i = 0; i < config.GetInitialPopulation(); i++)
            {
                Individual individual = new Individual(epoch, birth_year);
                individual.genes.BuildFromRandom();
                individuals.Add(individual);
            }
        }

        /**
         * Produces a new generation. Adds the generation to the population.
         *
         * @param current_year
         * @return The newly created set of babies
         */
        public List<Individual> AddNewGeneration(Epoch epoch, int current_year)
        {
            long totalAge = 0;
            List<Individual> babies = new List<Individual>();
            double totalMutations = 0;

            for (int i = 0; i < individuals.Count - 1; i += 2)
            {
                Individual father = individuals[i];
                Individual mother = individuals[i + 1];
                totalAge += father.Age(current_year);
                totalAge += mother.Age(current_year);
                bool pairCanBreed = RepeatableRandom.GenerateNext() < epoch.BreedingProbability();
                if (pairCanBreed && father.CanBreed(current_year) && mother.CanBreed(current_year))
                {
                    Individual baby = new Individual(epoch, current_year);
                    totalMutations += baby.InheritFromParents(mother, father);
                    babies.Add(baby);
                }
            }
            individuals.AddRange(babies);
            average_age = individuals.Count > 0 ? totalAge / individuals.Count : 0.0;
            average_mutations = babies.Count > 0 ? totalMutations / babies.Count : 0.0;
            return babies;
        }

        private bool Kill(Individual individual, int current_year, double fitness_factor)
        {
            double fitness = individual.genes.Fitness();
            double factored_fitness = fitness * fitness_factor;
            total_fitness += fitness;
            total_factored_fitness += factored_fitness;
            checked_fitness++;
            fitnesses.Add(fitness);

            if (individual.IsReadyToDie(current_year) ? true : factored_fitness < (RepeatableRandom.GenerateNext()))
            {
                total_age_at_death += individual.Age(current_year);
                return true;
            }
            return false;
        }

        private int AddSurvivors(Predicate<Individual> survivor)
        {
            int population_size = individuals.Count;
            total_age_at_death = 0.0;
            individuals = individuals.FindAll(survivor);
            int killed = population_size - individuals.Count;
            average_life_expectancy = killed < 1 ? 0.0 : Math.Round(total_age_at_death / killed);
            return killed;
        }

        /**
         * Kills those in the population who are ready to die and returns the number of fatalities
         *
         * @param current_year
         * @param epoch
         * @return
         */
        public int KillThoseUnfitOrReadyToDie(int current_year, Epoch epoch)
        {
            total_fitness = 0.0;
            total_factored_fitness = 0.0;
            checked_fitness = 0;
            environment_capacity = 0.0;
            fitnesses = new List<double>();

            if (individuals.Count < 1)
                return 0;

            if (epoch.IsCapacityUnlimited)
            {
                return AddSurvivors(i => !Kill(i, current_year, epoch.Fitness()));
            }

            environment_capacity = (double)(epoch.CapacityForYear(current_year)) / individuals.Count;
            epoch.AddCapacityFactor(environment_capacity);
            return AddSurvivors(i => !Kill(i, current_year, epoch.Fitness() * environment_capacity));
        }

        public double AverageFitness()
        {
            return checked_fitness > 0 ? total_fitness / checked_fitness : 0;
        }

        public double AverageFactoredFitness()
        {
            return checked_fitness > 0 ? total_factored_fitness / checked_fitness : 0;
        }

        public double CapacityFactor()
        {
            return environment_capacity;
        }

        public double StandardDeviationFitness()
        {
            if (checked_fitness < 1) return 0.0;
            double mean = AverageFitness();
            double variance = 0.0;
            foreach (double f in fitnesses)
            {
                double difference = f - mean;
                variance += difference * difference;
            }
            return Math.Sqrt(variance / checked_fitness);
        }
    }
}
