package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.Generations;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.models.genes.cache.ThreadLocalGenesCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    /**
     * Launches child runs for the number of runs defined in the tuning file.
     *
     * Waits for all the children to complete and then combines the results into a single file.
     *
     * @param config
     * @param epochs
     * @param tuning
     * @throws IOException
     */
    public static void RunAllInParallel(Config config, Epochs epochs, Tuning tuning) throws IOException {
        SharedCache.set(new ThreadLocalGenesCache(tuning.parallel_runs));

        List<SimulationThread> simulations = launchSimulations(config, epochs, tuning);
        writeResultsWhenComplete(tuning, simulations);
    }

    private static List<SimulationThread> launchSimulations(Config config, Epochs epochs, Tuning tuning) {
        List<SimulationThread> simulations = new ArrayList<>();

        for(int run = 1; run <= tuning.parallel_runs; run++){
            SimulationThread simulation = new SimulationThread(config, epochs, tuning, run);
            simulation.start();
            simulations.add(simulation);
        }
        return simulations;
    }

    private static void writeResultsWhenComplete(Tuning tuning, List<SimulationThread> simulations) throws IOException {
        Generations total = null;

        for (SimulationThread simulation : simulations) {
            try {
                simulation.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tuning.parallel_runs > 1){
                total = SimulationThread.CombineGenerationsAndWriteResult(tuning.parallel_runs, tuning.series_runs, simulation.generations, total, tuning);
            }
        }
    }

    /**
     * Adds the simulation epochs to the historical epochs read from the file
     *
     * @param config
     * @param epochs
     * @param tuning
     * @param diseaseYears
     * @param postDiseaseYears
     */
    public static void AddSimulatedEpochsToEndOfTunedEpochs(Config config,
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

    /**
     * Changes the initial population size of the simulation using the first epoch expected population.
     *
     * @param config
     * @param epochs
     */
    public static void SetInitialPopulationFromFirstEpochCapacity(Config config, Epochs epochs) {
        config.initial_population = epochs.first().expected_max_population;
    }
}
