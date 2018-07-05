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

        public readonly List<GenerationStatistics> History;
        public readonly Config Config;

        private Population _population;
        private int _firstYear;
        private readonly int _seriesRun;
        private readonly int _parallelRun;

        public Generations(Population population) : this(population, 1, 1)
        {
        }

        public Generations(Population population, int parallel_run, int series_run)
        {
            this._population = population;
            this.Config = population.Config;
            this._seriesRun = series_run;
            this._parallelRun = parallel_run;
            History = new List<GenerationStatistics>();
            _firstYear = UNDEFINED_YEAR;
        }

        public void CreateForAllEpochs(Epochs epochs)
        {
            AddInitialPopulation(epochs);

            foreach (Epoch epoch in epochs.All)
            {
                for (int year = epoch.StartYear; year <= epoch.EndYear; year++)
                {
                    GenerateForYear(year, epoch);
                }
            }
        }

        private void AddInitialPopulation(Epochs epochs)
        {
            Epoch epoch = epochs.First;
            _firstYear = epoch.StartYear;
            _population.AddNewIndividuals(epoch, _firstYear);
        }

        public PopulationComparison TuneFitnessFactorsForAllEpochs(Epochs epochs, double minFactor, double maxFactor, double increment, int percentage)
        {
            AddInitialPopulation(epochs);

            PopulationComparison divergence = PopulationComparison.TooLow;
            Epoch previousEpoch = null;
            foreach (Epoch epoch in epochs.All)
            {
                Population previousPopulation = new Population(_population);
                Search search = new Search();
                search.Increment(increment).Min(minFactor).Max(maxFactor);
                if (previousEpoch != null)
                {
                    search.Current = previousEpoch.Fitness();
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
                ICollection<IGenesIdentifier> survivors = _population.Individuals.Select(i => i.Genes.Identifier).ToList<IGenesIdentifier>();
                SharedCache.Cache.RetainOnly(survivors);
            }
        }

        private PopulationComparison GenerateAndCompareEpochPopulation(
                Search search,
                int percentage,
                Epoch epoch,
                Population previousPopulation)
        {
            _population = new Population(previousPopulation);
            epoch.Fitness(search.Current);
            PopulationComparison divergence = CompareToExpectedForEpoch(epoch, percentage);
            Console.WriteLine("Year " + epoch.EndYear + " Pop " + _population.Individuals.Count + " Expected " + epoch.ExpectedMaxPopulation + " F=" + epoch.Fitness() + " F'=" + epoch.AverageCapacityFactor() * epoch.Fitness());

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
            for (int year = epoch.StartYear; year <= epoch.EndYear; year++)
            {
                _population.KillThoseUnfitOrReadyToDie(year, epoch);
                _population.AddNewGeneration(epoch, year);
                PopulationComparison divergence = CompareToExpected(epoch, year, _population.Individuals.Count, percentage);
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

            if (year >= epoch.EndYear)
            {
                if (divergence >= max_divergence) return PopulationComparison.TooHigh;

                if (divergence < 0 - max_divergence) return PopulationComparison.TooLow;
            }

            return PopulationComparison.WithinRange;
        }

        private void GenerateForYear(int year, Epoch epoch)
        {
            Stopwatch stopWatch = Stopwatch.StartNew();
            int fatalities = _population.KillThoseUnfitOrReadyToDie(year, epoch);
            FlushBodiesFromTheCache();
            long kill_elapsed = GenesTimer.GetElapsed(stopWatch);

            stopWatch = Stopwatch.StartNew();
            List<Individual> babies = _population.AddNewGeneration(epoch, year);
            long born_elapsed = GenesTimer.GetElapsed(stopWatch);

            AddHistory(epoch, year, babies.Count, fatalities, born_elapsed, kill_elapsed);
        }

        private void AddHistory(Epoch epoch, int year, int number_born, int number_killed, long born_elapsed, long kill_elapsed)
        {
            GenerationStatistics generation = new GenerationStatistics(epoch,
                    year,
                    _population.Individuals.Count,
                    number_born,
                    number_killed,
                    born_elapsed,
                    kill_elapsed,
                    _population.CapacityFactor(),
                    _population.AverageMutations);
            generation.AverageFitness = _population.AverageFitness();
            generation.AverageFactoredFitness = _population.AverageFactoredFitness();
            generation.FitnessDeviation = _population.StandardDeviationFitness();
            generation.AverageAge = _population.AverageAge;
            generation.AverageLifeExpectancy = _population.AverageLifeExpectancy;
            History.Add(generation);

            Console.WriteLine("Run " + _parallelRun + "x" + _seriesRun + " Year " + generation.Year + " Pop " + generation.Population + " Expected " + epoch.ExpectedMaxPopulation + " Born " + generation.NumberBorn + " in " + generation.BornElapsedInHundredths() + "s Killed " + generation.NumberKilled + " in " + generation.KillElapsedInHundredths() + "s");
        }

        public static Generations Add(Generations first, Generations second)
        {
            Generations result = new Generations(first._population);
            result._firstYear = first._firstYear;
            result.History.AddRange(GenerationStatistics.Add(first.History, second.History));
            return result;
        }
    }
}
