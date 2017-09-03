package uk.edu.populationfitness.models.genes.fitness;

public class FitnessRange {

    // Defines the minimum fitness expected from the fitness function
    private double min_fitness;

    // Defines the max fitness expected from the fitness function
    private double max_fitness;

    private double range;

    private Statistics statistics;

    public FitnessRange(Statistics statistics){
        min_fitness = 0;
        max_fitness = 1;
        range = 1;
        this.statistics = statistics;
    }

    public FitnessRange(){
        this(null);
    }

    public FitnessRange max(double max){
        max_fitness = max;
        range = max_fitness - min_fitness;
        return this;
    }

    public FitnessRange min(double min){
        min_fitness = min;
        range = max_fitness - min_fitness;
        return this;
    }

    public double max(){
        return max_fitness;
    }

    public double min(){
        return min_fitness;
    }

    public FitnessRange statistics(Statistics stats){
        this.statistics = stats;
        return this;
    }

    public Statistics statistics() {
        return statistics;
    }

    // returns the range as a scale. S(f) = (f - min) / (max - min)
    public double toScale(double fitness){
        if (statistics != null){
            statistics.add(fitness);
        }
        return (fitness - min_fitness) / range;
    }
}
