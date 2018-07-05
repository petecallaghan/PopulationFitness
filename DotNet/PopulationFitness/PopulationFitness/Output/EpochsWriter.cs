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
                var records = epochs.All.Select(epoch => { return CreateRecord(epoch); });
                var writer = new CsvWriter(file);
                writer.WriteRecords(records);
            }
            return filePath;
        }

        private static object CreateRecord(Epoch epoch)
        {
            return new
            {
                StartYear = epoch.StartYear,
                EndYear = epoch.EndYear,
                EnvironmentCapacity = epoch.EnvironmentCapacity,
                BreedingProbability = epoch.BreedingProbability(),
                Disease = epoch.Disease(),
                FitnessFactor = epoch.Fitness(),
                ExpectedMaxPopulation = epoch.ExpectedMaxPopulation,
                MaxAge = epoch.MaxAge(),
                MaxBreedingAge = epoch.MaxBreedingAge(),
                AvgCapacityFactor = epoch.AverageCapacityFactor(),
                AvgCapacityFitness = epoch.AverageCapacityFactor() * epoch.Fitness(),
                Mutations = epoch.Config.MutationsPerGene
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
