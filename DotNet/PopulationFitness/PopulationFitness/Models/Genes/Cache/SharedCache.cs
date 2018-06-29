namespace PopulationFitness.Models.Genes.Cache
{
    /**
     * Single cache instance shared throughout the process
     */
    public class SharedCache
    {
        private static IGeneValues GenesCache = new OnHeapGeneValues();

        public static IGeneValues Cache
        {
            get
            {
                return GenesCache;
            }
            set
            {
                GenesCache = value;
            }
        }

        /**
         * Uses a cache that is fast and suitable for small numbers and/or sizes of genes.
         */
        public static void SetDefault()
        {
            GenesCache = new OnHeapGeneValues();
        }
    }
}
