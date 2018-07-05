using CsvHelper;
using PopulationFitness.Models;
using System;
using System.Collections.Generic;
using System.IO;

namespace PopulationFitness.Output
{
    public class EpochsReader
    {
        public static List<Epoch> ReadEpochs(Config config, String path)
        {
            List<Epoch> epochs = new List<Epoch>();

            var reader = new CsvReader(File.OpenText(path));

            reader.Read();
            reader.ReadHeader();

            while (reader.Read())
            {
                Epoch epoch = ReadFromRow(config, reader);
                epochs.Add(epoch);
            }

            return epochs;
        }

        private static Epoch ReadFromRow(Config config, CsvReader row)
        {
            Epoch epoch = new Epoch(config, row.GetField<int>(0))
            {
                EndYear = row.GetField<int>(1),
                EnvironmentCapacity = row.GetField<int>(2)
            };
            epoch.BreedingProbability(row.GetField<double>(3));
            epoch.Disease(row.GetField<bool>(4));
            epoch.Fitness(row.GetField<double>(5));
            epoch.ExpectedMaxPopulation = row.GetField<int>(6);
            epoch.MaxAge(row.GetField<int>(7));
            epoch.MaxBreedingAge(row.GetField<int>(8));
            return epoch;
        }
    }
}
