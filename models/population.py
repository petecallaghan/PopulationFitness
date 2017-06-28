import random
from models.individual import Individual
from models.fitness import calculateFitnessWithinPopulationForEpoch

class Population:
    def __init__(self, config):
        self.config = config
        self.individuals = []

    def addNewIndividuals(self, birth_year): # Adds new individuals to the population
        for person in range(0, self.config.initial_population_size):
            individual = Individual(self.config, birth_year)
            individual.genes.buildFromRandom()
            self.individuals.append(individual)

    # Produces a new generation. Adds the generation to the population and
    # returns the newly created set of babies
    def addNewGeneration(self, current_year):
        babies = []
        for pairIndex in range(0, len(self.individuals)-1, 2):
            # Selects a pair and breeds if they are both of breeding age
            father = self.individuals[pairIndex]
            mother = self.individuals[pairIndex + 1]
            pairCanBreed = (random.uniform(0, 1) < self.config.probability_of_breeding)
            if (father.canBreed(current_year) and mother.canBreed(current_year) and pairCanBreed):
                baby = Individual(self.config, current_year)
                baby.inheritFromParentsAndMutate(father, mother)
                babies.append(baby)

        # Add the babies to the set of individuals
        self.individuals.extend(babies)
        return babies

    def isUnfit(self, individual, epoch):
        individual.fitness = calculateFitnessWithinPopulationForEpoch(individual.genes, epoch, len(self.individuals))
        if (individual.fitness < random.uniform(0, 1) * epoch.kill_constant):
            return True

        return False

    # Kills those in the population who are ready to die and returns the fatalities
    def killThoseUnfitOrReadyToDie(self, current_year, epoch):
        survivors = []
        fatalities = []
        self.total_fitness = 0
        self.max_fitness = 0
        for individual in self.individuals:
            if (individual.isReadyToDie(current_year) or self.isUnfit(individual, epoch)):
                fatalities.append(individual)
            else:
                self.total_fitness = self.total_fitness + individual.fitness
                if (individual.fitness > self.max_fitness):
                    self.max_fitness = individual.fitness
                survivors.append(individual)
        self.individuals = survivors
        return fatalities

    def averageFitness(self):
        return self.total_fitness / len(self.individuals)
