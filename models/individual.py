from models.genes import GenesAs32BitArray

class Individual:
    def __init__(self, config, birth_year):
        self.config = config
        self.birth_year = birth_year
        self.genes = GenesAs32BitArray(config)
        self.genes.buildFromRandom()

    def age(self, current_year):
        return current_year - self.birth_year

    def isReadyToDie(self, current_year):
        return self.age(current_year) >= self.config.max_age

    def canBreed(self, current_year):
        age = self.age(current_year)
        return (age >= self.config.min_breeding_age and age <= self.config.max_breeding_age)

    def inheritFromParentsAndMutate(self, father, mother):
        self.genes.inheritFrom(father.genes, mother.genes)
        self.genes.mutate()

    def isUnfit(self, current_year): # Applies the fitness function for the year
        # TO DO!
        return False

    def isUnfitOrReadyToDie(self, current_year):
        return self.isUnfit(current_year) or self.isReadyToDie(current_year)
