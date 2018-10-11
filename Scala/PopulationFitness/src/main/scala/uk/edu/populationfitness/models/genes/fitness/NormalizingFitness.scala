package uk.edu.populationfitness.models.genes.fitness

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.Encoding

/**
  * Adds normalizing of fitness to caching and interpolation.
  */
abstract class NormalizingFitness protected(override val config: Config, override val maxInterpolatedValue: Double)
  extends InterpolatingFitness(config, maxInterpolatedValue) {

  private var _normalizationRatio = .0
  private var _isNormalisationRatioSet = false

  /** *
    * Implement this to define the normalization ratio
    *
    * @param n
    * @return the normalization ratio
    */
  protected def calculateNormalizationRatio(n: Int): Double

  /**
    * Implement this to calculate the fitness from an array of integers.
    *
    * @param values
    * @return
    */
  protected def calculate(values: Seq[Double]): Double

  override def apply(encoding: Encoding): Double = {
    if (isFitnessStored) return storedFitness

    implicit val values = encoding.asIntegers

    if (!_isNormalisationRatioSet) {
      _normalizationRatio = calculateNormalizationRatio(values.length)
      _isNormalisationRatioSet = true
    }

    val fitness = calculate(interpolatedView)
    val normalisedFitness = fitness / _normalizationRatio

    store(normalisedFitness)
  }
}
