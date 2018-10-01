package uk.edu.populationfitness.models

object Epoch {
  private val UNDEFINED_YEAR = -1

  // Indicates unlimited capacity
  val UNLIMITED_CAPACITY = 0
}

class Epoch(val config: Config, val startYear:Int) {
  var endYear: Int = Epoch.UNDEFINED_YEAR

  // Defines the fitness adjustment for this epoch
  private var _fitnessFactor = 1.0

  // Defines the holding capacity of the environment for this epoch
  var environmentCapacity: Int = Epoch.UNLIMITED_CAPACITY
  var prevEnvironmentCapacity = 0

  // Use this to turn off fitness. By default fitness is enabled
  var enableFitness = true

  // Max population actually expected for this epoch
  var expectedMaxPopulation = 0

  // Probability of a pair breeding in a given year
  private var _probabilityOfBreeding:Double = config.probabilityOfBreeding
  private var _isDisease = false
  private var _maxAge = config.maxAge
  private var _maxBreedingAge = config.maxBreedingAge
  private var _totalCapacityFactor:Double = 0.0

  def this(source: Epoch) {
    this(source.config, source.startYear)
    expectedMaxPopulation = source.expectedMaxPopulation
    endYear = source.endYear
    environmentCapacity = source.environmentCapacity
    enableFitness = source.enableFitness
    _fitnessFactor = source._fitnessFactor
    _isDisease = source._isDisease
    _probabilityOfBreeding = source._probabilityOfBreeding
    _totalCapacityFactor = source._totalCapacityFactor
    prevEnvironmentCapacity = source.prevEnvironmentCapacity
    _maxAge = source._maxAge
    _maxBreedingAge = source._maxBreedingAge
  }

  def isCapacityUnlimited: Boolean = environmentCapacity == Epoch.UNLIMITED_CAPACITY

  def capacityForYear(year: Int): Int = {
    val capacityRange = environmentCapacity - prevEnvironmentCapacity
    val yearRange = endYear - startYear
    prevEnvironmentCapacity + (capacityRange * (year - startYear)) / yearRange
  }

  def isFitnessEnabled: Boolean = enableFitness

  def breedingProbability(probability: Double): Epoch = {
    _probabilityOfBreeding = probability
    this
  }

  def breedingProbability: Double = _probabilityOfBreeding

  def disease(isDisease: Boolean): Epoch = {
    _isDisease = isDisease
    this
  }

  def disease: Boolean = _isDisease

  def fitnessFactor(fitness_factor: Double): Epoch = {
    _fitnessFactor = fitness_factor
    this
  }

  def fitnessFactor: Double = this._fitnessFactor

  def capacity(environment_capacity: Int): Epoch = {
    environmentCapacity = environment_capacity
    expectedMaxPopulation = environment_capacity
    this
  }

  def max(expected_max_population: Int): Epoch = {
    expectedMaxPopulation = expected_max_population
    this
  }

  def addCapacityFactor(capacity_factor: Double): Epoch = {
    _totalCapacityFactor += capacity_factor
    this
  }

  def averageCapacityFactor: Double = this._totalCapacityFactor / (this.endYear - this.startYear + 1)

  def maxAge: Int = _maxAge

  def maxAge(max_age: Int): Epoch = {
    _maxAge = max_age
    this
  }

  def maxBreedingAge: Int = _maxBreedingAge

  def maxBreedingAge(max_breeding_age: Int): Epoch = {
    _maxBreedingAge = max_breeding_age
    this
  }

  /**
    * Reduces the populations by the ratio
    *
    * P' = P/ratio
    *
    * @param ratio the amount to reduce by
    */
  def reducePopulation(ratio: Int): Epoch = {
    expectedMaxPopulation = expectedMaxPopulation / ratio
    environmentCapacity = environmentCapacity / ratio
    prevEnvironmentCapacity = prevEnvironmentCapacity / ratio
    this
  }

  /**
    * Increases the populations by the ratio
    *
    * P' = P * ratio
    *
    * @param ratio the amount to increase by
    */
  def increasePopulation(ratio: Int): Epoch = {
    expectedMaxPopulation = expectedMaxPopulation * ratio
    environmentCapacity = environmentCapacity * ratio
    prevEnvironmentCapacity = prevEnvironmentCapacity * ratio
    this
  }
}
