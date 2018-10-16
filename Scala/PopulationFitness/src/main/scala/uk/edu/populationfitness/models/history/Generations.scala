package uk.edu.populationfitness.models.history

import uk.edu.populationfitness.models.{Config, Population}

import scala.collection.mutable.ListBuffer

object Generations{
  def apply(config: Config, epochs: Epochs, seriesRun: Int, parallelRun: Int) : Generations = {
    new Generations(config, seriesRun, parallelRun) createForAllEpochs(epochs)
  }

  def apply(config: Config) : Generations = {
    new Generations(config, 0, 0)
  }
}

class Generations private[history](val config: Config,
                                   private val _history: Seq[GenerationStatistics],
                                   val seriesRun: Int,
                                   val parallelRun: Int) {
  def this(config: Config, seriesRun: Int = 1, parallelRun: Int = 1) {
    this(config, List[GenerationStatistics](), seriesRun, parallelRun)
  }

  private trait PopulationHistory {
    val history: ListBuffer[GenerationStatistics]
    var population: Population
  }

  def history: Seq[GenerationStatistics] = _history

  private def addThisYearsGeneration(h: PopulationHistory, e: EpochYear) : PopulationHistory = {
    val changes = h.population.generateForYear(e.epoch, e.year)
    val statistics = GenerationStatistics(e.epoch, e.year, changes)
    show(e.epoch, statistics)
    h.population = changes.population
    h.history += statistics
    h
  }

  def createForAllEpochs(epochs: Epochs): Generations ={
    val all = new PopulationHistory {
      override val history = new ListBuffer[GenerationStatistics]
      override var population = Population(epochs)
    }

    epochs.years.foldLeft(all)(addThisYearsGeneration)

    new Generations(config, all.history, seriesRun, parallelRun)
  }

  def +(other: Generations) : Generations =
    new Generations(config, GenerationStatistics.add(_history, other.history),
      Math.max(seriesRun, other.seriesRun),
      Math.max(parallelRun, other.parallelRun))

  private def show(epoch: Epoch, statistics: GenerationStatistics): Unit = {
    System.out.println(
      "Run " + parallelRun +
      "x" + seriesRun +
      " Year " + statistics.year +
      " Pop " + statistics.population +
      " Expected " + epoch.expectedMaxPopulation +
      " Born " + statistics.numberBorn +
      " in " + statistics.bornElapsedInHundredths +
      "s Killed " + statistics.numberKilled +
      " in " + statistics.killElapsedInHundredths + "s")
  }
}
