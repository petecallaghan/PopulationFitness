package uk.edu.populationfitness.models.genes.fitness;

import uk.edu.populationfitness.models.population.PopulationComparison;

public class ReverseSearch extends Search {
    public Search findNext(PopulationComparison comparison){
        ReverseSearch next = new ReverseSearch();
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
