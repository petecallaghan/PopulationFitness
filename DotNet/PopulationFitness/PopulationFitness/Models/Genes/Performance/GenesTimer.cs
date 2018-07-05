using System.Diagnostics;

namespace PopulationFitness.Models.Genes.Performance
{
    public class GenesTimer : IGenes
    {
        private static readonly double MicrosecPerTick = (1000.0 * 1000.0) / Stopwatch.Frequency;
        private static readonly TimingStatistics _buildRandom = new TimingStatistics("Build random genes");
        private static readonly TimingStatistics _inherit = new TimingStatistics("Inherit from parents");
        private static readonly TimingStatistics _fitness = new TimingStatistics("Calculate fitness");

        public static void ResetAll()
        {
            _buildRandom.Reset();
            _inherit.Reset();
            _fitness.Reset();
        }

        public static void ShowAll()
        {
            _buildRandom.Show();
            _inherit.Show();
            _fitness.Show();
        }

        public GenesTimer(IGenes genes)
        {
            Implementation = genes;
        }

        public void BuildEmpty()
        {
            Implementation.BuildEmpty();
        }

        public void BuildFull()
        {
            Implementation.BuildFull();
        }

        public int GetCode(int index)
        {
            return Implementation.GetCode(index);
        }

        /**
         * @return the elapsed time in micro seconds
         */
        public static long GetElapsed(Stopwatch stopWatch)
        {
            stopWatch.Stop();
            return (long)(stopWatch.ElapsedTicks * MicrosecPerTick);
        }

        public void BuildFromRandom()
        {
            Stopwatch stopWatch = Stopwatch.StartNew();
            Implementation.BuildFromRandom();
            _buildRandom.Add(GetElapsed(stopWatch));
        }

        public int NumberOfBits
        {
            get
            {
                return Implementation.NumberOfBits;
            }
        }

        public int Mutate()
        {
            return Implementation.Mutate();
        }

        public int InheritFrom(IGenes mother, IGenes father)
        {
            Stopwatch stopWatch = Stopwatch.StartNew();
            int mutatedCount = Implementation.InheritFrom(mother, father);
            _inherit.Add(GetElapsed(stopWatch));
            return mutatedCount;
        }

        public bool AreEmpty
        {
            get
            {
                return Implementation.AreEmpty;
            }
        }

        public double Fitness
        {
            get
            {
                Stopwatch stopWatch = Stopwatch.StartNew();
                double fitness = Implementation.Fitness;
                _fitness.Add(GetElapsed(stopWatch));
                return fitness;
            }
        }

        public bool IsEqual(IGenes other)
        {
            return Implementation.IsEqual(other);
        }

        public IGenes Implementation { get; }

        public long[] AsIntegers
        {
            get
            {
                return Implementation.AsIntegers;
            }
        }

        public IGenesIdentifier Identifier
        {
            get
            {
                return Implementation.Identifier;
            }
        }
    }
}
