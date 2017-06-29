from models.genes import GenesAs8BitArray
from models.genes import GenesAs32BitArray
from models.config import Config
import random

def check_set_and_get_code(genes):
    # Given a set of genes with random values
    genes.buildEmpty()
    values = []
    for code in genes.codeIndexRange():
        values.append(random.randint(0, 1))
    for code in genes.codeIndexRange():
        genes.setCode(code, values[code])

    # When the values in the genes are tested, they match the original values
    for code in genes.codeIndexRange():
        assert values[code] == genes.getCode(code)

def test_set_and_get_code():
    config = Config()
    check_set_and_get_code(GenesAs8BitArray(config))
    check_set_and_get_code(GenesAs32BitArray(config))

def check_genes_are_empty(genes):
    # Given a set of genes with zero values
    genes.buildEmpty()

    # When they are tested, they are all zero
    assert True == genes.areEmpty()

def test_new_genes_are_empty():
    config = Config()
    check_genes_are_empty(GenesAs8BitArray(config))
    check_genes_are_empty(GenesAs32BitArray(config))

def check_mutated_genes_are_not_all_zero(genes, config):
    # Given a set of genes with zero values that will likely mutate
    genes.buildEmpty()
    config.mutation_probability = config.mutation_probability * 10

    # When they are mutated
    genes.mutate()

    # Then the number mutated falls inside the range of mutation probability
    mutated_count = 0
    for code in genes.codeIndexRange():
        if (genes.getCode(code) == 1):
            mutated_count += 1
    assert mutated_count > 0
    assert mutated_count <= 2.0 * genes.number_of_codes * config.mutation_probability

def test_mutated_genes_are_not_all_zero():
    config = Config()
    check_mutated_genes_are_not_all_zero(GenesAs8BitArray(config), config)
    check_mutated_genes_are_not_all_zero(GenesAs32BitArray(config), config)

def check_mutation_can_be_disabled(genes, config):
    # Given a set of genes with zero values that will not mutate
    genes.buildEmpty()
    config.mutation_probability = 0

    # When they are mutated
    genes.mutate()

    # Then none have changed
    mutated_count = 0
    for code in genes.codeIndexRange():
        if (genes.getCode(code) == 1):
            mutated_count += 1
    assert mutated_count == 0

def test_mutation_can_be_disabled():
    config = Config()
    check_mutation_can_be_disabled(GenesAs8BitArray(config), config)
    check_mutation_can_be_disabled(GenesAs32BitArray(config), config)

def then_they_fall_into_the_float_range(config, genes):
    genes.buildFromRandom()

    fitness = genes.fitness(1)

    assert 0.0 <= fitness
    assert 1.0 >= fitness

def test_genes_as_float_using_default_range():
    # Given a set of genes with non zero values
    config = Config()

    then_they_fall_into_the_float_range(config, GenesAs8BitArray(config))
    then_they_fall_into_the_float_range(config, GenesAs32BitArray(config))

def test_genes__with_large_bit_coding_as_float():
    # Given a set of genes with non zero values
    config = Config()
    config.number_of_genes = 111
    config.size_of_each_gene = 111

    then_they_fall_into_the_float_range(config, GenesAs8BitArray(config))
    then_they_fall_into_the_float_range(config, GenesAs32BitArray(config))

def test_configurable_float_range():
    # Given a set of genes with non zero values and a different float range
    config = Config()
    config.float_lower = 1.5
    config.float_upper = 4.5

    then_they_fall_into_the_float_range(config, GenesAs8BitArray(config))
    then_they_fall_into_the_float_range(config, GenesAs32BitArray(config))

def are_genes_the_same(individual1, individual2):
    for code in individual1.codeIndexRange():
        if (individual1.getCode(code) != individual2.getCode(code)):
            return False
    return True

def create_father_different_to_mother(config, mother, genesBuilder):
    father = genesBuilder(config)
    father.buildFromRandom()
    while(are_genes_the_same(father, mother)):
        father.mutate()
    return father

def check_baby_is_not_identical_to_mother_or_father(genesBuilder):
    # Given a mother with some mutated genes, a father with some mutated genes and a baby
    config = Config()
    config.mutation_probability = config.mutation_probability * 10
    mother = genesBuilder(config)
    mother.buildFromRandom()
    father = create_father_different_to_mother(config, mother, genesBuilder)
    baby = genesBuilder(config)
    different_to_mother = False
    different_to_father = False

    # When the baby inherits from the mother and father
    baby.inheritFrom(mother, father)

    # Then the baby's genes are different to both
    for code in baby.codeIndexRange():
        if (baby.getCode(code) != father.getCode(code)):
            different_to_father = True
        if (baby.getCode(code) != mother.getCode(code)):
            different_to_mother = True
    assert different_to_father == True
    assert different_to_mother == True

def test_baby_is_not_identical_to_mother_or_father():
    check_baby_is_not_identical_to_mother_or_father(GenesAs8BitArray)
    check_baby_is_not_identical_to_mother_or_father(GenesAs32BitArray)

def check_baby_is_not_zero(genesBuilder):
    # Given a mother, a father and a baby
    # Given a mother with some mutated genes, a father with some mutated genes and a baby
    config = Config()
    config.mutation_probability = config.mutation_probability * 10
    mother = genesBuilder(config)
    mother.buildFromRandom()
    father = create_father_different_to_mother(config, mother, genesBuilder)
    baby = genesBuilder(config)
    non_zero = False

    # When the baby inherits from the mother and father
    baby.inheritFrom(mother, father)

    # Then the baby's genes are non zero
    for code in baby.codeIndexRange():
        if (baby.getCode(code) != 0):
            non_zero = True
    assert non_zero == True

def test_baby_is_not_zero():
    check_baby_is_not_zero(GenesAs8BitArray)
    check_baby_is_not_zero(GenesAs32BitArray)

def check_baby_is_similar_to_mother_and_father(genesBuilder):
    # Given a mother with some mutated genes, a father with some mutated genes and a baby
    config = Config()
    config.mutation_probability = config.mutation_probability * 10
    genesBuilder = genesBuilder
    mother = genesBuilder(config)
    mother.buildFromRandom()
    father = create_father_different_to_mother(config, mother, genesBuilder)
    baby = genesBuilder(config)
    similar_to_mother = False
    similar_to_father = False

    # When the baby inherits from the mother and father
    baby.inheritFrom(mother, father)

    # Then the baby's genes has some similarity to both
    for code in baby.codeIndexRange():
        if (baby.getCode(code) == father.getCode(code)):
            similar_to_father = True
        if (baby.getCode(code) == mother.getCode(code)):
            similar_to_mother = True
    assert similar_to_mother == True
    assert similar_to_father == True

def test_baby_is_similar_to_mother_and_father():
    check_baby_is_similar_to_mother_and_father(GenesAs8BitArray)
    check_baby_is_similar_to_mother_and_father(GenesAs32BitArray)
