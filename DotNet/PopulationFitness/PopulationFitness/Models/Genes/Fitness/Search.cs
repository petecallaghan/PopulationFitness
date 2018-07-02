using System;

namespace PopulationFitness.Models.Genes.Fitness
{
    public class Search : FitnessRange
    {
        private double increment;

        private bool is_current_set = false;

        private double current;

        public Search Increment(double increment)
        {
            this.increment = increment;
            return this;
        }

        protected double Increment()
        {
            return increment;
        }

        private double Centre()
        {
            return ((long)((Min() + Max()) / increment) / 2) * increment;
        }

        public void Current(double current)
        {
            is_current_set = true;
            this.current = current;
        }

        public double Current()
        {
            if (!is_current_set)
            {
                current = Centre();
            }
            return this.current;
        }

        public virtual Search FindNext(PopulationComparison comparison)
        {
            Search next = new Search();
            next.Increment(increment).Max(Max()).Min(Min());
            switch (comparison)
            {
                case PopulationComparison.TooLow:
                    next.Min(Current());
                    break;
                case PopulationComparison.TooHigh:
                    next.Max(Current());
                    break;
                default:
                    return null;
            }
            double current = next.Current();
            if (Math.Abs(next.Max() - current) < increment) return null;
            if (Math.Abs(next.Min() - current) < increment) return null;
            return next;
        }
    }
}
