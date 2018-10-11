package uk.edu.populationfitness.models.tuning

import uk.edu.populationfitness.models.history._
import uk.edu.populationfitness.models.Population

import scala.annotation.tailrec

object EpochFitnessTuning {
  trait TuningResult {
    def comparison: Comparison.Comparison
    def hasDiverged: Boolean = comparison ne Comparison.WithinRange
  }

  trait TunedPopulation {
    def population: Population
  }

  def tuneAll(epochs: Epochs, minFactor: Double, maxFactor: Double, inc: Double, percentage: Int):
  Comparison.Comparison = {
    val search = new Search() { increment = inc; min = minFactor; max = maxFactor }

    tuneRemainingEpochs(epochs.epochs.iterator, Population(epochs), search, percentage)
  }

  @tailrec
  private def tuneRemainingEpochs(epochs: Iterator[Epoch], population: Population, search: Search, percentage: Int):
  Comparison.Comparison = {
    if (!epochs.hasNext) return Comparison.WithinRange // We have reached the end without diverging

    val epoch = epochs.next
    val results = tuneEpoch(epoch, population, search, percentage)
    if (results.hasDiverged) return results.comparison // We have diverged so stop tuning

    tuneRemainingEpochs(epochs, results.population, new Search(search, epoch.fitnessFactor), percentage)
  }

  @tailrec
  private def tuneEpoch(epoch: Epoch, population: Population, search: Search, percentage: Int):
  TuningResult with TunedPopulation = {
    // SIDE EFFECT!! Set the epoch fitness factor to the current search
    epoch.fitnessFactor = search.current

    val results = generateRemainingYears(population, population, epoch.years.iterator, percentage)
    showResults(population, epoch.startYear, epoch)

    if (!results.hasDiverged) {
      // Tuned
      return results
    }

    val next = search.findNext(results.comparison)
    if (next == None) return results

    // Try again with the next search
    tuneEpoch(epoch, population, next.get, percentage)
  }

  private def showResults(population: Population, year: Int, epoch: Epoch) = {
    System.out.println("Year " + year +
      " Pop " + population.size +
      " Expected " + epoch.expectedMaxPopulation +
      " F=" + epoch.fitnessFactor +
      " F'=" + epoch.averageCapacityFactor * epoch.fitnessFactor)
  }

  @tailrec
  private def generateRemainingYears(start: Population, current: Population, years: Iterator[EpochYear], percentage: Int) :
  TuningResult with TunedPopulation =
  {
    if (!years.hasNext) // Reached the end of the epoch without diverging, so that's a success
      return new TuningResult with TunedPopulation {
        override def population = current // return the final population for this epoch
        override def comparison = Comparison.WithinRange
      }

    val next = years.next
    val newPop = current.generateForYear(next.epoch, next.year).population
    val divergence = compareToExpected(next.epoch, next.year, newPop.size, percentage)
    if (divergence ne Comparison.WithinRange) // we have diverged so stop trying
      return new TuningResult with TunedPopulation {
        override def population = start // discard all the results so far for this epoch
        override def comparison = divergence
      }

    generateRemainingYears(start, newPop, years, percentage)
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