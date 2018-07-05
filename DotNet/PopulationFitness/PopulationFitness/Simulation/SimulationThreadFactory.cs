using PopulationFitness.Models;

namespace PopulationFitness.Simulation
{
    public class SimulationThreadFactory : ISimulationFactory
    {
        private readonly Config _config;
        private readonly Epochs _epochs;

        public SimulationThreadFactory(Config config, Epochs epochs, Tuning tuning)
        {
            _config = config;
            _epochs = epochs;
            Tuning = tuning;
        }

        public Tuning Tuning { get; }

        public Simulation CreateNew(int run)
        {
            return new SimulationThread(_config, _epochs, Tuning, run);
        }
    }
}
