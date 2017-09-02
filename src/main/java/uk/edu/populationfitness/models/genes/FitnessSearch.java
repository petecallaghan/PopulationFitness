package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.PopulationComparison;

public class FitnessSearch extends FitnessRange {
    private double increment;

    public FitnessSearch increment(double increment){
        this.increment = increment;
        return this;
    }

    public double increment(){
        return increment;
    }

    public double current(){
        return ((int)((min() + max()) / increment) / 2) * increment;
    }

    public FitnessSearch findNext(PopulationComparison comparison){
        FitnessSearch next = new FitnessSearch();
        next.increment(increment).max(max()).min(min());
        switch(comparison){
            case TooLow:
                next.min(current());
                break;
            case TooHigh:
                next.max(current());
                break;
            default:
                return null;
        }
        double current = next.current();
        if (Math.abs(next.max() - current) < increment) return null;
        if (Math.abs(next.min() - current) < increment) return null;
        return next;
    }
}
