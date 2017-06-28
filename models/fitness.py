from math import sin
from functools import reduce

# Calculates the fitness of a set of genes within a population and epoch
def calculateFitnessWithinPopulationForEpoch(genes, epoch, population_size):
    fitness = calculateFitnessForEpoch(genes, epoch, population_size)
    fitness = fitness * epoch.environment_capacity / population_size
    return fitness

def calculateFitnessForEpoch(genes, epoch, population_size):
    fitness_factor = epoch.fitness_factor #optimize
    fitness = 1

    fitness = reduce((lambda x, y : x * sin(y)**fitness_factor), genes.toFloat(), 1)

    if (fitness < 0):
        fitness = 0

    return fitness
