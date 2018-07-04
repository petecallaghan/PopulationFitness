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
                    config.GetGenesFactory().GetFitnessFunction() +
                    "-genes" +
                    config.GetNumberOfGenes() +
                    "x" +
                    config.GetSizeOfEachGene() +
                    "-pop" +
                    config.GetInitialPopulation() +
                    "-mut" +
                    config.GetMutationsPerGene() +
                    "-" +
                    config.id.Replace(":", "-") +
                    ".csv";
        }

        private static void WriteCsv(int parallel_run, int series_run, int number_of_runs, Generations generations, Tuning tuning)
        {
            WriteCsv(FilePath(parallel_run, series_run, number_of_runs, generations.config), generations, tuning);
        }

        public static String WriteCsv(String filePath, Generations generations, Tuning tuning)
        {
            using (var file = CreateCsvWriter(filePath))
            {
                var records = generations.history.Select(generation => { return CreateRecord(generation); });
                var writer = new CsvWriter(file);
                writer.WriteRecords(records);
            }
            return filePath;
        }

        private static object CreateRecord(GenerationStatistics generation)
        {
            return new
            {
                EpochStartYear = generation.epoch.start_year,
                EpochEndYear = generation.epoch.end_year,
                EpochEnvironmentCapacity = generation.epoch.CapacityForYear(generation.year),
                EpochEnableFitness = generation.epoch.IsFitnessEnabled,
                EpochBreedingProbability = generation.epoch.BreedingProbability(), 
                Year = generation.year,
                EpochFitnessFactor = generation.epoch.Fitness(),
                EpochExpectedMaxPopulation = generation.epoch.expected_max_population,
                Population = generation.population,
                NumberBorn = generation.number_born,
                NumberKilled = generation.number_killed,
                BornElapsed = generation.BornElapsedInHundredths(),
                KillElapsed = generation.KillElapsedInHundredths(),
                AvgFitness = generation.average_fitness,
                FitnessDeviation = generation.fitness_deviation,
                AverageAge = generation.average_age,
                CapacityFactor = generation.capacity_factor,
                AvgFactoredFitness = generation.average_factored_fitness,
                AvgMutations = generation.average_mutations,
                AvgLifeExpectancy = generation.average_life_expectancy,
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
            WriteCsv(parallelRun, seriesRun, tuning.series_runs * tuning.parallel_runs, total, tuning);
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