from math import asin

ALWAYS_FIT = 100000

# Calculates the fitness of a set of genes within a population and epoch
def calculateFitnessWithinPopulationForEpoch(genes, epoch, population_size):
    if (epoch.isFitnessEnabled() == False):
        return ALWAYS_FIT

    fitness = 1
    for float in genes.toFloat():
        fitness = fitness * asin(float)**epoch.fitness_factor

    if (epoch.isCapacityUnlimited() == False):
        fitness = fitness * epoch.environment_capacity / population_size

    if (fitness < 0):
        fitness = 0

    return fitness
