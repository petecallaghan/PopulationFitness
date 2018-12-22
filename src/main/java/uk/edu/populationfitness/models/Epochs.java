package uk.edu.populationfitness.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class Epochs {
    public final List<Epoch> epochs;

    public Epochs(){
        epochs = new ArrayList<>();
    }

    /***
     * Adds an epoch with the specified start year.
     * Sets the end year of the preceding epoch to the year
     * before the start of this epoch (if there is a previous epoch)
     *
     * @param epoch
     */
    public void addNextEpoch(Epoch epoch){
        if (!epochs.isEmpty()){
            Epoch previous = epochs.get(epochs.size() - 1);
            previous.end_year = epoch.start_year - 1;
            epoch.prev_environment_capacity = previous.capacity();
        }
        else
        {
            epoch.prev_environment_capacity = epoch.capacity();
        }

        epochs.add(epoch);
    }

    public void addAll(List<Epoch> epochs){
        for (Epoch e: epochs) {
            this.addNextEpoch(e);
        }
    }

    /***
     * Set the end year of the final epoch
     *
     * @param last_year
     */
    public void setFinalEpochYear(int last_year){
        Epoch previous = epochs.get(epochs.size() - 1);
        previous.end_year = last_year;
    }

    /***
     *
     * @return the last epoch
     */
    public Epoch last(){
        return epochs.get(epochs.size() - 1);
    }

    public Epoch first(){
        return epochs.get(0);
    }

    /**
     * Reduces the populations for all epochs by the same ratio
     *
     * P' = P/ratio
     *
     * @param ratio
     */
    public void reducePopulation(int ratio){
        for(Epoch epoch: epochs){
            epoch.reducePopulation(ratio);
        }
    }

    /**
     * Increases the populations for all epochs by the same ratio
     *
     * P' = P * ratio
     *
     * @param ratio
     */
    public void increasePopulation(int ratio){
        for(Epoch epoch: epochs){
            epoch.increasePopulation(ratio);
        }
    }

    public void printFitnessFactors(){
        for(Epoch epoch: epochs){
            System.out.print("Epoch ");
            System.out.print(epoch.start_year);
            System.out.print(" f=");
            System.out.println(epoch.fitness());
        }
    }
}
