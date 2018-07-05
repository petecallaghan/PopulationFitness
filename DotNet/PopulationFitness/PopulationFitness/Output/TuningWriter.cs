using CsvHelper;
using System;
using System.Collections.Generic;
using System.IO;

namespace PopulationFitness.Output
{
    public class TuningWriter
    {
        public static void WriteInPath(String path, Tuning tuning)
        {
            Write(tuning, path + "/" + tuning.Function.ToString() + "-" + tuning.NumberOfGenes + "-" + tuning.SizeOfGenes + ".csv");
        }

        public static void Write(Tuning tuning, String filePath)
        {
            EpochsWriter.DeleteExisting(filePath);

            using (var file = new StreamWriter(filePath))
            {
                var records = new List<object>
                {
                    CreateRecord(tuning)
                };
                var writer = new CsvWriter(file);
                writer.WriteRecords(records);
            }
        }

        private static object CreateRecord(Tuning tuning)
        {
            return new
            {
                Function = tuning.Function, 
                HistoricFit = tuning.HistoricFit,
                DiseaseFit = tuning.DiseaseFit,
                ModernFit = tuning.ModernFit,
                ModernBreeding = tuning.ModernBreeding,
                SizeOfGenes = tuning.SizeOfGenes,
                NumberOfGenes = tuning.NumberOfGenes,
                Mutations = tuning.MutationsPerGene,
                SeriesRuns = tuning.SeriesRuns,
                ParallelRuns =tuning.ParallelRuns,
            };
        }
    }
}