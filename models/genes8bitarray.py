import array, random
from bitarray import bitarray
from math import sin
from functools import reduce

getRandom = random.uniform # optimize

DIVIDE_BY_8 = 3
MAX_8_BIT_VALUE = 0xFF

class GenesAs8BitArray:
    def __init__(self, config):
        self.config = config

        # Total code size is size of each gene x number of genes
        self.number_of_codes = config.number_of_genes * config.size_of_each_gene
        self.max_code_index = self.number_of_codes - 1

        # The entire code is mapped to bits stored across a number of 8 bit integers
        # Each integer is a code fragment
        self.max_code_fragment_value = MAX_8_BIT_VALUE
        self.number_of_code_fragments = self.number_of_codes >> DIVIDE_BY_8
        number_of_bits_in_last_word = self.number_of_codes % 8
        if (number_of_bits_in_last_word > 0): # if bit size is not divisible by 8, add one
            self.number_of_code_fragments += 1

        # The integers so which the code maps is currently the code fragments
        # (Not the individual genes, in contrast to the original BASIC implementation)
        # This may need to change
        self.number_of_integers = self.number_of_code_fragments

    def buildEmpty(self):
        self.genetic_code = bitarray(self.number_of_codes)
        self.genetic_code.setall(0)

    def getCode(self, index):
        return self.genetic_code[index]

    def setCode(self, index, value):
        self.genetic_code[index] = value

    def buildFromRandom(self): # Generate an initial code randomly
        self.buildEmpty()

        genetic_code = self.genetic_code # optimize

        for index in self.codeIndexRange():
            if (getRandom(0, 1) > 0.5):
                genetic_code[index] = 1

    def codeIndexRange(self): # Returns the code index range
        return range(0, self.number_of_codes)

    def mutate(self):
        probability = self.config.mutation_probability # optimize
        genetic_code = self.genetic_code # optimize

        for index in self.codeIndexRange():
            if (getRandom(0, 1) < probability):
                genetic_code[index] = ~genetic_code[index]

    def inheritFrom(self, mother, father): # Copies a random set from mother and father
        # Randomly picks the code index that crosses over from mother to father
        cross_over_index = 1 + int(getRandom(0, 1) * (self.max_code_index - 1))

        # First set of codes from mother
        self.genetic_code = mother.genetic_code.copy()

        genetic_code = self.genetic_code # optimize
        father = father.genetic_code # optimize

        # Remaining codes from father
        for index in range(cross_over_index, self.number_of_codes):
            genetic_code[index] = father[index]

    def areEmpty(self): # Returns true if the genes are all zero
        return False if self.genetic_code.any() else True

    def fitness(self, fitness_factor):
        lower = self.config.float_lower
        ratio = (self.config.float_upper - lower) / self.max_code_fragment_value

        return max(0, reduce(lambda x, y : x * sin(lower + y * ratio)**fitness_factor, self.genetic_code.tobytes(), 1))
