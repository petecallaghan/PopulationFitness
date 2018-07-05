using PopulationFitness.Models.Genes.Cache;
using System;

namespace PopulationFitness.Models.Genes.BitSet
{

    /**
     * Extend this to provide different fitness functions that use a bitset
     *
     * Created by pete.callaghan on 03/07/2017.
     */
    public abstract class BitSetGenes : IGenes
    {
        protected readonly Config Config;

        private IGenesIdentifier _genesIdentifier;

        private double _storedFitness = 0;

        private bool _fitnessStored = false;

        private readonly int _sizeOfGenes;

        protected BitSetGenes(Config config)
        {
            Config = config;
            _sizeOfGenes = config.GeneBitCount;
        }

        private long[] GetGenesFromCache()
        {
            return SharedCache.Cache.Get(_genesIdentifier);
        }

        private BitSet GetBitSetGenesFromCache()
        {
            return BitSet.ValueOf(SharedCache.Cache.Get(_genesIdentifier));
        }

        private void StoreGenesInCache(BitSet genes)
        {
            StoreGenesInCache(genes.ToLongArray());
        }

        private void StoreGenesInCache(long[] genes)
        {
            _genesIdentifier = SharedCache.Cache.Add(genes);
            _fitnessStored = false;
        }

        public void BuildEmpty()
        {
            int numberOfInts = (int)((_sizeOfGenes / Long.Size) + (Long.Size % _sizeOfGenes == 0 ? 0 : 1));
            long[] genes = new long[numberOfInts];
            Array.Clear(genes, 0, genes.Length);
            StoreGenesInCache(genes);
        }

        public void BuildFull()
        {
            BitSet genes = new BitSet(_sizeOfGenes);
            genes.Set(0, _sizeOfGenes - 1);
            StoreGenesInCache(genes);
        }

        public void BuildFromRandom()
        {
            BuildEmpty();
            long[] genes = AsIntegers;
            MutateGenesWithMutationInterval(genes, 0);
            StoreGenesInCache(genes);
        }

        public long[] AsIntegers
        {
            get
            {
                return SharedCache.Cache.Get(_genesIdentifier);
            }
        }

        public int GetCode(int index)
        {
            return GetBitSetGenesFromCache().Get(index) ? 1 : 0;
        }

        public int NumberOfBits
        {
            get
            {
                return _sizeOfGenes;
            }
        }

        public int Mutate()
        {
            return MutateAndStore(AsIntegers);
        }

        private int MutateAndStore(long[] genes)
        {
            int mutatedCount = MutateGenes(genes);
            StoreGenesInCache(genes);
            return mutatedCount;
        }

        private int MutateGenes(long[] genes)
        {
            if (Config.MutationsPerGene <= 0)
            {
                return 0;
            }
            long mutation_genes_interval = 1 + (long)(genes.Length * 2.0 / Config.MutationsPerGene);
            return MutateGenesWithMutationInterval(genes, mutation_genes_interval);
        }

        private int MutateGenesWithMutationInterval(long[] genes, long mutation_genes_interval)
        {
            long max = Config.MaxGeneValue;
            long lastMax = Config.LastMaxGeneValue;
            int last = genes.Length - 1;
            int mutatedCount = 0;
            for (int i = RepeatableRandom.GenerateNextInt(mutation_genes_interval);
                 i < genes.Length;
                 i += Math.Max(1, (int)RepeatableRandom.GenerateNextLong(0, mutation_genes_interval)))
            {
                genes[i] = GetMutatedValue(genes[i], i == last ? lastMax : max);
                mutatedCount++;
            }
            return mutatedCount;
        }

        private long GetMutatedValue(long gene, long max)
        {
            return gene + RepeatableRandom.GenerateNextLong(0 - gene, max);
        }

        /**
         * Call this to store a fitness
         *
         * @param fitness
         * @return
         */
        protected double StoreFitness(double fitness)
        {
            _storedFitness = fitness;
            _fitnessStored = true;
            return _storedFitness;
        }

        /**
         * @return the stored fitness
         */
        protected double StoredFitness()
        {
            return _storedFitness;
        }

        protected bool IsFitnessStored()
        {
            return _fitnessStored;
        }

        public int InheritFrom(IGenes mother, IGenes father)
        {
            long[] motherEncoding = ((BitSetGenes)mother.Implementation).GetGenesFromCache();
            long[] fatherEncoding = ((BitSetGenes)father.Implementation).GetGenesFromCache();
            long[] babyEncoding = new long[Math.Max(motherEncoding.Length, fatherEncoding.Length)];

            // Randomly picks the code index that crosses over from mother to father
            int cross_over_word = Math.Min(1 + RepeatableRandom.GenerateNextInt(babyEncoding.Length - 1), babyEncoding.Length - 1);
            int mother_length = Math.Min(cross_over_word + 1, motherEncoding.Length);
            int father_length = Math.Min(babyEncoding.Length - cross_over_word - 1, fatherEncoding.Length - cross_over_word - 1);

            Array.Copy(motherEncoding, 0, babyEncoding, 0, mother_length);
            if (father_length > 0)
            {
                Array.Copy(fatherEncoding, cross_over_word + 1, babyEncoding, cross_over_word + 1, father_length);
            }

            return MutateAndStore(babyEncoding);
        }

        public bool AreEmpty
        {
            get
            {
                return GetBitSetGenesFromCache().IsEmpty;
            }
        }

        public bool IsEqual(IGenes other)
        {
            return GetBitSetGenesFromCache().Equals(((BitSetGenes)other).GetBitSetGenesFromCache());
        }

        public IGenes Implementation
        {
            get
            {
                return this;
            }
        }

        public IGenesIdentifier Identifier
        {
            get
            {
                return _genesIdentifier;
            }
        }

        public abstract double Fitness { get; }
    }
}
