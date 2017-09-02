package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.PopulationComparison;

public class ReverseFitnessSearch extends FitnessSearch {
    public FitnessSearch findNext(PopulationComparison comparison){
        ReverseFitnessSearch next = new ReverseFitnessSearch();
        next.increment(increment()).max(max()).min(min());
        switch(comparison){
            case TooLow:
                next.max(current());
                break;
            case TooHigh:
                next.min(current());
                break;
            default:
                return null;
        }
        double current = next.current();
        if (Math.abs(next.max() - current) < increment()) return null;
        if (Math.abs(next.min() - current) < increment()) return null;
        return next;
    }
}
