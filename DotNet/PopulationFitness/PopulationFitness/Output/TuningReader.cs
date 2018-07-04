using CsvHelper;
using PopulationFitness.Models.Genes;
using System;
using System.IO;

namespace PopulationFitness.Output
{
    public class TuningReader
    {
        public static void Read(Tuning tuning, String file)
        {
            var reader = new CsvReader(File.OpenText(file));

            reader.Read();
            reader.ReadHeader();

            if (reader.Read())
            {
                ReadFromRow(tuning, reader);
            }

        }

        private static void ReadFromRow(Tuning tuning, CsvReader row)
        {
            tuning.function = row.GetField<Function>(0);
            tuning.historic_fit = row.GetField<double>(1);
            tuning.disease_fit = row.GetField<double>(2);
            tuning.modern_fit = row.GetField<double>(3);
            tuning.modern_breeding = row.GetField<double>(4);
            tuning.size_of_genes = row.GetField<int>(5);
            tuning.number_of_genes = row.GetField<int>(6);
            tuning.mutations_per_gene = row.GetField<double>(7);
            tuning.series_runs = row.GetField<int>(8);
            tuning.parallel_runs = row.GetField<int>(9);
        }
    }
}
