namespace PopulationFitness.Models.Genes.Cache
{
    /**
     * Single cache instance shared throughout the process
     */
    public class SharedCache
    {

        public static IGeneValues Cache { get; set; } = new OnHeapGeneValues();

        /**
         * Uses a cache that is fast and suitable for small numbers and/or sizes of genes.
         */
        public static void SetDefault()
        {
            Cache = new OnHeapGeneValues();
        }
    }
}
