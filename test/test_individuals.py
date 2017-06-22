from models.individual import Individual
from models.config import Config

def test_old_guy_ready_to_die():
    # Given an old individual
    config = Config()
    birth_year = 1964
    current_year = birth_year + config.max_age + 10
    individual = Individual(config, birth_year)

    # Then they are ready to die
    assert True == individual.isReadyToDie(current_year)

def test_young_guy_not_ready_to_die():
    # Given a young individual
    config = Config()
    birth_year = 1964
    current_year = birth_year + config.max_age - 10
    individual = Individual(config, birth_year)

    # Then they are ready to die
    assert False == individual.isReadyToDie(current_year)

def test_can_breed():
    # Given a breeding age individual
    config = Config()
    birth_year = 1964
    current_year = birth_year + config.min_breeding_age + 1
    individual = Individual(config, birth_year)

    # Then they can breed
    assert True == individual.canBreed(current_year)

def test_youngster_cannot_breed():
    # Given a below breeding age individual
    config = Config()
    birth_year = 1964
    current_year = birth_year + config.min_breeding_age - 1
    individual = Individual(config, birth_year)

    # Then they cannot breed
    assert False == individual.canBreed(current_year)

def test_oldgster_cannot_breed():
    # Given an above breeding age individual
    config = Config()
    birth_year = 1964
    current_year = birth_year + config.max_breeding_age + 1
    individual = Individual(config, birth_year)

    # Then they cannot breed
    assert False == individual.canBreed(current_year)
