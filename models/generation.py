from models.population import Population
from models.config import Config
from models.epoch import Epoch

import timeit

UNDEFINED_YEAR = -1

class GenerationHistory:
    def __init__(self, epoch, year, population, number_born, number_killed, born_time, kill_time):
        self.number_born = number_born
        self.number_killed = number_killed
        self.population = population
        self.year = year
        self.epoch = epoch
        self.born_time = born_time
        self.kill_time = kill_time

    def bornElapsedInHundredths(self):
        return int(self.born_time * 100) / 100

    def killElapsedInHundredths(self):
        return int(self.kill_time * 100) / 100

class Generations:
    def __init__(self, population):
        self.history = []
        self.population = population
        self.first_year = UNDEFINED_YEAR

    def addNextGeneration(self, year, epoch):
        if (self.first_year == UNDEFINED_YEAR):
            self.first_year = year
            # Add an initial population
            self.population.addNewIndividuals(year)

        start_time = timeit.default_timer()
        fatalities = self.population.killThoseUnfitOrReadyToDie(year, epoch)
        kill_elapsed = timeit.default_timer() - start_time

        start_time = timeit.default_timer()
        babies = self.population.addNewGeneration(year)
        born_elapsed = timeit.default_timer() - start_time

        return self.addHistory(epoch, year, len(self.population.individuals), len(babies), len(fatalities), born_elapsed, kill_elapsed)

    def addHistory(self, epoch, year, population, number_born, number_killed, born_time, kill_time):
        generation = GenerationHistory(epoch, year, population, number_born, number_killed, born_time, kill_time)
        self.history.append(generation)
        print('Year', generation.year, 'Pop', generation.population, 'Born', generation.number_born, 'in', generation.bornElapsedInHundredths(), 's Killed', generation.number_killed, 'in', generation.killElapsedInHundredths(), 's')
        return generation
