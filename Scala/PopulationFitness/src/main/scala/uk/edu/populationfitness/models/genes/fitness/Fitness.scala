package uk.edu.populationfitness.models.genes.fitness

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.{Encoding, FitnessFunction}
import uk.edu.populationfitness.models.genes.fitness.localminima._
import uk.edu.populationfitness.models.genes.fitness.ridge._
import uk.edu.populationfitness.models.genes.fitness.sphere._
import uk.edu.populationfitness.models.genes.fitness.valley._

object Fitness {
  def apply(config: Config) : uk.edu.populationfitness.models.genes.Fitness = {
    config.fitnessFunction match {
      case FitnessFunction.Ackleys => new AckleysFitness(config)
      case FitnessFunction.Rastrigin => new RastriginFitness(config)
      case FitnessFunction.Alpine => new AlpineFitness(config)
      case FitnessFunction.Schwefel226 => new Schwefel226Fitness(config)
      case FitnessFunction.Salomon => new SalomonFitness(config)
      case FitnessFunction.StyblinksiTang => new StyblinksiTangFitness(config)
      case FitnessFunction.Schwefel220 => new Schwefel220Fitness(config)
      case FitnessFunction.Brown => new BrownFitness(config)
      case FitnessFunction.SchumerSteiglitz => new SchumerSteiglitzFitness(config)
      case FitnessFunction.Qing => new QingFitness(config)
      case FitnessFunction.SumSquares => new SumSquaresFitness(config)
      case FitnessFunction.Griewank => new GriewankFitness(config)
      case FitnessFunction.Exponential => new ExponentialFitness(config)
      case FitnessFunction.Sphere => new SphereFitness(config)
      case FitnessFunction.ChungReynolds => new ChungReynoldsFitness(config)
      case FitnessFunction.SumOfPowers => new SumOfPowersFitness(config)
      case FitnessFunction.Zakharoy => new ZakharoyFitness(config)
      case FitnessFunction.Trid => new TridFitness(config)
      case FitnessFunction.DixonPrice => new DixonPriceFitness(config)
      case FitnessFunction.Rosenbrock => new RosenbrockFitness(config)
    }
  }
}

abstract class Fitness private[fitness](val config: Config) extends uk.edu.populationfitness.models.genes.Fitness{
  private var _storedFitness: Double = 0.0
  private var _fitnessStored = false

  protected def store(fitness: Double): Double = {
    _fitnessStored = true
    _storedFitness = fitness
    _storedFitness
  }

  protected def storeInverted(fitness: Double): Double = store(1 - fitness)

  protected def storedFitness: Double = _storedFitness

  protected def isFitnessStored: Boolean = _fitnessStored

  def apply(encoding: Encoding): Double
}
