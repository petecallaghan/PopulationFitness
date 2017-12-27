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

public class DiskBackedGeneValues implements GeneValues {
    private static final String IndexName = "genes";

    private static final String StoreName = "~genescache"+ManagementFactory.getRuntimeMXBean().getName()+".tmp";

    private final String storeName;

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

    private void ensureBackingFileDoesNotExist() {
        File file = new File(storeName);
        file.delete();
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
    public static long getAvailableMemorySize(){
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

    public DiskBackedGeneValues(){
        this(StoreName);
    }

    public DiskBackedGeneValues(String storeName){
        this(storeName, getAvailableMemorySize());
    }

    public DiskBackedGeneValues(String storeName, long maxMemorySize)
    {
        this.storeName = storeName;

        ensureBackingFileDoesNotExist();

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
        diskIndex.clear();
        memoryIndex.clear();
        diskIndex.close();
        memoryIndex.close();
        memoryStore.close();
        diskStore.close();
        ensureBackingFileDoesNotExist();
    }
}
