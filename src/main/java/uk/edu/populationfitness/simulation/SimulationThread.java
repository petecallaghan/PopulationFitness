package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.Generations;
import uk.edu.populationfitness.models.Population;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;

public class SimulationThread extends Thread{

    private final Config config;

    private final Epochs epochs;

    public final Generations generations;

    private final Tuning tuning;

    public final int run;

    public SimulationThread(Config config, Epochs epochs, Tuning tuning, int run) {
        this.config = config;
        this.epochs = epochs;
        this.tuning = tuning;
        this.run = run;
        generations = new Generations(new Population(config), run);
    }

    @Override
    public void run() {
        generations.createForAllEpochs(epochs);
        writeResultsToCsv();
        SharedCache.cache().close();
    }

    private void writeResultsToCsv() {
        try {
            GenerationsWriter.writeCsv(run, tuning.number_of_runs, generations, tuning);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
