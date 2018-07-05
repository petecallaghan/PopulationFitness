using CsvHelper;
using PopulationFitness.Models;
using System;
using System.IO;
using System.Linq;

namespace PopulationFitness.Output
{
    public class GenerationsWriter
    {
        private static String FilePath(int parallel_run, int series_run, int number_of_runs, Config config)
        {
            return "generations" + "-" +
                    parallel_run +
                    "-" +
                    series_run +
                    "of" +
                    number_of_runs +
                    "-" +
                    config.GenesFactory.FitnessFunction +
                    "-genes" +
                    config.NumberOfGenes +
                    "x" +
                    config.SizeOfEachGene +
                    "-pop" +
                    config.InitialPopulation +
                    "-mut" +
                    config.MutationsPerGene +
                    "-" +
                    config.Id.Replace(":", "-") +
                    ".csv";
        }

        private static void WriteCsv(int parallel_run, int series_run, int number_of_runs, Generations generations, Tuning tuning)
        {
            WriteCsv(FilePath(parallel_run, series_run, number_of_runs, generations.Config), generations, tuning);
        }

        public static String WriteCsv(String filePath, Generations generations, Tuning tuning)
        {
            using (var file = CreateCsvWriter(filePath))
            {
                var records = generations.History.Select(generation => { return CreateRecord(generation); });
                var writer = new CsvWriter(file);
                writer.WriteRecords(records);
            }
            return filePath;
        }

        private static object CreateRecord(GenerationStatistics generation)
        {
            return new
            {
                EpochStartYear = generation.Epoch.StartYear,
                EpochEndYear = generation.Epoch.EndYear,
                EpochEnvironmentCapacity = generation.Epoch.CapacityForYear(generation.Year),
                EpochEnableFitness = generation.Epoch.IsFitnessEnabled,
                EpochBreedingProbability = generation.Epoch.BreedingProbability(), 
                Year = generation.Year,
                EpochFitnessFactor = generation.Epoch.Fitness(),
                EpochExpectedMaxPopulation = generation.Epoch.ExpectedMaxPopulation,
                Population = generation.Population,
                NumberBorn = generation.NumberBorn,
                NumberKilled = generation.NumberKilled,
                BornElapsed = generation.BornElapsedInHundredths(),
                KillElapsed = generation.KillElapsedInHundredths(),
                AvgFitness = generation.AverageFitness,
                FitnessDeviation = generation.FitnessDeviation,
                AverageAge = generation.AverageAge,
                CapacityFactor = generation.CapacityFactor,
                AvgFactoredFitness = generation.AverageFactoredFitness,
                AvgMutations = generation.AverageMutations,
                AvgLifeExpectancy = generation.AverageLifeExpectancy,
            };
        }

        private static StreamWriter CreateCsvWriter(String filePath)
        {
            try
            {
                return new StreamWriter(filePath);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
                return new StreamWriter(TryTemporaryVersionOf(filePath));
            }
        }

        private static String TryTemporaryVersionOf(String filePath)
        {
            return "~tmp." + filePath;
        }

        /**
         * Adds the two generation histories together and writes the result as a CSV file.
         *
         * @param parallelRun
         * @param seriesRun
         * @param current
         * @param total
         * @param tuning
         * @return
         * @throws IOException
         */
        public static Generations CombineGenerationsAndWriteResult(int parallelRun,
                                                                   int seriesRun,
                                                                   Generations current,
                                                                   Generations total,
                                                                   Tuning tuning)
        {
            total = (total == null ? current : Generations.Add(total, current));
            WriteCsv(parallelRun, seriesRun, tuning.SeriesRuns * tuning.ParallelRuns, total, tuning);
            return total;
        }

        /**
         * Creates a result file name from the file id.
         *
         * @param id
         * @return the resulting file name
         */
        public static String CreateResultFileName(String id)
        {
            return "allgenerations" + "-" +
                    id +
                    ".csv";
        }
    }
}