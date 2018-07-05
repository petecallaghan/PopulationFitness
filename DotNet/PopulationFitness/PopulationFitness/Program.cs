using PopulationFitness.Models;
using PopulationFitness.Simulation;

namespace PopulationFitness
{
    class Program
    {
        private const int DiseaseYears = 3;
        private const int PostDiseaseYears = 30;

        static void Main(string[] args)
        {
            Config config = new Config();
            Epochs epochs = new Epochs();
            Tuning tuning = new Tuning
            {
                Id = config.Id
            };

            Commands.ConfigureTuningAndEpochsFromInputFiles(config, tuning, epochs, args);
            Simulations.SetInitialPopulationFromFirstEpochCapacity(config, epochs);
            Simulations.AddSimulatedEpochsToEndOfTunedEpochs(config, epochs, tuning, DiseaseYears, PostDiseaseYears);
            Simulations.RunAllInParallel(new SimulationThreadFactory(config, epochs, tuning), Commands.CacheType);
        }
    }
}
