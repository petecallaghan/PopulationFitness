package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Commands;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationProcessFactory implements SimulationFactory {
    private final Tuning tuning;

    private final Config config;

    private final List<String> arguments;

    public SimulationProcessFactory(Tuning tuning, Config config, String commandLine, String epochsFile, String tuningFile) {
        this.tuning = tuning;
        this.config = config;
        arguments = new ArrayList<>();
        arguments.add("java");
        arguments.addAll(Arrays.asList(commandLine.trim().split("\\s+")));
        this.arguments.add(Commands.EpochsFile);
        this.arguments.add(epochsFile);
        this.arguments.add(Commands.TuningFile);
        this.arguments.add(tuningFile);
    }

    @Override
    public Tuning tuning() {
        return tuning;
    }

    @Override
    public Simulation createNew(int run) {
        return new SimulationProcess(run, arguments, config);
    }
}
