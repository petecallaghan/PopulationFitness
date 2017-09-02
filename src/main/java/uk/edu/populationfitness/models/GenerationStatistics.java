package uk.edu.populationfitness.models;

import static java.lang.Math.abs;

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
    public double average_fitness;
    public double fitness_deviation;
    public int average_age;

    public GenerationStatistics(Epoch epoch, int year, int population, int number_born, int number_killed, long born_time, long kill_time){
        this. number_born = number_born;
        this. number_killed = number_killed;
        this. population = population;
        this. year = year;
        this. epoch = epoch;
        this. born_time = born_time;
        this. kill_time = kill_time;
        this. average_fitness = 0;
        this.fitness_deviation = 0;
        this.average_age = 0;
    }

    public double bornElapsedInHundredths(){
        return (double) (born_time / 10) / 100;
    }

    public double killElapsedInHundredths(){
        return (double) (kill_time / 10) / 100;
    }

    /**
     *
     * @param percentage the maximum percentage difference between actual and expected population that defines convergence
     *
     * @return TooLow if the population is too low, TooHigh if the population is too high
     */
    public PopulationComparison compareToExpected(int percentage){
        if (population == 0) return PopulationComparison.TooLow;

        if (population >= epoch.expected_max_population * 2) return PopulationComparison.TooHigh;

        int divergence = (population - epoch.expected_max_population)*100;

        int max_divergence = epoch.expected_max_population * percentage;

        if (year >= epoch.end_year)
        {
            if (divergence >= max_divergence) return PopulationComparison.TooHigh;

            if (divergence < 0 - max_divergence) return PopulationComparison.TooLow;
        }

        return PopulationComparison.WithinRange;
    }
}
