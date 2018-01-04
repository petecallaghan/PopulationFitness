package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.simulation.SimulationProcessFactory;
import uk.edu.populationfitness.simulation.SimulationThreadFactory;
import uk.edu.populationfitness.simulation.Simulations;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Epochs epochs = new Epochs(config);
        Tuning tuning = new Tuning();
        tuning.id = config.id;

        Commands.configureTuningAndEpochsFromInputFiles(config, tuning, epochs, args);
        Simulations.SetInitialPopulationFromFirstEpochCapacity(config, epochs);
        Simulations.AddSimulatedEpochsToEndOfTunedEpochs(config, epochs, tuning, 3, 30);

        if (commandedToRunAsParallelProcesses(tuning)){
            Simulations.RunAllInParallel(new SimulationProcessFactory(tuning,
                    config,
                    Commands.childCommandLine,
                    Commands.epochsFile,
                    Commands.tuningFile),
                    Commands.genesCache);
        }
        else{
            Simulations.RunAllInParallel(new SimulationThreadFactory(config, epochs, tuning),
                    Commands.genesCache);
        }
    }

    private static boolean commandedToRunAsParallelProcesses(Tuning tuning) {
        return !Commands.childCommandLine.isEmpty() && tuning.parallel_runs > 1;
    }
}
