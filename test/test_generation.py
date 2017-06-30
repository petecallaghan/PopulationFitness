from models.population import Population
from models.config import Config
from models.epoch import Epochs
from models.generation import Generations

def test_produce_generation_history(capsys):
    # Given a standard configuration ...
    config = Config()
    population = Population(config)
    generations = Generations(population)

    # ... with some epochs
    epochs = Epochs()
    epochs.addNextEpoch(-50, 1, 1, 4000)
    epochs.setFinalEpochYear(-40)

    # When the simulation runs through the epochs
    with capsys.disabled():
        generations.createForAllEpochs(epochs)

    # Then we get a history of the simulation
    assert 11 == len(generations.history)
