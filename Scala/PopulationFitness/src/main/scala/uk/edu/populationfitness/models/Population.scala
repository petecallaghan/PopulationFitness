package uk.edu.populationfitness.models

trait PopulationChange {
  val result: Population
  val changes: Population
}

object Population{
  def apply(epoch: Epoch, year: Int, size:Int): Unit ={
    new Population(epoch.config, new Array(size).map(i => Individual.apply(epoch, year)))
  }

  def addNewGeneration(epoch: Epoch, year: Int, current: Population) : PopulationChange = {
    val babies = current._individuals.grouped(2).
      filter((p) => RepeatableRandom.generateNext < epoch.breedingProbability && p(0).canBreed(year) && p(1).canBreed(year)).
      map((parents) => {
        val father = parents(0)
        val mother = parents(1)
        Individual.inheritFromParents(epoch, year, mother, father)
    }).toArray

    val newGeneration = current._individuals ++ babies

    new PopulationChange {
      override val result: Population = new Population(current.config, newGeneration)
      override val changes: Population = new Population(current.config, babies)
    }
  }

  def killThoseUnfitOrReadyToDie(epoch: Epoch, currentYear: Int, current: Population) : PopulationChange = {
    val environmentCapacity = if (epoch.isCapacityUnlimited) 1.0 else epoch.capacityForYear(currentYear).toDouble / current._individuals.length
    val fitnessFactor = epoch.fitnessFactor * environmentCapacity

    val (survivors, victims) = current._individuals partition((i) => {
      if (i.isReadyToDie(currentYear)) true else i.fitness * fitnessFactor < RepeatableRandom.generateNext
    })

    new PopulationChange {
      override val result: Population = new Population(current.config, survivors, fitnessFactor, environmentCapacity)
      override val changes: Population = new Population(current.config, victims, fitnessFactor, environmentCapacity)
    }
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
}