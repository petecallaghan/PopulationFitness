namespace PopulationFitness.Models.Genes.Fitness
{
    public class FitnessRange
    {

        // Defines the minimum fitness expected from the fitness function
        private double min_fitness;

        // Defines the max fitness expected from the fitness function
        private double max_fitness;

        private double range;

        private Statistics statistics;

        private FitnessRange(Statistics statistics)
        {
            min_fitness = 0;
            max_fitness = 1;
            range = 1;
            this.statistics = statistics;
        }

        public FitnessRange() : this(null)
        {
        }

        public FitnessRange Max(double max)
        {
            max_fitness = max;
            range = max_fitness - min_fitness;
            return this;
        }

        public FitnessRange Min(double min)
        {
            min_fitness = min;
            range = max_fitness - min_fitness;
            return this;
        }

        public double Max()
        {
            return max_fitness;
        }

        public double Min()
        {
            return min_fitness;
        }

        public FitnessRange Statistics(Statistics stats)
        {
            statistics = stats;
            return this;
        }

        public Statistics Statistics()
        {
            return statistics;
        }

        // returns the range as a scale. S(f) = (f - min) / (max - min)
        public double ToScale(double fitness)
        {
            if (statistics != null)
            {
                statistics.Add(fitness);
            }
            return (fitness - min_fitness) / range;
        }
    }
}
