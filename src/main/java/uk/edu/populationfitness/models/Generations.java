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

    public Population population;

    public final Config config;

    public int first_year;

    public Generations(Population population){
        this.population = population;
        this.config = population.config;
        history = new ArrayList<>();
        first_year = UNDEFINED_YEAR;
    }

    public void createForAllEpochs(Epochs epochs){
        addInitialPopulation(epochs);

        for (Epoch epoch: epochs.epochs){
            for(int year = epoch.start_year; year <= epoch.end_year; year++){
                generateForYear(year, epoch);
            }
        }
    }

    private void addInitialPopulation(Epochs epochs) {
        first_year = epochs.epochs.get(0).start_year;
        population.addNewIndividuals(first_year);
    }

    public boolean tuneFitnessFactorsForAllEpochs(Epochs epochs, double minFactor, double maxFactor, double increment, int percentage){
        addInitialPopulation(epochs);

        for (Epoch epoch: epochs.epochs){
            epoch.fitness(minFactor);
            Population previousPopulation = new Population(population);
            boolean isDivergent = true;
            for(epoch.fitness(minFactor); isDivergent && Math.abs(epoch.fitness() - maxFactor) > increment; epoch.fitness(epoch.fitness() + increment)){
                population = new Population(previousPopulation);
                isDivergent = populationIsDivergentForEpoch(epoch, percentage);
            }
            if (isDivergent) return false;
        }
        return true;
    }

    private boolean populationIsDivergentForEpoch(Epoch epoch, int percentage) {
        for(int year = epoch.start_year; year <= epoch.end_year; year++){
            if (generateForYear(year, epoch).hasDivergedFromExpected(percentage)) return true;
        }
        return false;
    }

    public GenerationStatistics generateForYear(int year, Epoch epoch) {
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
        generation.average_fitness = population.averageFitness();
        generation.fitness_deviation = population.standardDeviationFitness();
        generation.average_age = population.average_age;
        history.add(generation);

        System.out.println("Year "+generation.year+" Pop "+generation.population+" Expected "+epoch.expected_max_population+" Born "+generation.number_born+" in "+generation.bornElapsedInHundredths()+"s Killed "+generation.number_killed+" in "+generation.killElapsedInHundredths()+"s");
        return generation;
    }
}
