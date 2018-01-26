package uk.edu.populationfitness.simulation;

import uk.edu.populationfitness.Tuning;

/**
 * Implement this to create new simulations
 */
interface SimulationFactory {
    Tuning tuning();

    Simulation createNew(int run);
}
