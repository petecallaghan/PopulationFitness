package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.simulation.RunType;
import uk.edu.populationfitness.simulation.Simulation;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Epochs epochs = new Epochs(config);
        Tuning tuning = new Tuning();
        tuning.id = config.id;

        RunType runType = Commands.configureTuningAndEpochsFromInputFiles(config, tuning, epochs, args);
        Simulation.SetInitialPopulationFromFirstEpochCapacity(config, epochs);
        Simulation.AddSimulatedEpochsToEndOfTunedEpochs(config, epochs, tuning, 3, 30);

        switch (runType){
            default:
            case Series:
                Simulation.RunAllInSeries(config, epochs, tuning);
                break;
            case Parallel:
                Simulation.RunAllInParallel(config, epochs, tuning);
                break;
        }
    }
}
