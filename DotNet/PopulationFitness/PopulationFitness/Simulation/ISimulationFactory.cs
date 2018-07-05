namespace PopulationFitness.Simulation
{
    /**
     * Implement this to create new simulations
     */
    public interface ISimulationFactory
    {
        Tuning Tuning
        {
            get;
        }

        Simulation CreateNew(int run);
    }
}
