class Individual:
    def __init__(self, config, birth_year):
        self.config = config
        self.birth_year = birth_year
        self.genes = Genes(config)

    def age(self, current_year):
        return current_year - self.birth_year

    def readyToDie(self, current_year):
        return self.age(current_year) >= self.config.max_age
