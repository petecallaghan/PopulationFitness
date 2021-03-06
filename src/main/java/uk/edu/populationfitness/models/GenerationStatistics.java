package uk.edu.populationfitness.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class GenerationStatistics {
    public final int number_born;
    public final int number_killed;
    public final int population;
    public final int year;
    public final Epoch epoch;
    private final long born_time;
    private final long kill_time;
    public double average_fitness;
    public double fitness_deviation;
    public double average_age;
    public double average_mutations;
    public double average_life_expectancy;

    public GenerationStatistics(Epoch epoch,
                                int year,
                                int population,
                                int number_born,
                                int number_killed,
                                long born_time,
                                long kill_time,
                                double average_mutations){
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
        this.average_mutations = average_mutations;
    }

    public double bornElapsedInHundredths(){
        return (double) (born_time / 10) / 100;
    }

    public double killElapsedInHundredths(){
        return (double) (kill_time / 10) / 100;
    }

    private static double average(double total, long count){
        return count < 1 ? total : total / count;
    }

    public static GenerationStatistics add(GenerationStatistics first, GenerationStatistics second){
        if (first.year != second.year) throw new Error("Cannot add different years");

        GenerationStatistics result = new GenerationStatistics(new Epoch(first.epoch),
                first.year,
                first.population + second.population,
                first.number_born + second.number_born,
                first.number_killed + second.number_killed,
                first.born_time + second.born_time,
                first.kill_time + second.kill_time,
                first.average_mutations);
        result.average_age = average(first.average_age * first.population + second.average_age * second.population, result.population);
        result.average_fitness = average(first.average_fitness * first.population + second.average_fitness * second.population, result.population);
        result.fitness_deviation = average(first.fitness_deviation * first.population + second.fitness_deviation * second.population, result.population);
        result.epoch.expected_max_population += second.epoch.expected_max_population;
        result.epoch.environment_capacity += second.epoch.environment_capacity;
        result.epoch.prev_environment_capacity += second.epoch.prev_environment_capacity;
        result.average_mutations = average((first.average_mutations * first.number_born + second.average_mutations * second.number_born), result.number_born);
        result.average_life_expectancy = average(first.average_life_expectancy * first.number_killed + second.average_life_expectancy * second.number_killed, result.number_killed);
        return result;
    }

    public static List<GenerationStatistics> add(List<GenerationStatistics> first, List<GenerationStatistics> second){
        ArrayList<GenerationStatistics> result = new ArrayList<>();

        GenerationStatistics[] firstStats = first.toArray(new GenerationStatistics[0]);
        GenerationStatistics[] secondStats = second.toArray(new GenerationStatistics[0]);

        if (firstStats.length != secondStats.length){
            throw new Error("Cannot add different numbers of generations");
        }

        for (int i = 0; i < firstStats.length; i++) {
            result.add(add(firstStats[i], secondStats[i]));
        }

        return result;
    }
}
