from math import sin

ALWAYS_FIT = 100000

# Calculates the fitness of a set of genes within a population and epoch
def calculateFitnessWithinPopulationForEpoch(genes, epoch, population_size):
    if (epoch.isFitnessEnabled() == False):
        return ALWAYS_FIT

    fitness = 1
    for float_value in genes.toFloat():
        fitness = fitness * sin(float_value)**epoch.fitness_factor

    if (epoch.isCapacityUnlimited() == False):
        fitness = fitness * epoch.environment_capacity / population_size

    if (fitness < 0):
        fitness = 0

    return fitness
