package uk.edu.populationfitness.models;

/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class GenerationStatistics {
    public final int number_born;
    public final int number_killed;
    public final int population;
    public final int year;
    public final Epoch epoch;
    public final long born_time;
    public final long kill_time;
    public double total_fitness;
    public double average_fitness;
    public double max_fitness;

    public GenerationStatistics(Epoch epoch, int year, int population, int number_born, int number_killed, long born_time, long kill_time){
        this. number_born = number_born;
        this. number_killed = number_killed;
        this. population = population;
        this. year = year;
        this. epoch = epoch;
        this. born_time = born_time;
        this. kill_time = kill_time;
        this. total_fitness = 0;
        this. average_fitness = 0;
        this. max_fitness = 0;
    }

    double bornElapsedInHundredths(){
        return (double) (born_time / 10) / 100;
    }

    double killElapsedInHundredths(){
        return (double) (kill_time / 10) / 100;
    }
}
