package uk.edu.populationfitness.models.genes.cache;

import uk.edu.populationfitness.models.genes.GenesIdentifier;

import java.util.Collection;

public class ThreadLocalGenesCache implements GeneValues {
    private final ThreadLocal<DiskBackedGeneValues> cache;

    private String storeNameForCurrentThread(){
        return DiskBackedGeneValues.getStoreNameForId(Thread.currentThread().getId());
    }

    public ThreadLocalGenesCache(int numberOfThreadsExpected) {
        cleanUp();

        this.cache = ThreadLocal.withInitial(() -> new DiskBackedGeneValues(storeNameForCurrentThread(),
                        DiskBackedGeneValues.getPortionSizeOfAvailableMemory(numberOfThreadsExpected)));
    }

    @Override
    public GenesIdentifier add(long[] genesIntegers) {
        return cache.get().add(genesIntegers);
    }

    @Override
    public long[] get(GenesIdentifier identifier) {
        return cache.get().get(identifier);
    }

    @Override
    public void retainOnly(Collection<GenesIdentifier> genesIdentifiers) {
        cache.get().retainOnly(genesIdentifiers);
    }

    @Override
    public void close() {
        cache.get().close();
        cache.remove();
    }

    /**
     * Call this to clean up any files
     */
    public static void cleanUp(){
        DiskBackedGeneValues.cleanUp();
    }
}
