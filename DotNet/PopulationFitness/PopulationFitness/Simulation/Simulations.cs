using PopulationFitness.Models;
using PopulationFitness.Models.Genes.Cache;
using PopulationFitness.Output;
using System;
using System.Collections.Generic;

namespace PopulationFitness.Simulation
{
    public class Simulations
    {
        /**
         * Launches child runs for the number of runs defined in the tuning file.
         *
         * Waits for all the children to complete and then combines the results into a single file.
         *
         * @param factory
         * @param cacheType
         */
        public static void RunAllInParallel(ISimulationFactory factory, CacheType cacheType)
        {
            var simulations = LaunchSimulations(factory);
            WriteResultsWhenComplete(factory.Tuning, simulations);
        }

        private static List<Simulation> LaunchSimulations(ISimulationFactory factory)
        {
            var simulations = new List<Simulation>();

            for (int run = 1; run <= factory.Tuning.ParallelRuns; run++)
            {
                var simulation = factory.CreateNew(run);
                simulation.Start();
                simulations.Add(simulation);
            }
            return simulations;
        }

        private static void WriteResultsWhenComplete(Tuning tuning, List<Simulation> simulations)
        {
            Generations total = null;

            foreach (var simulation in simulations)
            {
                try
                {
                    simulation.Join();
                }
                catch (Exception e)
                {
                    Console.Write(e.StackTrace);
                }
                if (tuning.ParallelRuns > 1)
                {
                    total = GenerationsWriter.CombineGenerationsAndWriteResult(tuning.ParallelRuns,
                            tuning.SeriesRuns,
                            simulation.Generations,
                            total,
                            tuning);
                }
                else
                {
                    total = simulation.Generations;
                }
            }
            WriteFinalResults(tuning, total);
        }

        private static void WriteFinalResults(Tuning tuning, Generations total)
        {
            GenerationsWriter.WriteCsv(GenerationsWriter.CreateResultFileName(total.Config.Id), total, tuning);
        }

        /**
         * Adds the simulation epochs to the historical epochs read from the file
         *
         * @param config
         * @param epochs
         * @param tuning
         * @param diseaseYears
         * @param postDiseaseYears
         */
        public static void AddSimulatedEpochsToEndOfTunedEpochs(Config config,
                                                                Epochs epochs,
                                                                Tuning tuning,
                                                                int diseaseYears,
                                                                int postDiseaseYears)
        {
            int diseaseStartYear = epochs.Last.EndYear + 1;
            int recoveryStartYear = diseaseStartYear + diseaseYears;
            int finalYear = recoveryStartYear + postDiseaseYears;
            int maxExpected = epochs.Last.ExpectedMaxPopulation;

            epochs.AddNextEpoch(new Epoch(config, diseaseStartYear).Fitness(tuning.DiseaseFit).Max(maxExpected).BreedingProbability(tuning.ModernBreeding));
            epochs.AddNextEpoch(new Epoch(config, recoveryStartYear).Fitness(tuning.HistoricFit).Max(maxExpected));
            epochs.SetFinalEpochYear(finalYear);
        }

        /**
         * Changes the initial population size of the simulation using the first epoch expected population.
         *
         * @param config
         * @param epochs
         */
        public static void SetInitialPopulationFromFirstEpochCapacity(Config config, Epochs epochs)
        {
            config.InitialPopulation = epochs.First.ExpectedMaxPopulation;
        }
    }
}
