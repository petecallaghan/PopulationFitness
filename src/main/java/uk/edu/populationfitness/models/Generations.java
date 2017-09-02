package uk.edu.populationfitness.models;

import uk.edu.populationfitness.models.genes.FitnessSearch;
import uk.edu.populationfitness.models.genes.ReverseFitnessSearch;

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

    public PopulationComparison tuneFitnessFactorsForAllEpochs(Epochs epochs, double minFactor, double maxFactor, double increment, int percentage){
        addInitialPopulation(epochs);

        PopulationComparison divergence = PopulationComparison.TooLow;
        for (Epoch epoch: epochs.epochs){
            Population previousPopulation = new Population(population);
            FitnessSearch search = new FitnessSearch();
            search.increment(increment).min(minFactor).max(maxFactor);
            divergence = generateAndCompareEpochPopulation(search, percentage, epoch, previousPopulation);
            if (divergence != PopulationComparison.WithinRange){
                // Try the reverse search
                search = new ReverseFitnessSearch();
                search.increment(increment).min(minFactor).max(maxFactor);
                divergence = generateAndCompareEpochPopulation(search, percentage, epoch, previousPopulation);
                if (divergence != PopulationComparison.WithinRange)
                    return divergence;
            }
        }
        return divergence;
    }

    private PopulationComparison generateAndCompareEpochPopulation(
            FitnessSearch search,
            int percentage,
            Epoch epoch,
            Population previousPopulation) {
        population = new Population(previousPopulation);
        epoch.fitness(search.current());
        PopulationComparison divergence = compareToExpectedForEpoch(epoch, percentage);
        System.out.println("Year "+epoch.end_year+" Pop "+population.individuals.size()+" Expected "+epoch.expected_max_population+" F="+epoch.fitness());
        if (divergence != PopulationComparison.WithinRange){
            FitnessSearch nextSearch = search.findNext(divergence);
            if (nextSearch == null) return divergence;
            return generateAndCompareEpochPopulation(nextSearch, percentage, epoch, previousPopulation);
        }
        return divergence;
    }

    private PopulationComparison compareToExpectedForEpoch(Epoch epoch, int percentage) {
        for(int year = epoch.start_year; year <= epoch.end_year; year++){
            PopulationComparison divergence = generateForYear(year, epoch).compareToExpected(percentage);
            if (divergence != PopulationComparison.WithinRange) {
                return divergence;
            }
        }
        return PopulationComparison.WithinRange;
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

//        System.out.println("Year "+generation.year+" Pop "+generation.population+" Expected "+epoch.expected_max_population+" Born "+generation.number_born+" in "+generation.bornElapsedInHundredths()+"s Killed "+generation.number_killed+" in "+generation.killElapsedInHundredths()+"s");
        return generation;
    }
}
