package uk.edu.populationfitness.models.genes.fitness.ridge

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness

class Schwefel220Fitness(config: Config) extends NormalizingFitness(config, 10.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = 10.0 * n

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             f left (x right ) =- sum from {i=1} to {n} {left lline {x} rsub {i} right rline}
            */
    Math.abs(values.foldLeft (0.0 * values.length) ((fitness, x) => fitness - Math.abs(x)))
  }
}
