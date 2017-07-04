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
        }

        epochs.add(epoch);
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
}