using CsvHelper;
using PopulationFitness.Models;
using System;
using System.Collections.Generic;
using System.IO;

namespace PopulationFitness.Output
{
    public class GenerationsReader
    {
        public static List<GenerationStatistics> ReadGenerations(Config config, String path)
        {
            List<GenerationStatistics> generations = new List<GenerationStatistics>();

            var reader = new CsvReader(File.OpenText(path));

            reader.Read();
            reader.ReadHeader();
            while (reader.Read())
            {
                try
                {
                    generations.Add(ReadFromRow(config, reader));
                }
                catch (Exception)
                {
                    break;
                }
            }

            return generations;
        }

        private static GenerationStatistics ReadFromRow(Config config, CsvReader row)
        {
            GenerationStatistics generation = new GenerationStatistics(new Epoch(config,
                    row.GetField<int>(0)),
                    row.GetField<int>(5),
                    row.GetField<int>(8),
                    row.GetField<int>(9),
                    row.GetField<int>(10),
                    (int)(row.GetField<double>(11) * 1000),
                    (int)(row.GetField<double>(12) * 1000),
                    row.GetField<double>(16),
                    row.GetField<double>(18));
            generation.Epoch.EndYear = row.GetField<int>(1);
            generation.Epoch.EnvironmentCapacity = (int)row.GetField<double>(2);
            generation.Epoch.EnableFitness = row.GetField<bool>(3);
            generation.Epoch.BreedingProbability(row.GetField<double>(4));

            generation.Epoch.Fitness(row.GetField<double>(6));
            generation.Epoch.ExpectedMaxPopulation = row.GetField<int>(7);
            generation.AverageFitness = row.GetField<double>(13);
            generation.FitnessDeviation = row.GetField<double>(14);
            generation.AverageAge = row.GetField<double>(15);
            generation.AverageFactoredFitness = row.GetField<double>(17);
            generation.AverageLifeExpectancy = row.GetField<double>(19);
            return generation;
        }
    }
}
