from models.population import Population
from models.config import Config
from models.epoch import Epoch
from models.epoch import Epochs


INITIAL_POPULATION_SIZE = 30
BIRTH_YEAR = 1964

def test_create_new_non_empty_population():
    # Given a small population
    config = Config()
    config.initial_population_size = INITIAL_POPULATION_SIZE
    population = Population(config)
    birth_year = BIRTH_YEAR

    # When the population is created from scratch
    population.addNewIndividuals(birth_year)

    # Then it has the right number of individuals for the birth year
    # and each individual has a set of genes
    assert config.initial_population_size == len(population.individuals)
    for individual in population.individuals:
        assert birth_year == individual.birth_year
        assert False == individual.genes.areEmpty()

def test_kill_off_oldies():
    # Given a population that is a mixture of oldies and babies, where fitness is ignored
    config = Config()
    epochs = Epochs()
    epochs.addNextEpoch(-50, 1, 1)
    # Ignore fitness
    epochs.epochs[0].enable_fitness = False
    config.initial_population_size = INITIAL_POPULATION_SIZE
    population = Population(config)
    birth_year = BIRTH_YEAR
    population.addNewIndividuals(birth_year)
    current_year = birth_year + config.max_age
    population.addNewIndividuals(current_year)

    # When we kill off the elderly
    assert 2 * config.initial_population_size == len(population.individuals)
    fatalities = population.killThoseUnfitOrReadyToDie(current_year, epochs.epochs[0])

    # Just the babies remain and the elderly were killed
    assert config.initial_population_size == len(population.individuals)
    assert config.initial_population_size == len(fatalities)
    for individual in fatalities:
        assert config.max_age <= individual.age(current_year)
    for individual in population.individuals:
        assert 0 == individual.age(current_year)

def test_create_a_new_generation_from_the_current_one():
    # Given a population that is a mix of ages...
    config = Config()
    config.initial_population_size = INITIAL_POPULATION_SIZE
    population = Population(config)
    birth_year = BIRTH_YEAR
    current_year = birth_year + config.max_breeding_age
    breeding_birth_year = current_year - config.min_breeding_age
    # .. some who will be too old to breed
    population.addNewIndividuals(birth_year)
    # .. some who will be too young to breed
    population.addNewIndividuals(current_year)
    # .. some who can breed
    population.addNewIndividuals(breeding_birth_year)

    # When we create a new population
    babies = population.addNewGeneration(current_year)

    # The right number of babies are produced and the babies are added
    assert len(babies) >= (config.initial_population_size * config.probability_of_breeding / 2) - config.initial_population_size / 5
    assert len(babies) <= (config.initial_population_size * config.probability_of_breeding) + config.initial_population_size / 5
    assert (config.initial_population_size * 3) + len(babies) == len(population.individuals)
