import array, random

DIVIDE_BY_32 = 5
MASK_UNITS_FOR_32_BITS = 31
MAX_32_BIT_VALUE = 0x7FFFFFFF

# Represents a set of genes as a bit array, using 32 bit integers to contain
# 32 bit fragments of the genetic  code.

# Creates a zero-filled set of genes
class GenesAs32BitArray:
    def __init__(self, config):
        self.config = config
        self.number_of_codes = config.number_of_genes * config.size_of_each_gene
        self.max_gene_32_bit_integer_value = MAX_32_BIT_VALUE
        self.number_of_32_bit_integers = self.number_of_codes >> DIVIDE_BY_32

        if (self.number_of_32_bit_integers & MASK_UNITS_FOR_32_BITS): # if bit size is not divisible by 32, add one
            self.number_of_32_bit_integers += 1

        self.genetic_code = array.array('I')

        # fill the genes with zero
        self.genetic_code.extend((0,) * self.number_of_32_bit_integers)

    def getCode(self, index): # returns the code at the index in the gene
        code_fragment_index = index >> DIVIDE_BY_32
        bit_offset_of_code_in_gene = index & MASK_UNITS_FOR_32_BITS
        code_bit_mask = 1 << bit_offset_of_code_in_gene
        if (self.genetic_code[code_fragment_index] & code_bit_mask):
            return 1
        return 0

    def setCode(self, index, value):
        code_fragment_index = index >> DIVIDE_BY_32
        bit_offset_of_code_in_gene = index & MASK_UNITS_FOR_32_BITS
        if (value == 0):
            code_bit_mask = ~(1 << bit_offset_of_code_in_gene)
            self.genetic_code[code_fragment_index] &= code_bit_mask
        else:
            code_bit_mask = 1 << bit_offset_of_code_in_gene
            self.genetic_code[code_fragment_index] |= code_bit_mask

    def toggleCode(self, index):
        code_fragment_index = index >> DIVIDE_BY_32
        bit_offset_of_code_in_gene = index & MASK_UNITS_FOR_32_BITS
        code_bit_mask = 1 << bit_offset_of_code_in_gene
        self.genetic_code[code_fragment_index] ^= code_bit_mask

    def randomlyMutateCode(self, index):
        if (random.uniform(0, 1) < self.config.mutation_probability):
            self.toggleCode(index)

    def linearFloatInterpolation(self, code_fragment):
        return self.config.float_lower + (code_fragment * (self.config.float_upper - self.config.float_lower)) / self.max_gene_32_bit_integer_value

    def toFloat(self): # represents genetic code as real numbers
        float_genes = []
        for code_fragment in self.genetic_code:
            float_genes.append(self.linearFloatInterpolation(code_fragment))
        return float_genes

    def mutate(self):
        for gene_code in range(0, self.number_of_codes):
            self.randomlyMutateCode(gene_code)
