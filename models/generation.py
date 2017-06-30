from models.population import Population
from models.config import Config
from models.epoch import Epoch

import timeit

default_timer = timeit.default_timer # optimize

UNDEFINED_YEAR = -1

# The statistics for a generation
class GenerationStatistics:
    def __init__(self, epoch, year, population, number_born, number_killed, born_time, kill_time):
        self.number_born = number_born
        self.number_killed = number_killed
        self.population = population
        self.year = year
        self.epoch = epoch
        self.born_time = born_time
        self.kill_time = kill_time
        self.total_fitness = 0
        self.average_fitness = 0
        self.max_fitness = 0

    def bornElapsedInHundredths(self):
        return int(self.born_time * 100) / 100

    def killElapsedInHundredths(self):
        return int(self.kill_time * 100) / 100

# The generations recorded for a population
class Generations:
    def __init__(self, population):
        self.history = []
        self.population = population
        self.first_year = UNDEFINED_YEAR

    # Iterates over all the years in the epochs and produces the generations
    def createForAllEpochs(self, epochs):
        nextYear = self.createForYear # optimize

        for epoch in epochs.epochs:
            for year in epoch.getRangeOfYears():
                nextYear(year, epoch)

    def createForYear(self, year, epoch):
        if (self.first_year == UNDEFINED_YEAR):
            self.first_year = year
            # Add an initial population
            self.population.addNewIndividuals(year)

        start_time = default_timer()
        fatalities = self.population.killThoseUnfitOrReadyToDie(year, epoch)
        kill_elapsed = default_timer() - start_time

        start_time = default_timer()
        babies = self.population.addNewGeneration(year)
        born_elapsed = default_timer() - start_time

        return self.addHistory(epoch, year, len(babies), fatalities, born_elapsed, kill_elapsed)

    def addHistory(self, epoch, year, number_born, number_killed, born_time, kill_time):
        generation = GenerationStatistics(epoch, year, len(self.population.individuals), number_born, number_killed, born_time, kill_time)
        generation.total_fitness = self.population.total_fitness
        generation.average_fitness = self.population.averageFitness()
        generation.max_fitness = self.population.max_fitness

        self.history.append(generation)
        print('Year', generation.year, 'Pop', generation.population, 'Born', generation.number_born, 'in', generation.bornElapsedInHundredths(), 's Killed', generation.number_killed, 'in', generation.killElapsedInHundredths(), 's')
        return generation
