package uk.edu.populationfitness.models.genes;

public class FitnessRange {

    // Defines the minimum fitness expected from the fitness function
    private double min_fitness;

    // Defines the max fitness expected from the fitness function
    private double max_fitness;

    private double range;

    public FitnessRange(){
        min_fitness = 0;
        max_fitness = 1;
        range = 1;
    }

    public FitnessRange max(double max){
        max_fitness = max;
        range = max_fitness - min_fitness;
        return this;
    }

    public double max(){
        return max_fitness;
    }

    public double min(){
        return min_fitness;
    }

    public FitnessRange min(double min){
        min_fitness = min;
        range = max_fitness - min_fitness;
        return this;
    }

    // returns the range as a scale. S(f) = (f - min) / (max - min)
    public double toScale(double fitness){
        return (fitness - min_fitness) / range;
    }
}
