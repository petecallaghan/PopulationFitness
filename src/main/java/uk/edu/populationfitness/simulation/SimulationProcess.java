package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Commands;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Generations;
import uk.edu.populationfitness.models.Population;
import uk.edu.populationfitness.output.GenerationsReader;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class SimulationProcess extends Simulation {
    private final List<String> arguments;

    private final Config config;

    private final String processId;

    public SimulationProcess(int parallel_run, List<String> arguments, Config config) {
        super(parallel_run);
        processId = config.id+"-process-"+parallel_run;
        this.arguments = new ArrayList<>();
        this.arguments.addAll(arguments);
        this.arguments.add(Commands.Id);
        this.arguments.add(processId);
        this.config = config;
        generations = new Generations(new Population(config), parallel_run, 1);
    }

    @Override
    public void run() {
        try {
            Process simulation = (new ProcessBuilder(arguments)).start();
            displayProcessOutput(simulation);
            simulation.waitFor();
            readGeneratedResultsFromFile();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayProcessOutput(Process simulation) throws IOException {
        BufferedReader processOutput = new BufferedReader(new InputStreamReader(simulation.getInputStream()));
        String nextLine;
        while((nextLine = processOutput.readLine()) != null){
            System.out.print("Process(");
            System.out.print(parallel_run);
            System.out.print("):");
            System.out.println(nextLine);
        }
    }

    private void readGeneratedResultsFromFile() throws IOException {
        String resultsFile = GenerationsWriter.createResultFileName(processId);
        generations.history.addAll(GenerationsReader.readGenerations(config, resultsFile));
    }
}