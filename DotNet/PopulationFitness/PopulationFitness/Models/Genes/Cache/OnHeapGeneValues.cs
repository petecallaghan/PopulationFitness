using System.Collections.Generic;

namespace PopulationFitness.Models.Genes.Cache
{

    /**
     * Emulates a GeneValues by creating a self identified bitset, which retains a reference to the bitset, allowing
     * the bitset to be retrieved given the identifier. This means that all bitsets created by the cache will live
     * on the heap
     */
    public class OnHeapGeneValues : IGeneValues
    {
        public IGenesIdentifier Add(long[] genesIntegers)
        {
            return new SelfIdentifiedGenes(genesIntegers);
        }

        public long[] Get(IGenesIdentifier identifier)
        {
            return ((SelfIdentifiedGenes)identifier).integerGenes;
        }

        public void RetainOnly(ICollection<IGenesIdentifier> genesIdentifiers)
        {

        }

        public void Close()
        {

        }

        public bool IsFlushable
        {
            get
            {
                return false;
            }
        }
    }
}
