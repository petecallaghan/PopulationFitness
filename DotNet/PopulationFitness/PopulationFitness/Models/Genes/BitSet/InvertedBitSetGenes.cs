namespace PopulationFitness.Models.Genes.BitSet
{
    public abstract class InvertedBitSetGenes : BitSetGenes
    {
        protected InvertedBitSetGenes(Config config) : base(config)
        {
        }

        /**
         * Call this to store the fitness, inverted to 1- fitness
         *
         * @param fitness
         * @return the scaled stored fitness
         */
        protected double StoreInvertedFitness(double fitness)
        {
            return StoreFitness(1 - fitness);
        }
    }
}
