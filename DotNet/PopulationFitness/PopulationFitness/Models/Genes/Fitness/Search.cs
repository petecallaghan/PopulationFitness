using System;

namespace PopulationFitness.Models.Genes.Fitness
{
    public class Search : FitnessRange
    {
        private double _increment;

        private bool _isCurrentSet = false;

        private double _current;

        public Search Increment(double increment)
        {
            this._increment = increment;
            return this;
        }

        protected double Increment()
        {
            return _increment;
        }

        private double Centre
        {
            get
            {
                return ((long)((Min() + Max()) / _increment) / 2) * _increment;
            }
        }

        public double Current
        {
            get
            {
                if (!_isCurrentSet)
                {
                    _current = Centre;
                }
                return _current;
            }

            set
            {
                _isCurrentSet = true;
                _current = value;
            }
        }

        public virtual Search FindNext(PopulationComparison comparison)
        {
            Search next = new Search();
            next.Increment(_increment).Max(Max()).Min(Min());
            switch (comparison)
            {
                case PopulationComparison.TooLow:
                    next.Min(Current);
                    break;
                case PopulationComparison.TooHigh:
                    next.Max(Current);
                    break;
                default:
                    return null;
            }
            double current = next.Current;
            if (Math.Abs(next.Max() - current) < _increment) return null;
            if (Math.Abs(next.Min() - current) < _increment) return null;
            return next;
        }
    }
}
