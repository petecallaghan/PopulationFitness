using System;
using System.Collections.Generic;

namespace PopulationFitness.Models
{
    /**
     * Created by pete.callaghan on 03/07/2017.
     */
    public class Population
    {
        public readonly Config Config;

        public List<Individual> Individuals;

        public double AverageAge = 0.0;
        public double AverageMutations = 0.0;
        public double AverageLifeExpectancy = 0.0;

        private double _totalFitness = 0.0;
        private double _totalAgeAtDeath = 0.0;
        private double _totalFactoredFitness = 0.0;
        private int _checkedFitness = 0;
        private List<double> _fitnesses;
        private double _environmentCapacity = 0.0;

        public Population(Config config)
        {
            Config = config;
            Individuals = new List<Individual>();
            _fitnesses = new List<double>();
        }

        public Population(Population source) : this(source.Config)
        {
            Individuals.AddRange(source.Individuals);
            _fitnesses.AddRange(source._fitnesses);
        }

        /**
         * Adds new individuals to the population. Adds the number configured as the population size.
         *
         * @param birth_year
         */
        public void AddNewIndividuals(Epoch epoch, int birth_year)
        {
            for (int i = 0; i < Config.InitialPopulation; i++)
            {
                Individual individual = new Individual(epoch, birth_year);
                individual.Genes.BuildFromRandom();
                Individuals.Add(individual);
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

            for (int i = 0; i < Individuals.Count - 1; i += 2)
            {
                Individual father = Individuals[i];
                Individual mother = Individuals[i + 1];
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
            Individuals.AddRange(babies);
            AverageAge = Individuals.Count > 0 ? totalAge / Individuals.Count : 0.0;
            AverageMutations = babies.Count > 0 ? totalMutations / babies.Count : 0.0;
            return babies;
        }

        private bool Kill(Individual individual, int current_year, double fitness_factor)
        {
            double fitness = individual.Genes.Fitness;
            double factored_fitness = fitness * fitness_factor;
            _totalFitness += fitness;
            _totalFactoredFitness += factored_fitness;
            _checkedFitness++;
            _fitnesses.Add(fitness);

            if (individual.IsReadyToDie(current_year) ? true : factored_fitness < (RepeatableRandom.GenerateNext()))
            {
                _totalAgeAtDeath += individual.Age(current_year);
                return true;
            }
            return false;
        }

        private int AddSurvivors(Predicate<Individual> survivor)
        {
            int population_size = Individuals.Count;
            _totalAgeAtDeath = 0.0;
            Individuals = Individuals.FindAll(survivor);
            int killed = population_size - Individuals.Count;
            AverageLifeExpectancy = killed < 1 ? 0.0 : Math.Round(_totalAgeAtDeath / killed);
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
            _totalFitness = 0.0;
            _totalFactoredFitness = 0.0;
            _checkedFitness = 0;
            _environmentCapacity = 0.0;
            _fitnesses = new List<double>();

            if (Individuals.Count < 1)
                return 0;

            if (epoch.IsCapacityUnlimited)
            {
                return AddSurvivors(i => !Kill(i, current_year, epoch.Fitness()));
            }

            _environmentCapacity = (double)(epoch.CapacityForYear(current_year)) / Individuals.Count;
            epoch.AddCapacityFactor(_environmentCapacity);
            return AddSurvivors(i => !Kill(i, current_year, epoch.Fitness() * _environmentCapacity));
        }

        public double AverageFitness()
        {
            return _checkedFitness > 0 ? _totalFitness / _checkedFitness : 0;
        }

        public double AverageFactoredFitness()
        {
            return _checkedFitness > 0 ? _totalFactoredFitness / _checkedFitness : 0;
        }

        public double CapacityFactor()
        {
            return _environmentCapacity;
        }

        public double StandardDeviationFitness()
        {
            if (_checkedFitness < 1) return 0.0;
            double mean = AverageFitness();
            double variance = 0.0;
            foreach (double f in _fitnesses)
            {
                double difference = f - mean;
                variance += difference * difference;
            }
            return Math.Sqrt(variance / _checkedFitness);
        }
    }
}
