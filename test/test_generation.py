from models.population import Population
from models.config import Config
from models.epoch import Epochs
from models.generation import Generations
import timeit

DEFAULT_KILL_CONSTANT = 1.0255
UNDEFINED_YEAR = -1

def test_produce_generation_history(capsys):
    # Given a standard configuration ...
    config = Config()
    history = Generations()
    population = Population(config)

    # ... with default epochs
    epochs = Epochs()
    epochs.addNextEpoch(-50, 1, 1, 4000)
    epochs.addNextEpoch(400, 1.5, DEFAULT_KILL_CONSTANT)
    epochs.setFinalEpochYear(550)
    #epochs.addNextEpoch(550, 1, DEFAULT_KILL_CONSTANT, 2000)
    #epochs.addNextEpoch(1086, 1.1, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(1300, 1.1, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(1348, 3, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(1400, 1, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(2016, 3, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(2068, 1, DEFAULT_KILL_CONSTANT)
    #epochs.setFinalEpochYear(-50 + config.number_of_years - 1)

    # When the simulation runs through the epochs
    first_year = UNDEFINED_YEAR
    for epoch in epochs.epochs:
        for year in epoch.getRangeOfYears():
            if (first_year == UNDEFINED_YEAR):
                first_year = year
                # Add an initial population
                population.addNewIndividuals(year)

            start_time = timeit.default_timer()
            fatalities = population.killThoseUnfitOrReadyToDie(year, epoch)
            kill_elapsed = int((timeit.default_timer() - start_time) * 100) / 100

            start_time = timeit.default_timer()
            babies = population.addNewGeneration(year)
            born_elapsed = int((timeit.default_timer() - start_time) * 100) / 100

            #history.add(epoch, year, len(babies), len(fatalities))

            with capsys.disabled():
                print('Year', year, 'Pop', len(population.individuals), 'Born', len(babies), 'in', born_elapsed, 's Killed', len(fatalities), 'in', kill_elapsed, 's')


    # Then we get a history of the simulation
