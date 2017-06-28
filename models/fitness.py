from math import sin
from functools import reduce

ALWAYS_FIT = 100000

# Calculates the fitness of a set of genes within a population and epoch
def calculateFitnessWithinPopulationForEpoch(genes, epoch, population_size):
    if (epoch.isFitnessEnabled() == False):
        return ALWAYS_FIT

    fitness_factor = epoch.fitness_factor #optimize
    fitness = 1

    fitness = reduce((lambda x, y : x * sin(y)**fitness_factor), genes.toFloat(), 1)

    if (epoch.isCapacityUnlimited() == False):
        fitness = fitness * epoch.environment_capacity / population_size

    if (fitness < 0):
        fitness = 0

    return fitness
