package uk.edu.populationfitness.models.population;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Individual;
import uk.edu.populationfitness.models.RepeatableRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Population {
    public final Config config;

    private final Killed killed = new Killed();
    private final Fitnesses fitnesses = new Fitnesses();

    public ArrayList<Individual> individuals;

    public double average_age = 0.0;
    public double average_mutations = 0.0;
    public double average_life_expectancy = 0.0;

    public Population(Config config){
        this.config = config;
        individuals = new ArrayList<>();
    }

    public Population(Population source){
        this(source.config);
        individuals.addAll(source.individuals);
        fitnesses.copy(source.fitnesses);
    }

    /**
     * Adds new individuals to the population. Adds the number configured as the population size.
     *
     * @param birth_year
     */
    public void addNewIndividuals(Epoch epoch, int birth_year){
        fitnesses.resetCounts();
        for(int i = 0; i < config.getInitialPopulation(); i++){
            Individual individual = new Individual(epoch, birth_year);
            individual.genes.buildFromRandom();
            individuals.add(individual);
            fitnesses.add(individual.genes.fitness());
        }
    }

    /**
     * Produces a new generation. Adds the generation to the population.
     *
     * @param current_year
     * @return The newly created set of babies
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public List<Individual> addNewGeneration(Epoch epoch, int current_year){
        long totalAge = 0;
        ArrayList<Individual> babies = new ArrayList<>();
        double totalMutations = 0;

        for(int i = 0; i < individuals.size() - 1; i+=2){
            Individual father = individuals.get(i);
            Individual mother = individuals.get(i + 1);
            totalAge += father.age(current_year);
            totalAge += mother.age(current_year);
            boolean pairCanBreed = RepeatableRandom.generateNext() < epoch.breedingProbability();
            if (pairCanBreed && father.canBreed(current_year) && mother.canBreed(current_year)){
                Individual baby = new Individual(epoch, current_year);
                totalMutations += baby.inheritFromParents(mother, father);
                babies.add(baby);
            }
        }
        individuals.addAll(babies);
        average_age = individuals.size() > 0 ? totalAge / individuals.size() : 0.0;
        average_mutations = babies.size() > 0 ? totalMutations / babies.size() : 0.0;
        return babies;
    }

    private boolean killWeakerThanMinWithinCapacity(Individual individual, int current_year, double minFitness) {
        final double fitness = individual.genes.fitness();
        fitnesses.add(fitness);

        if (killed.count() > 0 && killed.remaining() < 1){
            return false;
        }
        if (individual.isReadyToDie(current_year) || fitness < minFitness){
            killed.add(individual.age(current_year));
            return true;
        }
        return false;
    }


    private boolean killWeakerThanRandom(Individual individual, int current_year){
        final double fitness = individual.genes.fitness();
        fitnesses.add(fitness);

        if (individual.isReadyToDie(current_year) || fitness < (RepeatableRandom.generateNext())){
            killed.add(individual.age(current_year));
            return true;
        }
        return false;
    }

    private void addSurvivors(Predicate<Individual> survivor){
        individuals = individuals.stream().filter(survivor).collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * Kills those in the population who are ready to die and returns the number of fatalities
     *
     * @param current_year
     * @param epoch
     * @return
     */
    public int killThoseUnfitOrReadyToDie(int current_year, Epoch epoch){
        final double meanFitness = averageFitness();
        final int capacity =  epoch.capacityForYear(current_year);
        killed.setLimit(individuals.size() - capacity);
        fitnesses.resetCounts();

        if (individuals.size() < 1)
            return 0;

        if (epoch.isCapacityUnlimited()){
            addSurvivors(i -> !killWeakerThanRandom(i, current_year));
        }
        else {
            addSurvivors(i -> !killWeakerThanMinWithinCapacity(i, current_year, meanFitness));
        }

        average_life_expectancy = killed.averageAgeKilled();
        return killed.count();

    }

    public double averageFitness(){
        return fitnesses.averageFitness();
    }

    public double standardDeviationFitness(){
        return fitnesses.standardDeviationFitness();
    }
}

