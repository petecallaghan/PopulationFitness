from models.genes import GenesAs32BitArray
from models.config import Config
import random

def test_set_and_get_code():
    # setup
    config = Config()
    genes = GenesAs32BitArray(config)
    values = []
    for code in range(0, genes.number_of_codes):
        values.append(random.randint(0, 1))

    # set values
    for code in range(0, genes.number_of_codes):
        genes.setCode(code, values[code])

    # test values
    for code in range(0, genes.number_of_codes):
        assert values[code] == genes.getCode(code)

def test_new_genes_are_empty():
    # setup
    config = Config()
    genes = GenesAs32BitArray(config)

    # test values
    for code in range(0, genes.number_of_codes):
        assert 0 == genes.getCode(code)

def test_mutated_genes_are_not_all_zero():
    # setup
    config = Config()
    genes = GenesAs32BitArray(config)

    # make mutation highly likely
    config.mutation_probability = config.mutation_probability * 10
    genes.mutate()

    # check that some are non zero
    mutated_count = 0

    for code in range(0, genes.number_of_codes):
        if (genes.getCode(code) == 1):
            mutated_count += 1

    assert mutated_count > 0
    assert mutated_count <= 2.0 * genes.number_of_codes * config.mutation_probability

def test_mutation_can_be_disabled():
    # setup
    config = Config()
    genes = GenesAs32BitArray(config)
    config.mutation_probability = 0

    genes.mutate()

    # check that some are non zero
    mutated_count = 0

    for code in range(0, genes.number_of_codes):
        if (genes.getCode(code) == 1):
            mutated_count += 1

    assert mutated_count == 0

def test_genes_as_float_using_default_range():
    # setup
    config = Config()
    genes = GenesAs32BitArray(config)

    # make mutation highly likely
    config.mutation_probability = config.mutation_probability * 10
    genes.mutate()

    float_genes = genes.toFloat()
    total = 0.0

    for gene in float_genes:
        total += gene

    assert total > config.float_lower * genes.number_of_32_bit_integers
    assert total < config.float_upper * genes.number_of_32_bit_integers

def test_configural_float_range():
    # setup
    config = Config()
    genes = GenesAs32BitArray(config)
    config.float_lower = 1.5
    config.float_upper = 4.5

    # make mutation highly likely
    config.mutation_probability = config.mutation_probability * 10
    genes.mutate()

    float_genes = genes.toFloat()
    total = 0.0

    for gene in float_genes:
        total += gene

    assert total > config.float_lower * genes.number_of_32_bit_integers
    assert total < config.float_upper * genes.number_of_32_bit_integers
