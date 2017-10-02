package uk.edu.populationfitness;

import uk.edu.populationfitness.models.*;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = new Epochs(config);
        Tuning tuning = new Tuning();
        tuning.id = config.id;

        Commands.configureTuningAndEpochsFromInputFiles(config, tuning, epochs, args);
        SetInitialPopulationFromFirstEpochCapacity(config, epochs);
        AddSimulatedEpochsToEndOfTunedEpochs(config, epochs, tuning, 3, 80);
        generations.createForAllEpochs(epochs);
        GenerationsWriter.writeCsv(generations, tuning);
    }

    private static void AddSimulatedEpochsToEndOfTunedEpochs(Config config,
                                                             Epochs epochs,
                                                             Tuning tuning,
                                                             int diseaseYears,
                                                             int postDiseaseYears) {
        int diseaseStartYear = epochs.last().end_year + 1;
        int recoveryStartYear = diseaseStartYear + diseaseYears;
        int finalYear = recoveryStartYear + postDiseaseYears;
        int maxExpected = epochs.last().expected_max_population;

        epochs.addNextEpoch(new Epoch(config, diseaseStartYear).fitness(tuning.disease_fit).max(maxExpected).kill(tuning.historic_kill));
        epochs.addNextEpoch(new Epoch(config, recoveryStartYear).fitness(tuning.historic_fit).max(maxExpected).kill(tuning.historic_kill));
        epochs.setFinalEpochYear(finalYear);
    }

    private static void SetInitialPopulationFromFirstEpochCapacity(Config config, Epochs epochs) {
        config.initial_population = epochs.first().expected_max_population;
    }
}
