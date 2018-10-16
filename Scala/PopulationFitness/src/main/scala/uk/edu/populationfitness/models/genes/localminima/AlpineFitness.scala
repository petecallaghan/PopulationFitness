package uk.edu.populationfitness.models.genes.localminima

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.CosSineCache

class AlpineFitness(config: Config) extends NormalizingFitness(config, 10.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = 8.7149 * n

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) = sum from {i=1} to {n} {left lline {x} rsub {i} sin {left ({x} rsub {i} right ) +0.1 {x} rsub {i}} right rline}
            */
    values.foldLeft (0.0) ((fitness, x) => fitness + Math.abs(x * CosSineCache.sin(x) + 0.1 * x))
  }
}
