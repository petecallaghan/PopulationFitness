using System.Diagnostics;

namespace PopulationFitness.Models.Genes.Performance
{
    public class GenesTimer : IGenes
    {
        private static readonly long MicrosecPerTick = (1000L * 1000L) / Stopwatch.Frequency;

        private readonly IGenes genes;

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
            this.genes = genes;
        }

        public void BuildEmpty()
        {
            genes.BuildEmpty();
        }

        public void BuildFull()
        {
            genes.BuildFull();
        }

        public int GetCode(int index)
        {
            return genes.GetCode(index);
        }

        /**
         * @return the elapsed time in micro seconds
         */
        public static long GetElapsed(Stopwatch stopWatch)
        {
            stopWatch.Stop();
            return stopWatch.ElapsedTicks * MicrosecPerTick;
        }

        public void BuildFromRandom()
        {
            Stopwatch stopWatch = Stopwatch.StartNew();
            genes.BuildFromRandom();
            _buildRandom.Add(GetElapsed(stopWatch));
        }

        public int NumberOfBits()
        {
            return genes.NumberOfBits();
        }

        public int Mutate()
        {
            return genes.Mutate();
        }

        public int InheritFrom(IGenes mother, IGenes father)
        {
            Stopwatch stopWatch = Stopwatch.StartNew();
            int mutatedCount = genes.InheritFrom(mother, father);
            _inherit.Add(GetElapsed(stopWatch));
            return mutatedCount;
        }

        public bool AreEmpty()
        {
            return genes.AreEmpty();
        }

        public double Fitness()
        {
            Stopwatch stopWatch = Stopwatch.StartNew();
            double fitness = genes.Fitness();
            _fitness.Add(GetElapsed(stopWatch));
            return fitness;
        }

        public bool IsEqual(IGenes other)
        {
            return genes.IsEqual(other);
        }

        public IGenes GetImplementation()
        {
            return genes;
        }

        public long[] AsIntegers()
        {
            return genes.AsIntegers();
        }

        public IGenesIdentifier Identifier()
        {
            return genes.Identifier();
        }
    }
}
