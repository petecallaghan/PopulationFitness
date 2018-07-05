namespace PopulationFitness.Models.Genes.Cache
{

    /**
     * Identifies a bitset with a uniformly increasing long value.
     */
    public class LongIdentifier : IGenesIdentifier
    {
        private static long _globalIdentifier = 0;

        private readonly long _value;

        public LongIdentifier()
        {
            _value = ++_globalIdentifier;
        }

        public long AsUniqueLong()
        {
            return _value;
        }
    }
}
