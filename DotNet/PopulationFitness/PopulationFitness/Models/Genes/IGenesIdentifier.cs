namespace PopulationFitness.Models.Genes
{

    /**
     * Identifies a set of genes
     */
    public interface IGenesIdentifier
    {
        /**
         *
         * @return a long value that uniquely identifies the genes.
         */
        long AsUniqueLong();
    }
}
