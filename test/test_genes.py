from models.genes import GenesAs32BitArray
from models.config import Config
import random

def test_set_and_get_code():
    # Given a set of genes with random values
    config = Config()
    genes = GenesAs32BitArray(config)
    values = []
    for code in genes.codeIndexRange():
        values.append(random.randint(0, 1))
    for code in genes.codeIndexRange():
        genes.setCode(code, values[code])

    # When the values in the genes are tested, they match the original values
    for code in genes.codeIndexRange():
        assert values[code] == genes.getCode(code)

def test_new_genes_are_empty():
    # Given a set of genes with zero values
    config = Config()
    genes = GenesAs32BitArray(config)

    # When they are tested, they are all zero
    for code in genes.codeIndexRange():
        assert 0 == genes.getCode(code)

def test_mutated_genes_are_not_all_zero():
    # Given a set of genes with zero values that will likely mutate
    config = Config()
    genes = GenesAs32BitArray(config)
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

def test_mutation_can_be_disabled():
    # Given a set of genes with zero values that will not mutate
    config = Config()
    genes = GenesAs32BitArray(config)
    config.mutation_probability = 0

    # When they are mutated
    genes.mutate()

    # Then none have changed
    mutated_count = 0
    for code in genes.codeIndexRange():
        if (genes.getCode(code) == 1):
            mutated_count += 1
    assert mutated_count == 0

def when_the_genes_mutate_then_they_fall_into_the_float_range(config, genes):
    genes.mutate()

    float_genes = genes.toFloat()
    total = 0.0
    for gene in float_genes:
        total += gene
    assert total > config.float_lower * genes.number_of_integers
    assert total < config.float_upper * genes.number_of_integers


def test_genes_as_float_using_default_range():
    # Given a set of genes with zero values that will likely mutate
    config = Config()
    genes = GenesAs32BitArray(config)
    config.mutation_probability = config.mutation_probability * 10

    when_the_genes_mutate_then_they_fall_into_the_float_range(config, genes)

def test_configurable_float_range():
    # Given a set of genes with zero values that will likely mutate, with
    # a different float range
    config = Config()
    genes = GenesAs32BitArray(config)
    config.float_lower = 1.5
    config.float_upper = 4.5
    config.mutation_probability = config.mutation_probability * 10

    when_the_genes_mutate_then_they_fall_into_the_float_range(config, genes)

def are_genes_the_same(individual1, individual2):
    for code in individual1.codeIndexRange():
        if (individual1.getCode(code) != individual2.getCode(code)):
            return False
    return True

def create_father_different_to_mother(config, mother):
    mother.mutate()
    father = GenesAs32BitArray(config)
    father.mutate()
    while(are_genes_the_same(father, mother)):
        father.mutate()
    return father

def test_baby_is_not_identical_to_mother_or_father():
    # Given a mother with some mutated genes, a father with some mutated genes and a baby
    config = Config()
    config.mutation_probability = config.mutation_probability * 10
    mother = GenesAs32BitArray(config)
    father = create_father_different_to_mother(config, mother)
    baby = GenesAs32BitArray(config)
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

def test_baby_is_not_zero():
    # Given a mother, a father and a baby
    # Given a mother with some mutated genes, a father with some mutated genes and a baby
    config = Config()
    config.mutation_probability = config.mutation_probability * 10
    mother = GenesAs32BitArray(config)
    father = create_father_different_to_mother(config, mother)
    baby = GenesAs32BitArray(config)
    non_zero = False

    # When the baby inherits from the mother and father
    baby.inheritFrom(mother, father)

    # Then the baby's genes are non zero
    for code in baby.codeIndexRange():
        if (baby.getCode(code) != 0):
            non_zero = True
    assert non_zero == True

def test_baby_is_similar_to_mother_and_father():
    # Given a mother with some mutated genes, a father with some mutated genes and a baby
    config = Config()
    config.mutation_probability = config.mutation_probability * 10
    mother = GenesAs32BitArray(config)
    father = create_father_different_to_mother(config, mother)
    baby = GenesAs32BitArray(config)
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
