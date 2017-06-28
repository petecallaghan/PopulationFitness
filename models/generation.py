class Generation:
    def __init__(self, epoch, year, number_born, number_killed):
        self.number_born = number_born
        self.number_killed = number_killed
        self.year = year
        self.epoch = epoch

class Generations:
    def __init__(self):
        self.generations = []

    def add(self, epoch, year, number_born, number_killed):
        self.generations.append(Generation(epoch, year, number_born, number_killed))
