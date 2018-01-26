package uk.edu.populationfitness.models.genes.cache;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import uk.edu.populationfitness.models.genes.GenesIdentifier;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class DiskBackedGeneValues implements GeneValues {
    private static final String IndexName = "genes";

    private static final String StorePrefix = "~genescache";

    private static final String StoreSuffix = "tmp";

    private static final String StoreName = StorePrefix+ManagementFactory.getRuntimeMXBean().getName()+"."+StoreSuffix;

    private final DB diskStore;

    private final DB memoryStore;

    private final HTreeMap<Long, long[]> diskIndex;

    private final HTreeMap<Long, long[]> memoryIndex;

    @NotNull
    private static DB createDiskStore(String storeName){
        return DBMaker.fileDB(storeName)
                .fileMmapEnable()
                .fileMmapPreclearDisable()
                .make();
    }

    @NotNull
    private static DB createMemoryStore() {
        return DBMaker
                .memoryDB()
                .make();
    }

    @NotNull
    private static HTreeMap<Long, long[]> createDiskIndex(DB diskStore) {
        return diskStore
                .hashMap(IndexName)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .createOrOpen();
    }

    @NotNull
    private static HTreeMap<Long, long[]> createMemoryIndex(DB memoryStore, long maxMemorySize, HTreeMap<Long, long[]> diskIndex) {
        return memoryStore
                .hashMap(IndexName)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .expireAfterCreate() // queue for expiry when added to the index
                .expireStoreSize(maxMemorySize) // overflow when the memory limit is reached
                .expireOverflow(diskIndex) // Uses the disk store as an overflow
                .create();
    }

    /**
     * Call this to clean up any undeleted cache files
     */
    public static void cleanUp(){
        final File folder = new File(".");
        final File[] files = folder.listFiles((dir, name) -> name.matches( StorePrefix+"*.*"));
        for ( final File file : files ) {
            try{
                file.delete();
            }
            catch(Exception ignored){
            }
        }
    }

    /**
     * @param id
     * @return the storename corresponding to the id
     */
    public static String getStoreNameForId(long id){
         return StoreName + "." +Long.toString(id);
    }

    /**
     * @return a value roughly 3/4 of the available memory
     */
    private static long getAvailableMemorySize(){
        // Default to using 3/4 available memory
        return (Runtime.getRuntime().maxMemory() * 3) / 4;
    }

    /**
     * @param numberOfPortions
     * @return the size of available memory for each portion, given the number of portions
     */
    public static long getPortionSizeOfAvailableMemory(int numberOfPortions){
        if (numberOfPortions < 1) return getAvailableMemorySize();

        return getAvailableMemorySize() / numberOfPortions;
    }

    public DiskBackedGeneValues(String storeName, long maxMemorySize)
    {
        diskStore = createDiskStore(storeName);

        memoryStore = createMemoryStore();

        diskIndex = createDiskIndex(diskStore);

        memoryIndex = createMemoryIndex(memoryStore, maxMemorySize, diskIndex);
    }

    @Override
    public GenesIdentifier add(long[] genesIntegers) {
        LongIdentifier identifier = new LongIdentifier();
        memoryIndex.put(identifier.asUniqueLong(), genesIntegers);
        return identifier;
    }

    @Override
    public long[] get(GenesIdentifier identifier) {
        return memoryIndex.get(identifier.asUniqueLong());
    }

    @Override
    public void retainOnly(Collection<GenesIdentifier> genesIdentifiers) {
        Collection<Long> identifiers = genesIdentifiers.stream().map(GenesIdentifier::asUniqueLong).collect(Collectors.toList());
        diskIndex.keySet().retainAll(identifiers); // necessary to prevent inconsistencies with memory index
        memoryIndex.keySet().retainAll(identifiers);
    }

    /**
     * Call this to ensure all resources are released.
     *
     * The cache cannot be recreated in the same process if this is not called
     */
    @Override
    public void close(){
        emptyTheIndexes();
        closeTheIndexes();
        closeTheStores();
    }

    private void closeTheStores() {
        memoryStore.close();
        diskStore.close();
    }

    private void closeTheIndexes() {
        diskIndex.close();
        memoryIndex.close();
    }

    private void emptyTheIndexes() {
        diskIndex.clear();
        memoryIndex.clear();
    }
}
