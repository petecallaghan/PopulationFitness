from models.genes8bitarray import *
from models.genes32bitarray import *

class Individual:
    def __init__(self, config, birth_year):
        self.config = config
        self.birth_year = birth_year
        self.genes = GenesAs32BitArray(config)
        self.fitness = 0

    def age(self, current_year):
        return current_year - self.birth_year

    def isReadyToDie(self, current_year):
        return self.age(current_year) >= self.config.max_age

    def canBreed(self, current_year):
        age = self.age(current_year)
        config = self.config # optimize
        return (age >= config.min_breeding_age and age <= config.max_breeding_age)

    def inheritFromParentsAndMutate(self, mother, father):
        self.genes.inheritFrom(mother.genes, father.genes)
        self.genes.mutate()
