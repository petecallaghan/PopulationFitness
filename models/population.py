from functools import reduce
import random
from models.individual import Individual

getRandom = random.uniform # optimize

class Population:
    def __init__(self, config):
        self.config = config
        self.individuals = []
        self.total_fitness = 0
        self.max_fitness = 0

    def addNewIndividuals(self, birth_year): # Adds new individuals to the population
        config = self.config # optimize
        append = self.individuals.append # optimize

        for person in range(0, config.initial_population_size):
            individual = Individual(config, birth_year)
            individual.genes.buildFromRandom()
            append(individual)

    # Produces a new generation. Adds the generation to the population and
    # returns the newly created set of babies
    def addNewGeneration(self, current_year):
        babies = []

        config = self.config # optimize
        probability_of_breeding = config.probability_of_breeding # optmize
        append = babies.append # optimize
        individuals = self.individuals # optimize

        # TODO find a way of optimizing this loop
        for pairIndex in range(0, len(self.individuals)-1, 2):
            # Selects a pair and breeds if they are both of breeding age
            father = individuals[pairIndex]
            mother = individuals[pairIndex + 1]
            pairCanBreed = (getRandom(0, 1) < probability_of_breeding)
            if (father.canBreed(current_year) and mother.canBreed(current_year) and pairCanBreed):
                baby = Individual(config, current_year)
                baby.inheritFromParentsAndMutate(father, mother)
                append(baby)

        # Add the babies to the set of individuals
        self.individuals.extend(babies)
        return babies

    def isNeverUnFit(self, individual, fitness_factor, environment_capacity, kill_constant):
        individual.fitness = 0
        return False

    def isUnfit(self, individual, fitness_factor, environment_capacity, kill_constant):
        individual.fitness = individual.genes.fitness(fitness_factor) * environment_capacity
        return True if individual.fitness < getRandom(0, 1) * kill_constant else False

    def isUnfitUnlimited(self, individual, fitness_factor, environment_capacity, kill_constant):
        individual.fitness = individual.genes.fitness(fitness_factor)
        return True if individual.fitness < getRandom(0, 1) * kill_constant else False

    def selectFitnessFunction(self, epoch):
        if (epoch.isFitnessEnabled() == False):
            return self.isNeverUnFit

        if (epoch.isCapacityUnlimited()):
            return self.isUnfitUnlimited

        return self.isUnfit

    # Kills those in the population who are ready to die and returns the fatalities
    def killThoseUnfitOrReadyToDie(self, current_year, epoch):
        population_size = len(self.individuals) # optimize
        environment_capacity = epoch.environment_capacity / population_size # optimize
        fitness_factor = epoch.fitness_factor # optimize
        kill_constant = epoch.kill_constant # optimize

        # Pick the right fitness function depending on the epoch
        isUnfit = self.selectFitnessFunction(epoch)

        self.individuals = list(filter(lambda x : not (x.isReadyToDie(current_year) or isUnfit(x, fitness_factor, environment_capacity, kill_constant)), self.individuals))

        return population_size - len(self.individuals)

    def getStatistics(self):
        self.total_fitness = reduce(lambda x, y: x + y.fitness, self.individuals, 0)
        self.max_fitness = reduce(lambda x, y: max(x, y.fitness), self.individuals, 0)

    def averageFitness(self):
        return self.total_fitness / len(self.individuals)
