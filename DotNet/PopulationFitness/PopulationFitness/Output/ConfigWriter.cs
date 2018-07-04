using System;
using System.IO;
using YamlDotNet.Serialization;

namespace PopulationFitness.Output
{
    public static class ConfigWriter
    {
        /**
         * Writes a YAML encoding of the config to the file
         *
         * @param config
         * @param filename
         * @throws IOException
         */
        public static void Write(Tuning config, String filename) 
        {
            var serialized = ToString(config);
            File.WriteAllText(filename, serialized);
        }

        /**
         * Generates a string YAML encoding of the configuration
         *
         * @param config
         * @return
         */
        public static string ToString(Tuning config)
        {
            var serializer = new Serializer();
            return serializer.Serialize(new
            {
                Id = config.id,
                Function = config.function,
                HistoricFit = config.historic_fit,
                DiseaseFit = config.disease_fit,
                ModernFit = config.modern_fit,
                ModernBreeding = config.modern_breeding.ToString(),
                NumberOfGenes = config.number_of_genes,
                SizeOfGenes = config.size_of_genes,
                MutationsPerGene = config.mutations_per_gene.ToString(),
                SeriesRuns = config.series_runs,
                ParallelRuns = config.parallel_runs
            });
        }
    }
}
