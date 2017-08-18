package uk.edu.populationfitness.models.genes;

public class FitnessRange {

    // Defines the minimum fitness expected from the fitness function
    public double min_fitness;

    // Defines the max fitness expected from the fitness function
    public double max_fitness;

    public FitnessRange(){
        min_fitness = 0;
        max_fitness = 1;
    }

    public FitnessRange max(double max){
        max_fitness = max;
        return this;
    }

    public FitnessRange min(double min){
        min_fitness = min;
        return this;
    }

    // returns the range as a scale. S(f) = (f - min) / (max - min)
    public double toScale(double fitness){
        return (fitness - min_fitness) / (max_fitness - min_fitness);
    }
}
