using System;

namespace PopulationFitness.Models.Genes.Fitness
{
    public class ReverseSearch : Search
    {

        public override Search FindNext(PopulationComparison comparison)
        {
            ReverseSearch next = new ReverseSearch();
            next.Increment(Increment()).Max(Max()).Min(Min());
            switch (comparison)
            {
                case PopulationComparison.TooLow:
                    next.Max(Current());
                    break;
                case PopulationComparison.TooHigh:
                    next.Min(Current());
                    break;
                default:
                    return null;
            }
            double current = next.Current();
            if (Math.Abs(next.Max() - current) < Increment()) return null;
            if (Math.Abs(next.Min() - current) < Increment()) return null;
            return next;
        }
    }
}
