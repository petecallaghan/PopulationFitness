package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.CosSineCache

class GriewankFitness(config: Config) extends NormalizingFitness(config, 600.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = 900.0 * n + 2.0

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) = sum from {i=1} to {n} {{{x} rsub {i} rsup {2}} over {400} - prod from {i=1} to {n} {cos {left ({{x} rsub {i}} over {sqrt {i}} right )} +1}}
            */
    if (values.length < 1) return 0.0
    var fitness = 1.0
    var product = 1.0
    for(i <- 1 to values.length - 1) {
      val x = values(i)
      product *= CosSineCache.cos(x / Math.sqrt(1.0 * i))
      fitness += (x * x / 400.0)
    }
    fitness + product
  }
}
