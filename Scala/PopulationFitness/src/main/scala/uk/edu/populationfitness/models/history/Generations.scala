package uk.edu.populationfitness.models.history

import uk.edu.populationfitness.models.{Config, Population}

import scala.collection.mutable.ArrayBuffer

class Generations(val config: Config, private val _seriesRun: Int = 1, private val _parallelRun: Int = 1) {
  private var _history = new ArrayBuffer[GenerationStatistics]

  def history: Seq[GenerationStatistics] = _history

  def createForAllEpochs(epochs: Epochs): Seq[GenerationStatistics] ={
    _history = new ArrayBuffer[GenerationStatistics]

    var previousPopulation = Population(epochs)

    epochs.epochs.foreach(epoch => {
      for(year <- epoch.startYear to epoch.endYear){
        val generated = previousPopulation generateForYear(epoch, year)
        _history += show(epoch, GenerationStatistics(epoch, year, generated))
        previousPopulation = generated.population
      }
    })
    _history
  }

  def add(other: Generations) : Generations = {
    val added = new Generations(config)
    added._history ++= GenerationStatistics.add(history, other.history)
    added
  }

  private def show(epoch: Epoch, statistics: GenerationStatistics): GenerationStatistics = {
    System.out.println("Run " + _parallelRun +
      "x" + _seriesRun +
      " Year " + statistics.year +
      " Pop " + statistics.population +
      " Expected " + epoch.expectedMaxPopulation +
      " Born " + statistics.numberBorn +
      " in " + statistics.bornElapsedInHundredths +
      "s Killed " + statistics.numberKilled +
      " in " + statistics.killElapsedInHundredths + "s")
    statistics
  }
}
