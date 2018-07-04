using PopulationFitness.Models.Genes;
using PopulationFitness.Models.Genes.Cache;
using PopulationFitness.Models.Genes.Fitness;
using PopulationFitness.Models.Genes.Performance;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;

namespace PopulationFitness.Models
{
    /**
     * The generations recorded for a population
     *
     * Created by pete.callaghan on 04/07/2017.
     */
    public class Generations
    {
        private static readonly int UNDEFINED_YEAR = -1;

        public readonly List<GenerationStatistics> history;

        private Population population;

        public readonly Config config;

        private int first_year;

        private readonly int series_run;

        private readonly int parallel_run;

        public Generations(Population population) : this(population, 1, 1)
        {
        }

        public Generations(Population population, int parallel_run, int series_run)
        {
            this.population = population;
            this.config = population.config;
            this.series_run = series_run;
            this.parallel_run = parallel_run;
            history = new List<GenerationStatistics>();
            first_year = UNDEFINED_YEAR;
        }

        public void CreateForAllEpochs(Epochs epochs)
        {
            AddInitialPopulation(epochs);

            foreach (Epoch epoch in epochs.epochs)
            {
                for (int year = epoch.start_year; year <= epoch.end_year; year++)
                {
                    GenerateForYear(year, epoch);
                }
            }
        }

        private void AddInitialPopulation(Epochs epochs)
        {
            Epoch epoch = epochs.First;
            first_year = epoch.start_year;
            population.AddNewIndividuals(epoch, first_year);
        }

        public PopulationComparison TuneFitnessFactorsForAllEpochs(Epochs epochs, double minFactor, double maxFactor, double increment, int percentage)
        {
            AddInitialPopulation(epochs);

            PopulationComparison divergence = PopulationComparison.TooLow;
            Epoch previousEpoch = null;
            foreach (Epoch epoch in epochs.epochs)
            {
                Population previousPopulation = new Population(population);
                Search search = new Search();
                search.Increment(increment).Min(minFactor).Max(maxFactor);
                if (previousEpoch != null)
                {
                    search.Current(previousEpoch.Fitness());
                }
                previousEpoch = epoch;
                divergence = GenerateAndCompareEpochPopulation(search, percentage, epoch, previousPopulation);
                if (divergence != PopulationComparison.WithinRange)
                {
                    FlushBodiesFromTheCache();
                    return divergence;
                }
                FlushBodiesFromTheCache();
            }
            return divergence;
        }

        private void FlushBodiesFromTheCache()
        {
            if (SharedCache.Cache.IsFlushable)
            {
                ICollection<IGenesIdentifier> survivors = population.individuals.Select(i => i.genes.Identifier()).ToList<IGenesIdentifier>();
                SharedCache.Cache.RetainOnly(survivors);
            }
        }

        private PopulationComparison GenerateAndCompareEpochPopulation(
                Search search,
                int percentage,
                Epoch epoch,
                Population previousPopulation)
        {
            population = new Population(previousPopulation);
            epoch.Fitness(search.Current());
            PopulationComparison divergence = CompareToExpectedForEpoch(epoch, percentage);
            Console.WriteLine("Year " + epoch.end_year + " Pop " + population.individuals.Count + " Expected " + epoch.expected_max_population + " F=" + epoch.Fitness() + " F'=" + epoch.AverageCapacityFactor() * epoch.Fitness());

            if (divergence != PopulationComparison.WithinRange)
            {
                Search nextSearch = search.FindNext(divergence);
                if (nextSearch == null) return divergence;
                return GenerateAndCompareEpochPopulation(nextSearch, percentage, epoch, previousPopulation);
            }
            return divergence;
        }

        private PopulationComparison CompareToExpectedForEpoch(Epoch epoch, int percentage)
        {
            for (int year = epoch.start_year; year <= epoch.end_year; year++)
            {
                population.KillThoseUnfitOrReadyToDie(year, epoch);
                population.AddNewGeneration(epoch, year);
                PopulationComparison divergence = CompareToExpected(epoch, year, population.individuals.Count, percentage);
                if (divergence != PopulationComparison.WithinRange)
                {
                    return divergence;
                }
            }
            return PopulationComparison.WithinRange;
        }

        private static PopulationComparison CompareToExpected(Epoch epoch, int year, int population, int percentage)
        {
            if (population == 0) return PopulationComparison.TooLow;

            int expected = epoch.CapacityForYear(year);

            if (population >= expected * 2) return PopulationComparison.TooHigh;

            int divergence = (population - expected) * 100;

            int max_divergence = expected * percentage;

            if (year >= epoch.end_year)
            {
                if (divergence >= max_divergence) return PopulationComparison.TooHigh;

                if (divergence < 0 - max_divergence) return PopulationComparison.TooLow;
            }

            return PopulationComparison.WithinRange;
        }

        private void GenerateForYear(int year, Epoch epoch)
        {
            Stopwatch stopWatch = Stopwatch.StartNew();
            int fatalities = population.KillThoseUnfitOrReadyToDie(year, epoch);
            FlushBodiesFromTheCache();
            long kill_elapsed = GenesTimer.GetElapsed(stopWatch);

            stopWatch = Stopwatch.StartNew();
            List<Individual> babies = population.AddNewGeneration(epoch, year);
            long born_elapsed = GenesTimer.GetElapsed(stopWatch);

            AddHistory(epoch, year, babies.Count, fatalities, born_elapsed, kill_elapsed);
        }

        private void AddHistory(Epoch epoch, int year, int number_born, int number_killed, long born_elapsed, long kill_elapsed)
        {
            GenerationStatistics generation = new GenerationStatistics(epoch,
                    year,
                    population.individuals.Count,
                    number_born,
                    number_killed,
                    born_elapsed,
                    kill_elapsed,
                    population.CapacityFactor(),
                    population.average_mutations);
            generation.average_fitness = population.AverageFitness();
            generation.average_factored_fitness = population.AverageFactoredFitness();
            generation.fitness_deviation = population.StandardDeviationFitness();
            generation.average_age = population.average_age;
            generation.average_life_expectancy = population.average_life_expectancy;
            history.Add(generation);

            Console.WriteLine("Run " + parallel_run + "x" + series_run + " Year " + generation.year + " Pop " + generation.population + " Expected " + epoch.expected_max_population + " Born " + generation.number_born + " in " + generation.BornElapsedInHundredths() + "s Killed " + generation.number_killed + " in " + generation.KillElapsedInHundredths() + "s");
        }

        public static Generations Add(Generations first, Generations second)
        {
            Generations result = new Generations(first.population);
            result.first_year = first.first_year;
            result.history.AddRange(GenerationStatistics.Add(first.history, second.history));
            return result;
        }
    }
}
