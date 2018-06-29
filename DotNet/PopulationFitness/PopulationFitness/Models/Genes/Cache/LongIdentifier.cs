namespace PopulationFitness.Models.Genes.Cache
{

    /**
     * Identifies a bitset with a uniformly increasing long value.
     */
    public class LongIdentifier : IGenesIdentifier
    {
        private static long globalIdentifier = 0;

        private readonly long value;

        public LongIdentifier()
        {
            value = ++globalIdentifier;
        }

        public long AsUniqueLong()
        {
            return value;
        }
    }
}
