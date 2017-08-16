from multiprocessing import Process

from models.generation import Generations
from models.config import Config

#https://docs.python.org/3.5/library/multiprocessing.html
#https://docs.python.org/3.5/library/multiprocessing.html#multiprocessing-examples

def createParallelForAllEpochs(config, epochs, number_in_parallel):
    parallel_config = Config()
    parallel_config.copy(config)

    parallel_config.initial_population_size = config.initial_population_size / number_in_parallel

    for epoch in epochs.epochs:
        epoch.environment_capacity = epoch.environment_capacity / number_in_parallel
