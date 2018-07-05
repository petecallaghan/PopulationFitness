using PopulationFitness.Models;
using PopulationFitness.Models.Genes.Cache;
using PopulationFitness.Output;
using System;
using System.Collections.Generic;

namespace PopulationFitness
{
    public class Commands
    {
        public const string Seed = "-s";
        public const string TuningFile = "-t";
        public const string EpochsFile = "-e";
        public const string Id = "-i";
        private const string CommandLine = "-c";
        private const string ProcessCount = "-p";
        public const string RandomSeed = "random";
        private const string GenesCache = "-g";

        /**
         * Defines the path of the tuning file read from arguments.
         */
        public static string TuningFilePath = "";

        /**
         * Defines the path of the epochs file read from arguments.
         */
        public static string EpochsFilePath = "";

        /**
         * Defines the command line for any child processes
         */
        public static string ChildCommandLine = "";

        /**
         * Defines the cache type for genes
         */
        public static CacheType CacheType = CacheType.Default;

        /**
         * Reads tuning and epochs from the process arguments
         *
         * @param config
         * @param tuning
         * @param epochs
         * @param args
         */
        public static void ConfigureTuningAndEpochsFromInputFiles(Config config, Tuning tuning, Epochs epochs, string[] args)
        {
            int parallelCount = 1;

            if (args.Length < 2)
            {
                ShowHelp();
            }

            if (args.Length % 2 == 1)
            {
                ShowHelp();
            }

            for (int i = 0; i < args.Length - 1; i += 2)
            {
                string argument = args[i].ToLower();
                string value = args[i + 1];
                try
                {
                    if (argument.StartsWith(Seed))
                    {
                        long seed = getSeed(value);
                        RepeatableRandom.SetSeed(seed);
                        continue;
                    }
                    if (argument.StartsWith(TuningFile))
                    {
                        TuningFilePath = value;
                        TuningReader.Read(tuning, TuningFilePath);
                        config.SizeOfEachGene = tuning.SizeOfGenes;
                        config.NumberOfGenes = tuning.NumberOfGenes;
                        config.GenesFactory.FitnessFunction =tuning.Function;
                        config.MutationsPerGene = tuning.MutationsPerGene;
                        parallelCount = tuning.ParallelRuns;
                        continue;
                    }
                    if (argument.StartsWith(EpochsFile))
                    {
                        EpochsFilePath = value;
                        List<Epoch> read = EpochsReader.ReadEpochs(config, EpochsFilePath);
                        epochs.AddAll(read);
                        continue;
                    }
                    if (argument.StartsWith(Id))
                    {
                        config.Id = tuning.Id = value;
                        continue;
                    }
                    if (argument.StartsWith(CommandLine))
                    {
                        ChildCommandLine = value;
                        continue;
                    }
                    if (argument.StartsWith(ProcessCount))
                    {
                        parallelCount = Int32.Parse(value);
                        continue;
                    }
                    if (argument.StartsWith(GenesCache))
                    {
                        CacheType = (CacheType)Enum.Parse(typeof(CacheType), value);
                        continue;
                    }
                }
                catch (Exception e)
                {
                    Console.Write(e);
                }
                ShowHelp();
            }
            tuning.ParallelRuns = parallelCount;
        }

        private static long getSeed(string arg)
        {
            // Will set the seed from the current time if 'random' is chosen
            if (arg.ToLower().StartsWith(RandomSeed))
            {
                return DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
            }
            return Int64.Parse(arg);
        }

        private static void ShowHelp()
        {
            Console.WriteLine("Commands:");
            Console.WriteLine("    -s [seed number]| [random to set a seed from the current time]");
            Console.WriteLine("    -t [csv file containing tuning]");
            Console.WriteLine("    -e [csv file containing epochs]");
            Console.WriteLine("    -i [simulation id - used to generate the name of the output files");
            Console.WriteLine("    -c [command line for child processes - eg '-Xms10g -Xmx10g -jar target/populationfitness.jar']");
            Console.WriteLine("    -p [process count of child processes, defaults to value in tuning file]");
            Console.WriteLine("    -g [DiskBacked or Heap - default is Heap. Use DiskBacked to use a file store for genes]");
            Environment.Exit(0);
        }
    }
}
