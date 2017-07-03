package uk.edu.populationfitness.models;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Individual {
    private Config config;

    private int birth_year;

    public Genes genes;

    public double fitness;

    public Individual(Config config, int birth_year){
        this.config = config;
        this.birth_year = birth_year;
        this.genes = new Genes(config);
    }

    public int age(int current_year){
        return current_year - birth_year;
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
