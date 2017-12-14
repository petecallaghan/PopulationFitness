package uk.edu.populationfitness.models.genes.cache;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import uk.edu.populationfitness.models.genes.GenesIdentifier;

import java.util.Collection;
import java.util.stream.Collectors;

public class DiskBackedGeneValues implements GeneValues {
    private static final String IndexName = "genes";

    private static final String StoreName = "~genescache.tmp";

    private final DB diskStore;

    private final HTreeMap<Long, long[]> diskIndex;

    private final HTreeMap<Long, long[]> memoryIndex;

    private DB createDiskStore(){
        return DBMaker.fileDB(StoreName)
                .fileMmapEnable()
                .fileMmapPreclearDisable()
                .fileDeleteAfterClose()
                .fileDeleteAfterOpen()
                .closeOnJvmShutdown()
                .make();
    }

    public DiskBackedGeneValues(long maxMemorySize)
    {
        diskStore = createDiskStore();

        diskIndex = createDiskIndex();

        // Force cleanup
        diskIndex.clear();

        memoryIndex = createMemoryIndex(maxMemorySize);
    }

    @NotNull
    private HTreeMap<Long, long[]> createDiskIndex() {
        return diskStore
                .hashMap(IndexName)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .createOrOpen();
    }

    @NotNull
    private HTreeMap<Long, long[]> createMemoryIndex(long maxMemorySize) {
        return createMemoryStore()
                .hashMap(IndexName)
                .keySerializer(Serializer.LONG)
                .valueSerializer(Serializer.LONG_ARRAY)
                .expireAfterCreate()
                .expireStoreSize(maxMemorySize)
                .expireOverflow(diskIndex)
                .create();
    }

    @NotNull
    private DB createMemoryStore() {
        return DBMaker
                .memoryDB()
                .make();
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
    public void remove(GenesIdentifier identifier) {
        memoryIndex.remove(identifier.asUniqueLong());
    }

    @Override
    public void retainOnly(Collection<GenesIdentifier> genesIdentifiers) {
        memoryIndex.keySet().retainAll(genesIdentifiers.stream().map(i -> i.asUniqueLong()).collect(Collectors.toList()));
    }

    public void close(){
        memoryIndex.clear();
        diskIndex.clear();
        diskStore.close();
    }
}
