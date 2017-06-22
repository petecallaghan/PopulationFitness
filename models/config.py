ZERO = 0.0
PI = 3.14159

class Config:
    def __init__(self):
        # No genes per individual
        self.number_of_genes = 4

        # Number of codes per gene
        self.size_of_each_gene = 10

        # Probability that an individual gene code will mutate
        self.mutation_probability = 1.0 / (self.size_of_each_gene * self.number_of_genes)

        self.max_age = 50
        self.max_year = 2150
        self.max_breeding_age = 35
        self.min_breeding_age = 16
        self.float_lower = ZERO
        self.float_upper = PI

        self.initial_population_size = 4000

        # Probability that a pair will produce offspring in a year
        self.probability_of_breeding = 0.6
