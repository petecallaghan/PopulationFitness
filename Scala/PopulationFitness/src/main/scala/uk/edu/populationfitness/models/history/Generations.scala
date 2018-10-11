package uk.edu.populationfitness.models.history

import uk.edu.populationfitness.models.{Config, Population}

import scala.collection.mutable.ListBuffer

class Generations private[history](val config: Config,
                          private val _history: Seq[GenerationStatistics],
                          private val _seriesRun: Int,
                          private val _parallelRun: Int) {
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

    new Generations(config, all.history, _seriesRun, _parallelRun)
  }

  def add(other: Generations) : Generations =
    new Generations(config, GenerationStatistics.add(_history, other.history), _seriesRun, _parallelRun)

  private def show(epoch: Epoch, statistics: GenerationStatistics): Unit = {
    System.out.println(
      "Run " + _parallelRun +
      "x" + _seriesRun +
      " Year " + statistics.year +
      " Pop " + statistics.population +
      " Expected " + epoch.expectedMaxPopulation +
      " Born " + statistics.numberBorn +
      " in " + statistics.bornElapsedInHundredths +
      "s Killed " + statistics.numberKilled +
      " in " + statistics.killElapsedInHundredths + "s")
  }
}
