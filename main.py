from models.population import Population
from models.config import Config
from models.epoch import Epochs
from models.generation import Generations

DEFAULT_KILL_CONSTANT = 1.0255

def main():
    config = Config()
    population = Population(config)
    generations = Generations(population)

    epochs = Epochs()
    epochs.addNextEpoch(-50, 1, 1, 4000)
    epochs.addNextEpoch(400, 1.5, DEFAULT_KILL_CONSTANT)
    epochs.setFinalEpochYear(450)
    #epochs.addNextEpoch(550, 1, DEFAULT_KILL_CONSTANT, 2000)
    #epochs.addNextEpoch(1086, 1.1, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(1300, 1.1, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(1348, 3, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(1400, 1, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(2016, 3, DEFAULT_KILL_CONSTANT)
    #epochs.addNextEpoch(2068, 1, DEFAULT_KILL_CONSTANT)
    #epochs.setFinalEpochYear(-50 + config.number_of_years - 1)

    generations.createForAllEpochs(epochs)

if __name__ == "__main__":
    main()
