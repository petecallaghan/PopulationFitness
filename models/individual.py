from models.genes import GenesAs8BitArray

class Individual:
    def __init__(self, config, birth_year):
        self.config = config
        self.birth_year = birth_year
        self.genes = GenesAs8BitArray(config)

    def age(self, current_year):
        return current_year - self.birth_year

    def isReadyToDie(self, current_year):
        return self.age(current_year) >= self.config.max_age

    def canBreed(self, current_year):
        age = self.age(current_year)
        config = self.config # optimize
        return (age >= config.min_breeding_age and age <= config.max_breeding_age)

    def inheritFromParentsAndMutate(self, father, mother):
        self.genes.inheritFrom(father.genes, mother.genes)
        self.genes.mutate()
