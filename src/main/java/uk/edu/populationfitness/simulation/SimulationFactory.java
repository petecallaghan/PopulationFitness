package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Tuning;

/**
 * Implement this to create new simulations
 */
public interface SimulationFactory {
    Tuning tuning();

    Simulation createNew(int run);
}
