namespace PopulationFitness.Models.Genes.Performance
{
    public class GenesTimerFactory : IGenesFactory
    {
        private readonly IGenesFactory factory;

        public GenesTimerFactory(IGenesFactory factory) => this.factory = factory;

        public IGenes Build(Config config) => new GenesTimer(factory.Build(config));

        public void UseFitnessFunction(Function function) => factory.UseFitnessFunction(function);

        public Function GetFitnessFunction() => factory.GetFitnessFunction();
    }
}
