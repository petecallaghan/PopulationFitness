using System;

namespace PopulationFitness.Models.Genes.Cache
{

    /**
     * A bit set identifier that directly references the bitset.
     */
    public class SelfIdentifiedGenes : IGenesIdentifier
    {
        public readonly long[] integerGenes;

        public SelfIdentifiedGenes(long[] integerGenes)
        {
            this.integerGenes = integerGenes;
        }

        public long AsUniqueLong()
        {
            throw new Exception("Cannot convert to long");
        }
    }
}
