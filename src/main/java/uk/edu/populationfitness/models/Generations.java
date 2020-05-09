package uk.edu.populationfitness.models;

import uk.edu.populationfitness.models.genes.GenesIdentifier;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.models.genes.fitness.ReverseSearch;
import uk.edu.populationfitness.models.genes.fitness.Search;
import uk.edu.populationfitness.models.population.Population;
import uk.edu.populationfitness.models.population.PopulationComparison;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The generations recorded for a population
 *
 * Created by pete.callaghan on 04/07/2017.
 */
public class Generations {
    private static final int UNDEFINED_YEAR = -1;
    private static final long NANOS_PER_MILLIS = 1000000;

    public final List<GenerationStatistics> history;

    private Population population;

    public final Config config;

    private int first_year;

    private final int series_run;

    private final int parallel_run;

    public Generations(Population population){
        this(population, 1, 1);
    }

    public Generations(Population population, int parallel_run, int series_run){
        this.population = population;
        this.config = population.config;
        this.series_run = series_run;
        this.parallel_run = parallel_run;
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
        final Epoch epoch = epochs.epochs.get(0);
        first_year = epoch.start_year;
        population.addNewIndividuals(epoch, first_year - config.getMinBreedingAge());
    }

    public PopulationComparison tuneFitnessForAllEpochs(Epochs epochs, double minFactor, double maxFactor, double increment, int percentage){
        addInitialPopulation(epochs);

        PopulationComparison divergence = PopulationComparison.TooLow;
        Epoch previousEpoch = null;
        for (Epoch epoch: epochs.epochs){
            Population previousPopulation = new Population(population);
            Search search = new ReverseSearch();
            search.increment(increment).min(minFactor).max(maxFactor);
            if (previousEpoch != null){
                search.current(previousEpoch.fitness());
            }
            previousEpoch = epoch;
            divergence = generateAndCompareEpochPopulation(search, percentage, epoch, previousPopulation);
            if (divergence != PopulationComparison.WithinRange){
                flushBodiesFromTheCache();
                return divergence;
            }
            flushBodiesFromTheCache();
        }
        return divergence;
    }

    private void flushBodiesFromTheCache(){
        if (SharedCache.cache().isFlushable()){
            Collection<GenesIdentifier> survivors = population.individuals.stream().map(i -> i.genes.identifier()).collect(Collectors.toList());
            SharedCache.cache().retainOnly(survivors);
        }
    }

    private PopulationComparison generateAndCompareEpochPopulation(
            Search search,
            int percentage,
            Epoch epoch,
            Population previousPopulation) {
        population = new Population(previousPopulation);
        epoch.fitness(search.current());
        PopulationComparison divergence = compareToExpectedForEpoch(epoch, percentage);
        System.out.println("Year "+epoch.end_year+" Pop "+population.individuals.size()+" Expected "+epoch.expected_max_population+" F="+epoch.fitness());

        if (divergence != PopulationComparison.WithinRange){
            Search nextSearch = search.findNext(divergence);
            if (nextSearch == null) return divergence;
            return generateAndCompareEpochPopulation(nextSearch, percentage, epoch, previousPopulation);
        }
        return divergence;
    }

    private PopulationComparison compareToExpectedForEpoch(Epoch epoch, int percentage) {
        for(int year = epoch.start_year; year <= epoch.end_year; year++){
            population.killThoseUnfitOrReadyToDie(year, epoch);
            population.addNewGeneration(epoch, year);

            PopulationComparison divergence = compareToExpected(epoch, year, population.individuals.size(), percentage);
            System.out.println(divergence.toString()+": Year "+year+" Pop "+population.individuals.size()+" Expected "+epoch.capacityForYear(year));
            if (divergence != PopulationComparison.WithinRange) {
                return divergence;
            }
        }
        return PopulationComparison.WithinRange;
    }

    private static PopulationComparison compareToExpected(Epoch epoch, int year, int population, int percentage){
        if (population == 0) return PopulationComparison.TooLow;

        final long expected = epoch.capacityForYear(year);

        if (population >= expected * 2) return PopulationComparison.TooHigh;

        long divergence = (population - expected)*100;

        long max_divergence = expected * percentage;

        if (year >= epoch.end_year)
        {
            if (divergence >= max_divergence) return PopulationComparison.TooHigh;

            if (divergence < 0 - max_divergence) return PopulationComparison.TooLow;
        }

        return PopulationComparison.WithinRange;
    }

    private void generateForYear(int year, Epoch epoch) {
        long start_time = System.nanoTime();

        int fatalities = population.killThoseUnfitOrReadyToDie(year, epoch);
        flushBodiesFromTheCache();
        long kill_elapsed = (System.nanoTime() - start_time) / NANOS_PER_MILLIS;

        start_time = System.nanoTime();
        List<Individual> babies = population.addNewGeneration(epoch, year);
        long born_elapsed = (System.nanoTime() - start_time) / NANOS_PER_MILLIS;

        addHistory(epoch, year, babies.size(), fatalities, born_elapsed, kill_elapsed);
    }

    private void addHistory(Epoch epoch, int year, int number_born, int number_killed, long born_elapsed, long kill_elapsed) {
        GenerationStatistics generation = new GenerationStatistics(epoch,
                year,
                population.individuals.size(),
                number_born,
                number_killed,
                born_elapsed,
                kill_elapsed,
                population.average_mutations);
        generation.average_fitness = population.averageFitness();
        generation.fitness_deviation = population.standardDeviationFitness();
        generation.average_age = population.average_age;
        generation.average_life_expectancy = population.average_life_expectancy;
        history.add(generation);

        System.out.println("Run "+parallel_run+"x"+series_run+" Year "+generation.year+" Pop "+generation.population+" Expected "+epoch.expected_max_population+" Born "+generation.number_born+" in "+generation.bornElapsedInHundredths()+"s Killed "+generation.number_killed+" in "+generation.killElapsedInHundredths()+"s");
    }

    public static Generations add(Generations first, Generations second){
        Generations result = new Generations(first.population);
        result.first_year = first.first_year;
        result.history.addAll(GenerationStatistics.add(first.history, second.history));
        return result;
    }
}
