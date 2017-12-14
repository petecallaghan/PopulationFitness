package uk.edu.populationfitness.models.genes.cache;

/**
 * Single cache instance shared throughout the process
 */
public class SharedCache {
    private static GeneValues GenesCache = new OnHeapGeneValues();

    /**
     * Uses the cache
     *
     * @param cache
     */
    public static void set(GeneValues cache){
        GenesCache = cache;
    }

    /**
     * Uses a cache that is fast and suitable for small numbers and/or sizes of genes.
     */
    public static void setDefault(){
        GenesCache = new OnHeapGeneValues();
    }

    /**
     * @return the cache in set
     */
    public static GeneValues cache(){
        return GenesCache;
    }
}
