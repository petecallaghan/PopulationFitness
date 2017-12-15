package uk.edu.populationfitness;

import uk.edu.populationfitness.models.*;
import uk.edu.populationfitness.models.genes.cache.DiskBackedGeneValues;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Epochs epochs = new Epochs(config);
        Tuning tuning = new Tuning();
        tuning.id = config.id;

        Commands.configureTuningAndEpochsFromInputFiles(config, tuning, epochs, args);
        SetInitialPopulationFromFirstEpochCapacity(config, epochs);
        AddSimulatedEpochsToEndOfTunedEpochs(config, epochs, tuning, 3, 30);

        SimulateAllRuns(config, epochs, tuning);
    }

    private static void SimulateAllRuns(Config config, Epochs epochs, Tuning tuning) throws IOException {
        Generations total = null;

        for(int run = 0; run < tuning.number_of_runs; run++){
            Generations current = RunSimulationAndWriteResult(config, epochs, tuning);

            total = CombineGenerationsAndWriteResult(run, current, total, tuning);
        }
    }

    private static Generations CombineGenerationsAndWriteResult(int run, Generations current, Generations total, Tuning tuning) throws IOException {
        StringBuffer prefix = new StringBuffer("run");
        prefix.append(run + 1);
        prefix.append("of");
        prefix.append(tuning.number_of_runs);

        Generations combined = (total == null ? current : Generations.add(total, current));
        GenerationsWriter.writeCsv(prefix.toString(), combined, tuning);
        return combined;
    }

    private static Generations RunSimulationAndWriteResult(Config config, Epochs epochs, Tuning tuning) throws IOException {
        SharedCache.set(new DiskBackedGeneValues());

        Generations generations = new Generations(new Population(config));
        generations.createForAllEpochs(epochs);
        GenerationsWriter.writeCsv(generations, tuning);

        SharedCache.cache().close();
        return generations;
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

        epochs.addNextEpoch(new Epoch(config, diseaseStartYear).fitness(tuning.disease_fit).max(maxExpected).kill(tuning.historic_kill).breedingProbability(tuning.modern_breeding));
        epochs.addNextEpoch(new Epoch(config, recoveryStartYear).fitness(tuning.historic_fit).max(maxExpected).kill(tuning.historic_kill));
        epochs.setFinalEpochYear(finalYear);
    }

    private static void SetInitialPopulationFromFirstEpochCapacity(Config config, Epochs epochs) {
        config.initial_population = epochs.first().expected_max_population;
    }
}
