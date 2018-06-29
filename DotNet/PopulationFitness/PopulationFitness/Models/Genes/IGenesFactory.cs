namespace PopulationFitness.Models.Genes
{
    /**
    * Implement this to create new genes
    *
    * Created by pete.callaghan on 13/07/2017.
    */
    public interface IGenesFactory
    {
        /**
            * Builds a set of genes using the fitness function specified
            * @param config
            * @return the built genes
            */
        IGenes Build(Config config);

        /**
            * Defines the fitness function to use when building genes
            *
            * @param function
            */
        void UseFitnessFunction(Function function);

        /***
            *
            * @return the function used when building genes
            */
        Function GetFitnessFunction();
    }
}
