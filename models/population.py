import random
from models.individual import Individual
from models.fitness import calculateFitnessWithinPopulationForEpoch

uniform = random.uniform # optimize

class Population:
    def __init__(self, config):
        self.config = config
        self.individuals = []

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

        for pairIndex in range(0, len(self.individuals)-1, 2):
            # Selects a pair and breeds if they are both of breeding age
            father = individuals[pairIndex]
            mother = individuals[pairIndex + 1]
            pairCanBreed = (uniform(0, 1) < probability_of_breeding)
            if (father.canBreed(current_year) and mother.canBreed(current_year) and pairCanBreed):
                baby = Individual(config, current_year)
                baby.inheritFromParentsAndMutate(father, mother)
                append(baby)

        # Add the babies to the set of individuals
        self.individuals.extend(babies)
        return babies

    def isUnfit(self, individual, epoch):
        individual.fitness = calculateFitnessWithinPopulationForEpoch(individual.genes, epoch, len(self.individuals))
        if (individual.fitness < uniform(0, 1) * epoch.kill_constant):
            return True

        return False

    # Kills those in the population who are ready to die and returns the fatalities
    def killThoseUnfitOrReadyToDie(self, current_year, epoch):
        survivors = []
        fatalities = []

        die = fatalities.append # optimize
        survive = survivors.append # optimize

        self.total_fitness = 0
        self.max_fitness = 0
        for individual in self.individuals:
            if (individual.isReadyToDie(current_year) or self.isUnfit(individual, epoch)):
                die(individual)
            else:
                self.total_fitness = self.total_fitness + individual.fitness
                if (individual.fitness > self.max_fitness):
                    self.max_fitness = individual.fitness
                survive(individual)
        self.individuals = survivors
        return fatalities

    def averageFitness(self):
        return self.total_fitness / len(self.individuals)
