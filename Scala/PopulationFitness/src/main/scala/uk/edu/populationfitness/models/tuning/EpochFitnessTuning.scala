package uk.edu.populationfitness.models.tuning

import uk.edu.populationfitness.models.{Epoch, Epochs, Population}

import scala.annotation.tailrec

object EpochFitnessTuning {
  trait TuningResult {
    def comparison: Comparison.Comparison
  }

  trait TunedPopulation {
    def population: Population
  }

  def tuneAll(epochs: Epochs, minFactor: Double, maxFactor: Double, increment: Double, percentage: Int): Comparison.Comparison = {
    var population = Population(epochs.first, epochs.firstYear, epochs.config.initialPopulation)
    val search = new Search().increment(increment)
    search.min(minFactor).max(maxFactor)

    for (epoch <- epochs.epochs) {
      val results = tuneFitnessForEpoch(epoch, population, search, percentage)
      if (results.comparison ne Comparison.WithinRange) {
        return results.comparison
      }
      population = results.population
      search.current(epoch.fitnessFactor)
    }

    Comparison.WithinRange
  }

  @tailrec
  private def tuneFitnessForEpoch(epoch: Epoch, population: Population, search: Search, percentage: Int):
  TuningResult with TunedPopulation = {
    // Set the epoch fitness factor to the current search
    epoch.fitnessFactor(search.current)

    val results = compareToExpectedForFitnessFactorForAllYearsInEpoch(population, epoch, percentage)
    showResults(population, epoch.startYear, epoch)

    if (results.comparison == Comparison.WithinRange) {
      return results
    }

    val nextSearch = search.findNext(results.comparison)
    if (nextSearch == None) return results

    tuneFitnessForEpoch(epoch, population, nextSearch.get, percentage)
  }

  private def showResults(population: Population, year: Int, epoch: Epoch) = {
    System.out.println("Year " + year + " Pop " + population.size + " Expected " + epoch.expectedMaxPopulation + " F=" + epoch.fitnessFactor + " F'=" + epoch.averageCapacityFactor * epoch.fitnessFactor)
  }

  private def compareToExpectedForFitnessFactorForAllYearsInEpoch(start: Population, epoch: Epoch, percentage: Int):
  TuningResult with TunedPopulation = {
    var next = start

    for(year <- epoch.startYear to epoch.endYear) {
      next = next.generateForYear(epoch, year).result
      val divergence = compareToExpected(epoch, year, next.size, percentage)
      if (divergence ne Comparison.WithinRange){
        return new TuningResult with TunedPopulation {
          override def population = start // discard all the results so far for this epoch
          override def comparison = divergence
        }
      }
    }
    new TuningResult with TunedPopulation {
      override def population = next // return the final population for this epoch
      override def comparison = Comparison.WithinRange
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
