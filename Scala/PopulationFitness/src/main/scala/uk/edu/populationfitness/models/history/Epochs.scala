package uk.edu.populationfitness.models.history

import uk.edu.populationfitness.models.Config

class Epochs() {
  private var _epochs = new Array[Epoch](0)

  def epochs: Seq[Epoch] = _epochs

  def config : Config = first.config

  def firstYear: Int = first.startYear

  /** *
    * Adds an epoch with the specified start year.
    * Sets the end year of the preceding epoch to the year
    * before the start of this epoch (if there is a previous epoch)
    *
    * @param epoch
    */
  def addNextEpoch(epoch: Epoch): Unit = {
    if (!_epochs.isEmpty) {
      val previous = epochs(_epochs.size - 1)
      previous.endYear = epoch.startYear - 1
      epoch.prevEnvironmentCapacity = previous.environmentCapacity
    }
    else{
      epoch.prevEnvironmentCapacity = epoch.environmentCapacity
    }
    _epochs = _epochs :+ epoch
  }

  def addAll(epochs: Array[Epoch]): Unit = {
    for (e <- epochs) {
      addNextEpoch(e)
    }
  }

  /** *
    * Set the end year of the final epoch
    *
    * @param last_year
    */
  def setFinalEpochYear(lastYear: Int): Unit = {
    last.endYear = lastYear
  }

  /** *
    *
    * @return the last epoch
    */
  def last: Epoch = _epochs(_epochs.size - 1)

  def first: Epoch = _epochs(0)

  /**
    * Reduces the populations for all epochs by the same ratio
    *
    * P' = P/ratio
    *
    * @param ratio
    */
  def reducePopulation(ratio: Int): Unit = {
    for (epoch <- epochs) {
      epoch.reducePopulation(ratio)
    }
  }

  /**
    * Increases the populations for all epochs by the same ratio
    *
    * P' = P * ratio
    *
    * @param ratio
    */
  def increasePopulation(ratio: Int): Unit = {
    for (epoch <- epochs) {
      epoch.increasePopulation(ratio)
    }
  }

  def printFitnessFactors(): Unit = {
    for (epoch <- epochs) {
      System.out.print("Epoch ")
      System.out.print(epoch.startYear)
      System.out.print(" f=")
      System.out.println(epoch.fitnessFactor)
    }
  }
}
