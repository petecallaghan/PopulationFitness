package uk.edu.populationfitness.models.history

import uk.edu.populationfitness.models.Population.{Born, Change, Killed}
import uk.edu.populationfitness.models.history.GenerationStatistics.average

object GenerationStatistics {
  private def average(value: Double, count: Long): Double = {
    if (count < 1) value else value / count
  }

  def add(first: Seq[GenerationStatistics], second: Seq[GenerationStatistics]): Seq[GenerationStatistics] = {
    (first, second).zipped.map(_ + _)
  }

  def apply(epoch: Epoch, year: Int, latest: Change with Born with Killed): GenerationStatistics = {
    val pop = latest.population
    new GenerationStatistics(epoch,
      year,
      latest.population.size,
      latest.born.size,
      latest.killed.size,
      latest.bornElapsed,
      latest.killedElapsed,
      pop.capacityFactor,
      pop.averageMutations)
    {
      averageFitness = pop.averageFitness
      averageFactoredFitness = pop.averageFactoredFitness
      fitnessDeviation = pop.standardDeviationFitness
      averageAge = pop.averageAge(year)
      averageLifeExpectancy = latest.killed.averageAge(year)
    }
  }
}

class GenerationStatistics(val epoch: Epoch,
                           val year: Int,
                           val population: Int,
                           val numberBorn: Int,
                           val numberKilled: Int,
                           val bornTime: Long,
                           val killTime: Long,
                           var capacityFactor: Double,
                           var averageMutations: Double) {
  var averageFitness = .0
  var averageFactoredFitness = .0
  var fitnessDeviation = .0
  var averageAge = .0
  var averageLifeExpectancy = .0

  def bornElapsedInHundredths: Double = (bornTime / 10).toDouble / 100

  def killElapsedInHundredths: Double = (killTime / 10).toDouble / 100

  def +(g: GenerationStatistics): GenerationStatistics = {
    if (year != g.year) throw new Error("Cannot add different years")
    val result = new GenerationStatistics(new Epoch(epoch),
      year,
      population + g.population,
      numberBorn + g.numberBorn,
      numberKilled + g.numberKilled,
      bornTime + g.bornTime,
      killTime + g.killTime,
      capacityFactor,
      averageMutations)

    result.averageAge = average(averageAge * population + g.averageAge * g.population, result.population)
    result.averageFitness = average(averageFitness * population + g.averageFitness * g.population, result.population)
    result.averageFactoredFitness = average(averageFactoredFitness * population + g.averageFactoredFitness * g.population, result.population)
    result.epoch.expectedMaxPopulation += g.epoch.expectedMaxPopulation
    result.epoch.environmentCapacity += g.epoch.environmentCapacity
    result.capacityFactor = average(capacityFactor * population + g.capacityFactor * g.population, result.population)
    result.averageMutations = average(averageMutations * numberBorn + g.averageMutations * g.numberBorn, result.numberBorn)
    result.averageLifeExpectancy = average(averageLifeExpectancy * numberKilled + g.averageLifeExpectancy * g.numberKilled, result.numberKilled)
    result
  }
}
