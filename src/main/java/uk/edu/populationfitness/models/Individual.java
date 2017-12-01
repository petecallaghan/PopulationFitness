package uk.edu.populationfitness.models;

import uk.edu.populationfitness.models.genes.Genes;

import static java.lang.Math.abs;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Individual {
    private Config config;

    public final int birth_year;

    public final Genes genes;

    public double fitness;

    public Individual(Config config, int birth_year){
        this.config = config;
        this.birth_year = birth_year;
        this.genes = config.genesFactory.build(config);
    }

    public int age(int current_year){
        return abs(current_year - birth_year);
    }

    public boolean isReadyToDie(int current_year){
        return age(current_year) >= config.max_age;
    }

    public boolean canBreed(int current_year){
        int age = age(current_year);
        return age >= config.min_breeding_age && age <= config.max_breeding_age;
    }

    public void inheritFromParentsAndMutate(Individual mother, Individual father){
        genes.inheritFrom(mother.genes, father.genes);
        genes.mutate();
    }
}
