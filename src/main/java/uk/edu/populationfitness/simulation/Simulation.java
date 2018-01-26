package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.models.Generations;

/***
 * Implement this to run a simulation
 */
public class Simulation extends Thread {

    public Generations generations;

    final int parallel_run;

    Simulation(int parallel_run) {
        this.parallel_run = parallel_run;
    }
}
