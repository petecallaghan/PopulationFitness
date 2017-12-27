package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epochs;

public class SimulationThreadFactory implements SimulationFactory {
    private final Config config;

    private final Epochs epochs;

    private final Tuning tuning;

    public SimulationThreadFactory(Config config, Epochs epochs, Tuning tuning) {
        this.config = config;
        this.epochs = epochs;
        this.tuning = tuning;
    }

    @Override
    public Tuning tuning() {
        return tuning;
    }

    @Override
    public Simulation createNew(int run) {
        return new SimulationThread(config, epochs, tuning, run);
    }
}
