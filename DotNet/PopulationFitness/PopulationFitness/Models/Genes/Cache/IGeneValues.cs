using System.Collections.Generic;

namespace PopulationFitness.Models.Genes.Cache
{
    public interface IGeneValues
    {
        /**
         * Adds the genesIntegers and returns its identifier.
         *
         * @param genesIntegers
         * @return the genesIntegers identifier
         */
        IGenesIdentifier Add(long[] genesIntegers);

        /***
         * @param identifier an identifier for a previously added set of genes
         * @return the identified genes
         */
        long[] Get(IGenesIdentifier identifier);

        /**
         * Discard all in the cache other than those indicated
         *
         * @param genesIdentifiers
         */
        void RetainOnly(ICollection<IGenesIdentifier> genesIdentifiers);

        /**
         * Called to release any values
         */
        void Close();

        /**
         * @return true if the cache is flushable
         */
        bool IsFlushable { get; }
    }
}
