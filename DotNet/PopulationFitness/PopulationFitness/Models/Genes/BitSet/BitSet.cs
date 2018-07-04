using System;

namespace PopulationFitness.Models.Genes.BitSet
{
    public static class Long
    {
        public static readonly int Size = System.Runtime.InteropServices.Marshal.SizeOf(typeof(long)) * 8;
    }

    class BitSet
    {
        private readonly long[] bits;

        private BitSet(long[] values)
        {
            bits = values;
        }

        public BitSet(int size_of_genes)
        {
            var numberOfLongs = size_of_genes / Long.Size;
            if (size_of_genes % Long.Size > 1)
            {
                numberOfLongs++;
            }
            bits = new long[numberOfLongs];
            Array.Clear(bits, 0, bits.Length);
        }

        internal bool IsEmpty()
        {
            foreach (long i in bits)
            {
                if (i != 0)
                {
                    return false;
                }
            }
            return true;
        }

        internal bool Get(int index)
        {
            int longIndex = index / Long.Size;
            int bitIndex = index % Long.Size;
            long mask = 1u << bitIndex;
            return ((mask & bits[longIndex]) == mask);
        }

        internal void Clear()
        {
            Array.Clear(bits, 0, bits.Length);
        }

        internal void Set(int index, int value)
        {
            int longIndex = index / Long.Size;
            int bitIndex = index % Long.Size;
            long mask = 1u << bitIndex;
            if (value == 0)
            {
                bits[longIndex] &= ~mask;
            }
            else
            {
                bits[longIndex] |= mask;
            }
        }

        internal static BitSet ValueOf(long[] values)
        {
            return new BitSet(values);
        }

        internal long[] ToLongArray()
        {
            return bits;
        }

        public override bool Equals(object obj)
        {
            if (obj is BitSet other)
            {
                if (other.bits.Length != bits.Length)
                {
                    return false;
                }
                for (int i = 0; i < bits.Length; i++)
                {
                    if (other.bits[i] != bits[i])
                    {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }

        public override string ToString()
        {
            return base.ToString();
        }
    }
}
