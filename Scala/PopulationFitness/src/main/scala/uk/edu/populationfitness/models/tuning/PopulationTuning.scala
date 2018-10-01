package uk.edu.populationfitness.models.tuning

import uk.edu.populationfitness.models.tuning.Comparison.Comparison
import uk.edu.populationfitness.models.{Epoch, Epochs, Population}

import scala.annotation.tailrec

object PopulationTuning {
  trait TuningResult {
    def population: Population
    def comparison: Comparison.Comparison
  }

  def tuneFitnessFactorsForAllEpochs(epochs: Epochs, minFactor: Double, maxFactor: Double, increment: Double, percentage: Int): Comparison.Comparison = {
    var population = Population(epochs.first, epochs.firstYear, epochs.config.initialPopulation)
    val search = new Search().increment(increment)
    search.min(minFactor).max(maxFactor)

    for (epoch <- epochs.epochs) {
      val result = generateAndCompareEpochPopulation(population, search, percentage, epoch)
      if (result.comparison ne Comparison.WithinRange) {
        return result.comparison
      }
      population = result.population
      search.current(epoch.fitnessFactor)
    }

    Comparison.WithinRange
  }

  @tailrec
  private def generateAndCompareEpochPopulation(start: Population, search: Search, percentage: Int, epoch: Epoch): TuningResult = {
    var next = start

    // Set the epoch fitness factor to the current search
    epoch.fitnessFactor(search.current)

    val results = compareToExpectedForFitnessFactorForAllYearsInEpoch(next, epoch, percentage)

    if (results.comparison == Comparison.WithinRange) {
      return results
    }

    val nextSearch = search.findNext(results.comparison)
    if (nextSearch == None) return results

    generateAndCompareEpochPopulation(start, nextSearch.get, percentage, epoch)
  }

  private def showResults(population: Population, year: Int, epoch: Epoch) = {
    System.out.println("Year " + year + " Pop " + population.size + " Expected " + epoch.expectedMaxPopulation + " F=" + epoch.fitnessFactor + " F'=" + epoch.averageCapacityFactor * epoch.fitnessFactor)
  }

  private def compareToExpectedForFitnessFactorForAllYearsInEpoch(start: Population, epoch: Epoch, percentage: Int): TuningResult = {
    var next = start
    for(year <- epoch.startYear to epoch.endYear) {
      next = next.generateForYear(epoch, year).result
      showResults(next, year, epoch)
      val divergence = compareToExpected(epoch, year, next.size, percentage)
      if (divergence ne Comparison.WithinRange){
        return new TuningResult {
          override def population: Population = start // discard all the results so far for this epoch
          override def comparison: Comparison = divergence
        }
      }
    }
    new TuningResult {
      override def population: Population = next // return the final population for this epoch
      override def comparison: Comparison = Comparison.WithinRange
    }
  }

  private def compareToExpected(epoch: Epoch, year: Int, population: Int, percentage: Int): Comparison.Comparison = {
    if (population == 0) return Comparison.TooLow
    val expected = epoch.capacityForYear(year)
    if (population >= expected * 2) return Comparison.TooHigh
    val divergence = (population - expected) * 100
    val maxDivergence = expected * percentage
    if (year >= epoch.endYear) {
      if (divergence >= maxDivergence) return Comparison.TooHigh
      if (divergence < 0 - maxDivergence) return Comparison.TooLow
    }
    Comparison.WithinRange
  }
}
