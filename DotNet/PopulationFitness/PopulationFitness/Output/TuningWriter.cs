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
            Write(tuning, path + "/" + tuning.function.ToString() + "-" + tuning.number_of_genes + "-" + tuning.size_of_genes + ".csv");
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
                Function = tuning.function, 
                HistoricFit = tuning.historic_fit,
                DiseaseFit = tuning.disease_fit,
                ModernFit = tuning.modern_fit,
                ModernBreeding = tuning.modern_breeding,
                SizeOfGenes = tuning.size_of_genes,
                NumberOfGenes = tuning.number_of_genes,
                Mutations = tuning.mutations_per_gene,
                SeriesRuns = tuning.series_runs,
                ParallelRuns =tuning.parallel_runs,
            };
        }
    }
}