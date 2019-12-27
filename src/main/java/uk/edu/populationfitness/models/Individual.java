package uk.edu.populationfitness.models;

import uk.edu.populationfitness.models.genes.Genes;

import static java.lang.Math.abs;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Individual {
    private final Epoch epoch;

    public final int birth_year;

    public final Genes genes;

    public Individual(Epoch epoch, int birth_year){
        this.epoch = epoch;
        this.birth_year = birth_year;
        this.genes = epoch.config().getGenesFactory().build(epoch.config());
    }

    public int age(int current_year){
        return abs(current_year - birth_year);
    }

    public boolean isReadyToDie(int current_year){
        return age(current_year) >= epoch.maxAge();
    }

    public boolean canBreed(int current_year){
        int age = age(current_year);
        return age >= epoch.config().getMinBreedingAge() && age <= epoch.maxBreedingAge();
    }

    public int inheritFromParents(Individual mother, Individual father){
        return genes.inheritFrom(mother.genes, father.genes);
    }

    public boolean isUpdatingMaxFitness(){
        return this.epoch.isUpdatingMaxFitness();
    }
}
