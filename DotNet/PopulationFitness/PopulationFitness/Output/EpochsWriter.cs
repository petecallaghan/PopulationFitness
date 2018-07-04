using CsvHelper;
using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using System;
using System.IO;
using System.Linq;

namespace PopulationFitness.Output
{
    public class EpochsWriter
    {
        private static String FilePath(String path, Function function, int genes, int size, double mutations)
        {
            return path + "/" + "functionepochs" + "-" +
                    function +
                    "-" +
                    genes +
                    "-" +
                    size +
                    "-" +
                    (int)mutations +
                    ".csv";
        }

        public static String WriteCsv(String path, Function function, int genes, int size, double mutations, Epochs epochs)
        {
            string filePath = FilePath(path, function, genes, size, mutations);
            DeleteExisting(filePath);

            using (var file = new StreamWriter(filePath))
            {
                var records = epochs.epochs.Select(epoch => { return CreateRecord(epoch); });
                var writer = new CsvWriter(file);
                writer.WriteRecords(records);
            }
            return filePath;
        }

        private static object CreateRecord(Epoch epoch)
        {
            return new
            {
                StartYear = epoch.start_year,
                EndYear = epoch.end_year,
                EnvironmentCapacity = epoch.environment_capacity,
                BreedingProbability = epoch.BreedingProbability(),
                Disease = epoch.Disease(),
                FitnessFactor = epoch.Fitness(),
                ExpectedMaxPopulation = epoch.expected_max_population,
                MaxAge = epoch.MaxAge(),
                MaxBreedingAge = epoch.MaxBreedingAge(),
                AvgCapacityFactor = epoch.AverageCapacityFactor(),
                AvgCapacityFitness = epoch.AverageCapacityFactor() * epoch.Fitness(),
                Mutations = epoch.Config().GetMutationsPerGene()
            };
        }

        public static void DeleteExisting(string path)
        {
            try
            {
                File.Delete(path);
            }
            catch (Exception)
            {
                Console.WriteLine("Could not delete " + path);
            }
        }
    }
}
