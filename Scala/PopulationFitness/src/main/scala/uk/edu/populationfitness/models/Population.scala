package uk.edu.populationfitness.models

import uk.edu.populationfitness.models.Population.{Born, Change, Killed}
import uk.edu.populationfitness.models.history.{Epoch, Epochs}
import uk.edu.populationfitness.models.maths.RepeatableRandom

object Population{
  private val NANOS_PER_MILLIS = 1000000

  trait Change {
    val population: Population
  }

  trait Born {
    val born: Population
    val bornElapsed: Long
  }

  trait Killed {
    val killed: Population
    val killedElapsed: Long
  }

  def apply(epoch: Epoch, year: Int, size:Int): Population ={
    new Population(epoch.config, new Array[Individual](size).map(_ => Individual(epoch, year)))
  }

  def apply(epochs: Epochs): Population ={
    apply(epochs.first, epochs.firstYear, epochs.config.initialPopulation)
  }
}

class Population private[models](val config: Config,
                                 private val _individuals:Seq[Individual],
                                 private val _fitnessFactor: Double = 0.0,
                                 private val _environmentCapacity: Double = 0.0) {

  private def totalFitness: Double = _individuals.foldLeft(0.0)((total, i) => { total + i.fitness})

  private def elapsedInMillis(start: Long) = (System.nanoTime() - start) / Population.NANOS_PER_MILLIS

  def averageAge(year: Int): Double = if (_individuals.nonEmpty) _individuals.foldLeft(0.0)((total, i) => { total + i.age(year)}) / _individuals.size else 0

  def averageFitness: Double = if (_individuals.nonEmpty) totalFitness / _individuals.size else 0

  def averageFactoredFitness: Double =
    if (_individuals.nonEmpty) totalFitness * _fitnessFactor / _individuals.size else 0

  val capacityFactor: Double = _environmentCapacity

  def size: Int = _individuals.size

  def averageMutations: Double =
    if (_individuals.nonEmpty)
      _individuals.foldLeft(0.0)((total, i) => { total + i.genes.mutations}) / _individuals.size
    else
      0.0

  def standardDeviationFitness: Double = {
    val mean = averageFitness
    val variance = _individuals.foldLeft(0.0)((total, i) => {
      val difference = i.fitness - mean
      total + difference * difference
    })
    math.sqrt(variance / _individuals.size)
  }

  def addNewGeneration(epoch: Epoch, year: Int) : Change with Born = {
    val start = System.nanoTime

    val babies = for (i <- 0 to _individuals.size - 2 by 2 if
      RepeatableRandom.generateNext < epoch.breedingProbability && _individuals(i).canBreed(year) && _individuals(i+1).canBreed(year)) yield {
      Individual.inheritFromParents(epoch, year, _individuals(i+1), _individuals(i))
    }

    val newGeneration = _individuals ++ babies

    new Change with Born {
      override val population = new Population(config, newGeneration, _fitnessFactor, _environmentCapacity)
      override val born = new Population(config, babies, _fitnessFactor, _environmentCapacity)
      override val bornElapsed: Long = elapsedInMillis(start)
    }
  }

  def killThoseUnfitOrReadyToDie(epoch: Epoch, currentYear: Int) : Change with Killed = {
    val start = System.nanoTime
    val environmentCapacity = if (epoch.isCapacityUnlimited) 1.0 else epoch.capacityForYear(currentYear).toDouble / _individuals.size
    val fitnessFactor = epoch.fitnessFactor * environmentCapacity

    epoch.addCapacityFactor(environmentCapacity)

    val (victims, survivors) = _individuals partition(i => {
      if (i.isReadyToDie(currentYear)) true else i.fitness * fitnessFactor < RepeatableRandom.generateNext
    })

    new Change with Killed {
      override val population = new Population(config, survivors, fitnessFactor, environmentCapacity)
      override val killed: Population = new Population(config, victims, fitnessFactor, environmentCapacity)
      override val killedElapsed: Long = elapsedInMillis(start)
    }
  }

  def generateForYear(epoch: Epoch, year: Int) : Change with Born with Killed = {
    val killedChange = killThoseUnfitOrReadyToDie(epoch, year)
    val bornChange = killedChange.population.addNewGeneration(epoch, year)
    new Change with Born with Killed {
      override val population: Population = bornChange.population
      override val killed: Population = killedChange.killed
      override val killedElapsed: Long = killedChange.killedElapsed
      override val born: Population = bornChange.born
      override val bornElapsed: Long = bornChange.bornElapsed
    }
  }
}