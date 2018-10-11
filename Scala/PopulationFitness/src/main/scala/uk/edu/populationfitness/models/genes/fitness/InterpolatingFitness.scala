package uk.edu.populationfitness.models.genes.fitness

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.maths.{ExpensiveCalculatedValues, FastMaths}

object InterpolatingFitness {

  def calculateMaxValue(bitCount: Long): Double = {
    Math.min(java.lang.Long.MAX_VALUE, FastMaths.pow(2, bitCount) - 1)
  }

  private lazy val _bitCounts = new ExpensiveCalculatedValues[Double](calculateMaxValue)

  /**
    * Calculates the maximum value given the bit count
    *
    * @param bitCount
    * @return
    */
  private def maxForBits(bitCount: Long) = _bitCounts.findOrCalculate(bitCount)
}

abstract class InterpolatingFitness protected(override val config: Config, val maxInterpolatedValue: Double) extends Fitness(config) {

  private lazy val _interpolationRatio = maxInterpolatedValue / InterpolatingFitness.maxForBits(config.geneBitCount)

  private def interpolate(x: Long): Double = _interpolationRatio * x

  protected def interpolatedView(implicit longValues: Seq[Long]) : Seq[Double] =  longValues.view map interpolate
}


