import array

UNDEFINED_YEAR = -1
LAST_EPOCH = -1 # the -1 index is the last in the list
UNLIMITED_CAPACITY = 0

class Epoch:
    def __init__(self, start_year, fitness_factor, kill_constant, environment_capacity):
        self.start_year = start_year
        self.end_year = UNDEFINED_YEAR

        # Defines the kill constant for this epoch
        self.kill_constant = kill_constant

        # Defines the fitness adjustment for this epoch
        self.fitness_factor = fitness_factor

        # Defines the holding capacity of the environment for this epoch
        self.environment_capacity = environment_capacity

        # Use this to turn off fitness. By default fitness is enabled
        self.enable_fitness = True

    # Returns the range of years
    def getRangeOfYears(self, step=1):
        if (self.end_year == UNDEFINED_YEAR):
            raise ValueError("Epoch end year is not defined")

        return range(self.start_year, self.end_year + 1, step)

    def isCapacityUnlimited(self):
        return self.environment_capacity == UNLIMITED_CAPACITY

    def isFitnessEnabled(self):
        return self.enable_fitness == True

class Epochs:
    def __init__(self):
        self.epochs = []

    # Adds an epoch with the specified start year.
    # Sets the end year of the preceeding epoch to the year
    # before the start of this epoch (if there is a previous epoch)
    def addNextEpoch(self, start_year, fitness_factor=1, kill_constant=1, environment_capacity=UNLIMITED_CAPACITY):
        if (self.epochs):
            previous_epoch = self.epochs[LAST_EPOCH]
            previous_epoch.end_year = start_year - 1

        self.epochs.append(Epoch(start_year, fitness_factor, kill_constant, environment_capacity))

    # Set the end year of the final epoch
    def setFinalEpochYear(self, last_year):
        last_epoch = self.epochs[LAST_EPOCH]
        last_epoch.end_year = last_year
