namespace PopulationFitness.Models.Genes.Performance
{
    public class GenesTimerFactory : IGenesFactory
    {
        private readonly IGenesFactory _factory;

        public GenesTimerFactory(IGenesFactory factory) => this._factory = factory;

        public IGenes Build(Config config) => new GenesTimer(_factory.Build(config));

        public Function FitnessFunction { get { return _factory.FitnessFunction; } set { _factory.FitnessFunction = value; } }
    }
}
