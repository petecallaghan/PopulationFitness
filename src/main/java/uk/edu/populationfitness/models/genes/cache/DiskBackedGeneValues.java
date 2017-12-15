package uk.edu.populationfitness.models.genes.cache;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import uk.edu.populationfitness.models.genes.GenesIdentifier;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public class DiskBackedGeneValues implements GeneValues {
    private static final String IndexName = "genes";

    private static final String StoreName = "~genescache.tmp";

    private final DB diskStore;

    private final DB memoryStore;

    private final HTreeMap<Long, long[]> diskIndex;

    private final HTreeMap<Long, long[]> memoryIndex;

    @NotNull
    private static DB createDiskStore(){
        return DBMaker.fileDB(StoreName)
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
        File file = new File(StoreName);
        file.delete();
    }

    public DiskBackedGeneValues(){
        // Default to using 3/4 available memory
        this((Runtime.getRuntime().maxMemory() * 3) / 4);
    }

    public DiskBackedGeneValues(long maxMemorySize)
    {
        ensureBackingFileDoesNotExist();

        diskStore = createDiskStore();

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
        diskIndex.close();
        memoryIndex.close();
        memoryStore.close();
        diskStore.close();
        ensureBackingFileDoesNotExist();
    }
}
