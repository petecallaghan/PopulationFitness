package uk.edu.populationfitness.models

import uk.edu.populationfitness.models.Population.{Born, Change, Killed}

object Population{
  trait Change {
    val result: Population
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
}

class Population private[models](val config: Config,
                                 private val _individuals:Seq[Individual],
                                 private val _fitnessFactor: Double = 0.0,
                                 private val _environmentCapacity: Double = 0.0) {

  private def totalFitness: Double = _individuals.foldLeft(0.0)((total, i) => { total + i.fitness})

  def averageFitness: Double = if (_individuals.size > 0) totalFitness / _individuals.size else 0

  def averageFactoredFitness: Double =
    if (_individuals.size > 0) totalFitness * _fitnessFactor / _individuals.size else 0

  val capacityFactor: Double = _environmentCapacity

  def size: Int = _individuals.size

  def averageMutations =
    if (_individuals.size > 0)
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
    val babies = _individuals.grouped(2).
      filter((p) => RepeatableRandom.generateNext < epoch.breedingProbability && p(0).canBreed(year) && p(1).canBreed(year)).
      map((parents) => {
        val father = parents(0)
        val mother = parents(1)
        Individual.inheritFromParents(epoch, year, mother, father)
      }).toArray

    val newGeneration = _individuals ++ babies

    new Change with Born {
      override val result = new Population(config, newGeneration)
      override val born = new Population(config, babies)
      override val bornElapsed = System.nanoTime - start
    }
  }

  def killThoseUnfitOrReadyToDie(epoch: Epoch, currentYear: Int) : Change with Killed = {
    val start = System.nanoTime
    val environmentCapacity = if (epoch.isCapacityUnlimited) 1.0 else epoch.capacityForYear(currentYear).toDouble / _individuals.length
    val fitnessFactor = epoch.fitnessFactor * environmentCapacity

    val (victims, survivors) = _individuals partition((i) => {
      if (i.isReadyToDie(currentYear)) true else i.fitness * fitnessFactor < RepeatableRandom.generateNext
    })

    new Change with Killed {
      override val result = new Population(config, survivors, fitnessFactor, environmentCapacity)
      override val killed: Population = new Population(config, victims, fitnessFactor, environmentCapacity)
      override val killedElapsed = System.nanoTime - start
    }
  }

  def generateForYear(epoch: Epoch, year: Int) : Change with Born with Killed = {
    val killedChange = killThoseUnfitOrReadyToDie(epoch, year)
    val bornChange = killedChange.result.addNewGeneration(epoch, year)
    new Change with Born with Killed {
      override val result: Population = bornChange.result
      override val killed: Population = killedChange.killed
      override val killedElapsed: Long = killedChange.killedElapsed
      override val born: Population = bornChange.born
      override val bornElapsed: Long = bornChange.bornElapsed
    }
  }
}