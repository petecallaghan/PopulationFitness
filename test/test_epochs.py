from models.epoch import Epoch
from models.epoch import Epochs
from models.config import Config

UNDEFINED_YEAR = -1

def test_create_epochs():
    # Given a set of epochs
    config = Config()
    epochs = Epochs()
    epochs.addNextEpoch(-50, 1)
    epochs.addNextEpoch(400, 1)
    epochs.addNextEpoch(550, 1)
    epochs.addNextEpoch(1086, 1)
    epochs.addNextEpoch(1300, 1)
    epochs.addNextEpoch(1348, 1)
    epochs.addNextEpoch(1400, 1)
    epochs.addNextEpoch(2016, 1)
    epochs.addNextEpoch(2068, 1)
    epochs.setFinalEpochYear(-50 + config.number_of_years - 1)

    # When we iterate over the epochs
    first_year = UNDEFINED_YEAR
    last_year = UNDEFINED_YEAR
    number_of_years = 0
    for epoch in epochs.epochs:
        for year in epoch.getRangeOfYears():
            if (first_year == UNDEFINED_YEAR):
                first_year = year
            last_year = year
            number_of_years = number_of_years + 1

    # Then we traverse all the years
    assert -50 == first_year
    assert -50 + config.number_of_years - 1 == last_year
    assert config.number_of_years == number_of_years

def test_epoch_fitness_factors():
    # Given a set of epochs
    epochs = Epochs()
    epochs.addNextEpoch(-50, 1)
    epochs.addNextEpoch(400, 2)
    epochs.addNextEpoch(550, 3)
    epochs.addNextEpoch(1086, 4)

    # When we iterate over the epochs we find the right fitness factor
    assert 1 == epochs.epochs[0].fitness_factor
    assert 2 == epochs.epochs[1].fitness_factor
    assert 3 == epochs.epochs[2].fitness_factor
    assert 4 == epochs.epochs[3].fitness_factor
