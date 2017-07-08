package uk.edu.populationfitness.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The generations recorded for a population
 *
 * Created by pete.callaghan on 04/07/2017.
 */
public class Generations {
    private static final int UNDEFINED_YEAR = -1;
    private static final long NANOS_PER_MILLIS = 1000000;

    public final List<GenerationStatistics> history;

    public final Population population;

    public int first_year;

    public Generations(Population population){
        this.population = population;
        history = new ArrayList<>();
        first_year = UNDEFINED_YEAR;
    }

    public void createForAllEpochs(Epochs epochs){
        for (Epoch epoch: epochs.epochs){
            for(int year = epoch.start_year; year <= epoch.end_year; year++){
                createForYear(year, epoch);
            }
        }

    }

    public GenerationStatistics createForYear(int year, Epoch epoch) {
        if (first_year == UNDEFINED_YEAR){
            first_year = year;
            // Add an initial population
            population.addNewIndividuals(year);
        }

        long start_time = System.nanoTime();
        int fatalities = population.killThoseUnfitOrReadyToDie(year, epoch);
        long kill_elapsed = (System.nanoTime() - start_time) / NANOS_PER_MILLIS;

        start_time = System.nanoTime();
        List<Individual> babies = population.addNewGeneration(epoch, year);
        long born_elapsed = (System.nanoTime() - start_time) / NANOS_PER_MILLIS;

        return addHistory(epoch, year, babies.size(), fatalities, born_elapsed, kill_elapsed);
    }

    private GenerationStatistics addHistory(Epoch epoch, int year, int number_born, int number_killed, long born_elapsed, long kill_elapsed) {
        GenerationStatistics generation = new GenerationStatistics(epoch, year, population.individuals.size(), number_born, number_killed, born_elapsed, kill_elapsed);
        generation.total_fitness = population.total_fitness;
        generation.average_fitness = population.averageFitness();
        generation.max_fitness = population.max_fitness;
        history.add(generation);

        System.out.println("Year "+generation.year+" Pop "+generation.population+" Expected "+epoch.expected_max_population+" Born "+generation.number_born+" in "+generation.bornElapsedInHundredths()+"s Killed "+generation.number_killed+" in "+generation.killElapsedInHundredths()+"s");
        return generation;
    }

}
