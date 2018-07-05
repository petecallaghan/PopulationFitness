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
                Id = config.Id,
                Function = config.Function,
                HistoricFit = config.HistoricFit,
                DiseaseFit = config.DiseaseFit,
                ModernFit = config.ModernFit,
                ModernBreeding = config.ModernBreeding.ToString(),
                NumberOfGenes = config.NumberOfGenes,
                SizeOfGenes = config.SizeOfGenes,
                MutationsPerGene = config.MutationsPerGene.ToString(),
                SeriesRuns = config.SeriesRuns,
                ParallelRuns = config.ParallelRuns
            });
        }
    }
}
