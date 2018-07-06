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
                    (int)(row.GetDoubleFieldWithoutRounding(11) * 1000.0),
                    (int)(row.GetDoubleFieldWithoutRounding(12) * 1000.0),
                    row.GetDoubleFieldWithoutRounding(16),
                    row.GetDoubleFieldWithoutRounding(18));
            generation.Epoch.EndYear = row.GetField<int>(1);
            generation.Epoch.EnvironmentCapacity = (int)row.GetDoubleFieldWithoutRounding(2);
            generation.Epoch.EnableFitness = row.GetField<bool>(3);
            generation.Epoch.BreedingProbability(row.GetDoubleFieldWithoutRounding(4));

            generation.Epoch.Fitness(row.GetDoubleFieldWithoutRounding(6));
            generation.Epoch.ExpectedMaxPopulation = row.GetField<int>(7);
            generation.AverageFitness = row.GetDoubleFieldWithoutRounding(13);
            generation.FitnessDeviation = row.GetDoubleFieldWithoutRounding(14);
            generation.AverageAge = row.GetDoubleFieldWithoutRounding(15);
            generation.AverageFactoredFitness = row.GetDoubleFieldWithoutRounding(17);
            generation.AverageLifeExpectancy = row.GetDoubleFieldWithoutRounding(19);
            return generation;
        }
    }
}
