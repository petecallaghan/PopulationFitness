package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.FastMaths

object ChungReynoldsFitness {
  private val TenToThe8th = FastMaths pow(10.0, 8)
}

class ChungReynoldsFitness(config: Config) extends NormalizingFitness(config, 100.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = ChungReynoldsFitness.TenToThe8th * FastMaths.pow(n, 2)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) = {left (sum from {i=1} to {n} {{x} rsub {i} rsup {2}} right )} ^ {2}
            */

    val fitness = values.foldLeft(0.0) ((f, x) => f + x * x)
    fitness * fitness
  }
}
