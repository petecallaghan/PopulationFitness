from models.individual import Individual

class Population:
    def __init__(self, config):
        self.config = config

    def createNew(birth_year): # Builds a new population
        self.individuals = []
        for person in range(0, config.initial_population_size):
            self.individuals.append(Individual(config, birth_year))
