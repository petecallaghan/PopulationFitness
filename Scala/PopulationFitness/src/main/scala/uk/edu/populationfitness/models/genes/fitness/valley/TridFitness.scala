package uk.edu.populationfitness.models.genes.fitness.valley

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.{InterpolatingFitness, NormalizingFitness}
import uk.edu.populationfitness.models.maths.{ExpensiveCalculatedValues, FastMaths}

object TridFitness {

  def calculateMin(index: Long): Double = {
    val min = (0.0 - index) * (index + 4.0) * (index + 1.0)
    min / 6
  }

  def calculateMax(index: Long): Double = {
    /*
                 M= left [{n} over {2} right ] {( {n} ^ {2} +1)} ^ {2} + left (n- left [{n} over {2} right ] right ) {( {n} ^ {2} -1)} ^ {2} + left (n-1 right ) {n} ^ {4}

                */
    val nOver2 = (index.toDouble / 2.0).round
    val nSquared = index * index
    nOver2 * FastMaths.pow(nSquared + 1, 2) + (index - nOver2) * FastMaths.pow(nSquared - 1, 2) + (index - 1) * nSquared * nSquared
  }

  private val CachedMinValues = new ExpensiveCalculatedValues[Double](calculateMin)
  private val CachedMaxValues = new ExpensiveCalculatedValues[Double](calculateMax)
}

class TridFitness(config: Config) extends NormalizingFitness(config, 0.0) {
  private var min = .0

  /**
    * Calculates the interpolation values and the normalization values
    *
    * @param n
    * @return
    */
  override protected def calculateNormalizationRatio(n: Int): Double = {
    min = TridFitness.CachedMinValues.findOrCalculate(n)
    val max = TridFitness.CachedMaxValues.findOrCalculate(n)
    calculateInterpolationRatio(n)
    max - min
  }

  private def calculateInterpolationRatio(n: Int): Unit = {
    /*
             {- {n} ^ {2} ≤x} rsub {i} ≤+ {n} ^ {2}
            */
    _interpolationRatio = (1.0 * n * n) / InterpolatingFitness.maxForBits(config.geneBitCount)
  }

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             https://www.sfu.ca/~ssurjano/trid.html

             f left (x right ) = sum from {i=1} to {n} {{left ({x} rsub {i} -1 right )} ^ {2} -} sum from {i=2} to {n} {{x} rsub {i} {x} rsub {i-1}}
            */
    var fitness = 0.0 - min
    if (values.length > 0) {
      var previousX = values(0)
      var firstSum = FastMaths.pow(previousX - 1, 2)
      var secondSum = 0.0
      for (x <- values) {
        firstSum += (x - 1) * (x - 1)
        secondSum += x * previousX
        previousX = x
      }
      fitness += (firstSum - secondSum)
    }
    fitness
  }
}
