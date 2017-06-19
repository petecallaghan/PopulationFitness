ZERO = 0.0
PI = 3.14159

class Config:
    def __init__(self):
        self.number_of_genes = 4
        self.size_of_each_gene = 10
        self.max_age = 50
        self.max_year = 2150
        self.mutation_probability = 1.0 / (self.size_of_each_gene * self.number_of_genes)
        self.max_breeding_age = 35
        self.min_breading_age = 16
        self.float_lower = ZERO
        self.float_upper = PI
