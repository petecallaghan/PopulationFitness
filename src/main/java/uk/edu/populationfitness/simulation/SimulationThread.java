package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.Generations;
import uk.edu.populationfitness.models.Population;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;

public class SimulationThread extends Simulation{

    private final Config config;

    private final Epochs epochs;

    private final Tuning tuning;

    public SimulationThread(Config config, Epochs epochs, Tuning tuning, int run) {
        super(run);
        this.config = config;
        this.epochs = epochs;
        this.tuning = tuning;
        generations = null;
    }

    @Override
    public void run() {
        Generations total = null;

        for(int series_run = 1; series_run <= tuning.series_runs; series_run++){
            Generations current = RunSimulation(series_run);
            try {
                total = GenerationsWriter.combineGenerationsAndWriteResult(parallel_run, series_run, current, total, tuning);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedCache.cache().close();
        }
        generations = total;
    }

    private Generations RunSimulation(int series_run) {
        Generations current = new Generations(new Population(config), parallel_run, series_run);
        current.createForAllEpochs(epochs);
        return current;
    }
}
