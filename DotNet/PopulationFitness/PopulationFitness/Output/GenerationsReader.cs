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
            generation.epoch.end_year = row.GetField<int>(1);
            generation.epoch.environment_capacity = (int)row.GetField<double>(2);
            generation.epoch.enable_fitness = row.GetField<bool>(3);
            generation.epoch.BreedingProbability(row.GetField<double>(4));

            generation.epoch.Fitness(row.GetField<double>(6));
            generation.epoch.expected_max_population = row.GetField<int>(7);
            generation.average_fitness = row.GetField<double>(13);
            generation.fitness_deviation = row.GetField<double>(14);
            generation.average_age = row.GetField<double>(15);
            generation.average_factored_fitness = row.GetField<double>(17);
            generation.average_life_expectancy = row.GetField<double>(19);
            return generation;
        }
    }
}
