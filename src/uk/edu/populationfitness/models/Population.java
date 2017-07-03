package uk.edu.populationfitness.models;

import java.util.*;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Population {
    private Config config;

    private ArrayList<Individual> individuals = new ArrayList<Individual>();

    public double total_fitness = 0.0;

    public double max_fitness = 0.0;

    private Random random = new Random();

    public Population(Config config){
        this.config = config;
    }

    /**
     * Adds new individuals to the population. Adds the number configured as the population size.
     * @param birth_year
     */
    public void addNewIndividuals(int birth_year){

        for(int i = 0; i < config.initial_population_size; i++){
            Individual individual = new Individual(config, birth_year);
            individual.genes.buildFromRandom();
            individuals.add(individual);
        }
    }

    /**
     * Produces a new generation. Adds the generation to the population.
     * @param current_year
     * @return The newly created set of babies
     */
    public List<Individual> addNewGeneration(int current_year){
        ArrayList<Individual> babies = new ArrayList<Individual>();

        for(int i = 0; i < babies.size() - 1; i+=2){
            Individual father = individuals.get(i);
            Individual mother = individuals.get(i);
            boolean pairCanBreed = random.nextDouble() < config.probability_of_breeding;
            if (pairCanBreed && father.canBreed(current_year) && mother.canBreed(current_year)){
                Individual baby = new Individual(config, current_year);
                baby.inheritFromParentsAndMutate(mother, father);
                babies.add(baby);
            }
        }
        individuals.addAll(babies);
        return babies;
    }
}

