import array, random

DIVIDE_BY_32 = 5
MASK_UNITS_FOR_32_BITS = 31
MAX_32_BIT_VALUE = 0xFFFFFFFF
NUMBER_OF_BITS_IN_A_WORD = 32

uniform = random.uniform # optimize

# Represents a set of genes as a bit array, using 32 bit integers to contain
# 32 bit fragments of the genetic  code.

# Creates a zero-filled set of genes
class GenesAs32BitArray:
    def __init__(self, config):
        self.config = config

        # Total code size is size of each gene x number of genes
        self.number_of_codes = config.number_of_genes * config.size_of_each_gene
        self.max_code_index = self.number_of_codes - 1

        # The entire code is mapped to bits stored across a number of 32 bit integers
        # Each integer is a code fragment
        self.max_code_fragment_value = MAX_32_BIT_VALUE
        self.max_last_fragment_value = MAX_32_BIT_VALUE
        self.number_of_code_fragments = self.number_of_codes >> DIVIDE_BY_32
        number_of_bits_in_last_word = self.number_of_codes % NUMBER_OF_BITS_IN_A_WORD
        if (number_of_bits_in_last_word > 0): # if bit size is not divisible by 32, add one
            self.number_of_code_fragments += 1
            self.max_last_fragment_value = MAX_32_BIT_VALUE >> (NUMBER_OF_BITS_IN_A_WORD - number_of_bits_in_last_word)

        # The integers so which the code maps is currently the code fragments
        # (Not the individual genes, in contrast to the original BASIC implementation)
        # This may need to change
        self.number_of_integers = self.number_of_code_fragments
        self.genetic_code = array.array('I')

    def buildEmpty(self):
        self.genetic_code.extend((0,) * self.number_of_code_fragments)

    def buildFromRandom(self): # Generate an initial code randomly
        setCode = self.setCode # optimize

        self.buildEmpty()
        for index in self.codeIndexRange():
            code = 0
            if (uniform(0, 1) > 0.5):
                code = 1
            setCode(index, code)

    def codeIndexRange(self): # Returns the code index range
        return range(0, self.number_of_codes)

    def rejectInvalidIndex(self, index):
        if (index < 0):
            raise ValueError("Gene code index of less than zero")
        if (index > self.max_code_index):
            raise ValueError("Gene code index greater than max")

    def getCode(self, index): # returns the code at the index in the gene as either 1 or 0
        self.rejectInvalidIndex(index)

        code_fragment_index = index >> DIVIDE_BY_32
        bit_offset_of_code_in_gene = index & MASK_UNITS_FOR_32_BITS
        code_bit_mask = 1 << bit_offset_of_code_in_gene
        if (self.genetic_code[code_fragment_index] & code_bit_mask):
            return 1
        return 0

    def setCode(self, index, boolean_value): # sets the code at the index as either 1 or 0
        self.rejectInvalidIndex(index)

        code_fragment_index = index >> DIVIDE_BY_32
        bit_offset_of_code_in_gene = index & MASK_UNITS_FOR_32_BITS
        if (boolean_value == 0):
            code_bit_mask = ~(1 << bit_offset_of_code_in_gene)
            self.genetic_code[code_fragment_index] &= code_bit_mask
        else:
            code_bit_mask = 1 << bit_offset_of_code_in_gene
            self.genetic_code[code_fragment_index] |= code_bit_mask

    def toggleCode(self, index): # Toggles the code at the index between 1 and 0
        self.rejectInvalidIndex(index)

        code_fragment_index = index >> DIVIDE_BY_32
        bit_offset_of_code_in_gene = index & MASK_UNITS_FOR_32_BITS
        code_bit_mask = 1 << bit_offset_of_code_in_gene
        self.genetic_code[code_fragment_index] ^= code_bit_mask

    def randomlyMutateCode(self, index): # Randomly toogles the code at the index, or leaves it unchanged
        if (uniform(0, 1) < self.config.mutation_probability):
            self.toggleCode(index)

    def linearFloatInterpolation(self, code_fragment, max_code_fragment_value):
        return self.config.float_lower + (code_fragment * (self.config.float_upper - self.config.float_lower)) / max_code_fragment_value

    def toFloat(self): # represents genetic code as real numbers
        # Differs from BASIC implementation by mapping the code fragments, not
        # the individual genes

        float_genes = []

        append = float_genes.append # optimize
        interpolate = self.linearFloatInterpolation # optimize
        max_code_fragment_value = self.max_code_fragment_value #optimize

        for code_fragment in self.genetic_code[:-1]: # All but the last in the list
            append(interpolate(code_fragment, max_code_fragment_value))
        #Add the last in the list
        append(interpolate(self.genetic_code[-1], self.max_last_fragment_value))
        return float_genes

    def mutate(self):
        mutate = self.randomlyMutateCode # optimize
        for gene_code in self.codeIndexRange():
            mutate(gene_code)

    def inheritFrom(self, mother, father): # Copies a random set from mother and father
        # Randomly picks the code index that crosses over from mother to father
        cross_over_index = 1 + int(uniform(0, 1) * (self.max_code_index - 1))

        # First set of codes from mother
        self.genetic_code.extend(mother.genetic_code)

        setCode = self.setCode # optimize
        getCode = father.getCode #optimize

        # Remaining codes from father
        for code in range(cross_over_index, self.number_of_codes):
            setCode(code, getCode(code))

    def areEmpty(self): # Returns true if the genes are all zero
        getCode = self.getCode #optimize

        for code in self.codeIndexRange():
            if (1 == getCode(code)):
                return False
        return True
