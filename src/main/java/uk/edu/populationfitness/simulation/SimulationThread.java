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

    public Generations generations;

    private final Tuning tuning;

    public final int parallel_run;

    public SimulationThread(Config config, Epochs epochs, Tuning tuning, int run) {
        this.config = config;
        this.epochs = epochs;
        this.tuning = tuning;
        this.parallel_run = run;
        generations = null;
    }

    @Override
    public void run() {
        Generations total = null;

        for(int series_run = 1; series_run <= tuning.series_runs; series_run++){
            Generations current = RunSimulation(series_run);
            try {
                total = CombineGenerationsAndWriteResult(parallel_run, series_run, current, total, tuning);
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

    public static Generations CombineGenerationsAndWriteResult(int parallel_run,
                                                               int series_run,
                                                               Generations current,
                                                               Generations total,
                                                               Tuning tuning) throws IOException {
        total = (total == null ? current : Generations.add(total, current));
        GenerationsWriter.writeCsv(parallel_run, series_run, tuning.series_runs * tuning.parallel_runs, total, tuning);
        return total;
    }
}
