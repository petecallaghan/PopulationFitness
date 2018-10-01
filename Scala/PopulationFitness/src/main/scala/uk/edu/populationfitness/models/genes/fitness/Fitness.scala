package uk.edu.populationfitness.models.genes.fitness

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.genes.localminima.AckleysFitness

object Fitness {
  def apply(config: Config) : uk.edu.populationfitness.models.genes.Fitness = {
    config.fitnessFunction match {
      case FitnessFunction.Ackleys => new AckleysFitness(config)
    }
  }
}

abstract class Fitness private[fitness](val config: Config) extends uk.edu.populationfitness.models.genes.Fitness{
  private var _storedFitness: Double = 0.0
  private var _fitnessStored = false

  protected def storeFitness(fitness: Double) = {
    _storedFitness = fitness
    _fitnessStored = true
    _storedFitness
  }

  protected def storeInvertedFitness(fitness: Double): Double = storeFitness(1 - fitness)

  protected def storedFitness: Double = _storedFitness

  protected def isFitnessStored: Boolean = _fitnessStored
}
