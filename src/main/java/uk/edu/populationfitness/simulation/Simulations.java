package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Commands;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.Generations;
import uk.edu.populationfitness.models.genes.cache.CacheType;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.models.genes.cache.ThreadLocalGenesCache;
import uk.edu.populationfitness.output.AnalysisWriter;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulations {

    /**
     * Launches child runs for the number of runs defined in the tuning file.
     *
     * Waits for all the children to complete and then combines the results into a single file.
     *
     * @param factory
     * @param cacheType
     * @throws IOException
     */
    public static void runAllInParallel(SimulationFactory factory, CacheType cacheType) throws IOException {
        if (cacheType == CacheType.DiskBacked){
            SharedCache.set(new ThreadLocalGenesCache(factory.tuning().parallel_runs));
        }

        List<Simulation> simulations = launchSimulations(factory);
        writeResultsWhenComplete(factory.tuning(), simulations);

        ThreadLocalGenesCache.cleanUp();
    }

    private static List<Simulation> launchSimulations(SimulationFactory factory) {
        List<Simulation> simulations = new ArrayList<>();

        for(int run = 1; run <= factory.tuning().parallel_runs; run++){
            Simulation simulation = factory.createNew(run);
            simulation.start();
            simulations.add(simulation);
        }
        return simulations;
    }

    private static void writeResultsWhenComplete(Tuning tuning, List<Simulation> simulations) throws IOException {
        Generations total = null;

        for (Simulation simulation : simulations) {
            try {
                simulation.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tuning.parallel_runs > 1){
                total = GenerationsWriter.combineGenerationsAndWriteResult(tuning.parallel_runs,
                        tuning.series_runs,
                        simulation.generations,
                        total,
                        tuning);
            }
            else{
                total = simulation.generations;
            }
        }
        writeFinalResults(tuning, total);
    }

    private static void writeFinalResults(Tuning tuning, Generations total) throws IOException {
        String resultsFileName = GenerationsWriter.createResultFileName(total.config.id);
        GenerationsWriter.writeCsv(resultsFileName, total, tuning);
        AnalysisWriter.append(Commands.experimentFile, Commands.tuningFile, Commands.epochsFile, resultsFileName, tuning, total);
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
    public static void addSimulatedEpochsToEndOfTunedEpochs(Config config,
                                                            Epochs epochs,
                                                            Tuning tuning,
                                                            int diseaseYears,
                                                            int postDiseaseYears) {
        int diseaseStartYear = epochs.last().end_year + 1;
        int recoveryStartYear = diseaseStartYear + diseaseYears;
        int finalYear = recoveryStartYear + postDiseaseYears;
        int maxExpected = epochs.last().expected_max_population;

        epochs.addNextEpoch(new Epoch(config, diseaseStartYear).fitness(tuning.disease_fit).max(maxExpected).breedingProbability(tuning.modern_breeding).disease(true).modern(true));
        epochs.addNextEpoch(new Epoch(config, recoveryStartYear).fitness(tuning.historic_fit).max(maxExpected).modern(true));
        epochs.setFinalEpochYear(finalYear);
    }

    /**
     * Changes the initial population size of the simulation using the first epoch expected population.
     *
     * @param config
     * @param epochs
     */
    public static void setInitialPopulationFromFirstEpochCapacity(Config config, Epochs epochs) {
        config.setInitialPopulation(epochs.first().expected_max_population);
    }
}
