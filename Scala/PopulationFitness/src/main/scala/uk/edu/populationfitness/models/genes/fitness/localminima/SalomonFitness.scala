package uk.edu.populationfitness.models.genes.fitness.localminima

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.CosSineCache

object SalomonFitness {
  private val NormalizationConstant = 200.0 * Math.PI + 10.0
  private val TwoPi = 2.0 * Math.PI
}

class SalomonFitness(config: Config) extends NormalizingFitness(config, 100.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = 1.0 - CosSineCache.cos(SalomonFitness.NormalizationConstant * Math.sqrt(n))

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) =1- cos {left (2π sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right ) +0.1 sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}}}
            */
    val sum = values.foldLeft (0.0) ((fitness, x) => fitness + x * x)
    val sqrtSum = Math.sqrt(sum)
    1.0 - CosSineCache.cos(SalomonFitness.TwoPi * sqrtSum) + 0.1 * sqrtSum
  }
}
