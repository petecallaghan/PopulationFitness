namespace PopulationFitness.Models.Genes.Fitness
{
    public class FitnessRange
    {

        // Defines the minimum fitness expected from the fitness function
        private double _minFitness;

        // Defines the max fitness expected from the fitness function
        private double _maxFitness;

        private double _range;

        private Statistics _statistics;

        private FitnessRange(Statistics statistics)
        {
            _minFitness = 0;
            _maxFitness = 1;
            _range = 1;
            this._statistics = statistics;
        }

        public FitnessRange() : this(null)
        {
        }

        public FitnessRange Max(double max)
        {
            _maxFitness = max;
            _range = _maxFitness - _minFitness;
            return this;
        }

        public FitnessRange Min(double min)
        {
            _minFitness = min;
            _range = _maxFitness - _minFitness;
            return this;
        }

        public double Max()
        {
            return _maxFitness;
        }

        public double Min()
        {
            return _minFitness;
        }

        public FitnessRange Statistics(Statistics stats)
        {
            _statistics = stats;
            return this;
        }

        public Statistics Statistics()
        {
            return _statistics;
        }

        // returns the range as a scale. S(f) = (f - min) / (max - min)
        public double ToScale(double fitness)
        {
            if (_statistics != null)
            {
                _statistics.Add(fitness);
            }
            return (fitness - _minFitness) / _range;
        }
    }
}
