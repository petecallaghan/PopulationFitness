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
            tuning.Function = row.GetField<Function>(0);
            tuning.HistoricFit = row.GetDoubleFieldWithoutRounding(1);
            tuning.DiseaseFit = row.GetDoubleFieldWithoutRounding(2);
            tuning.ModernFit = row.GetDoubleFieldWithoutRounding(3);
            tuning.ModernBreeding = row.GetDoubleFieldWithoutRounding(4);
            tuning.SizeOfGenes = row.GetField<int>(5);
            tuning.NumberOfGenes = row.GetField<int>(6);
            tuning.MutationsPerGene = row.GetDoubleFieldWithoutRounding(7);
            tuning.SeriesRuns = row.GetField<int>(8);
            tuning.ParallelRuns = row.GetField<int>(9);
        }
    }
}
