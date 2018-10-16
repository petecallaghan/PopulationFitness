package uk.edu.populationfitness.models.genes.localminima

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness

class StyblinksiTangFitness(config: Config) extends NormalizingFitness(config, 5.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = 85.834 * n

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             http://www.sfu.ca/~ssurjano/stybtang.html

             f(x) = 1/2 * sum{i=1 to n}[x{i}^4 - 16x{i}^2 + 5x{i}]

             Dimensions: d

            The function is usually evaluated on the hypercube xi ∈ [-5, 5], for all i = 1, …, d.

            Global Minimum:

            f(x) = -39.16599d at x = (-2.903534,...-2.903534)
            */

    val fitness = values.foldLeft (39.166 * values.length) ((f, x) => {
      val xSquared = x * x
      f + (xSquared * xSquared - 16 * xSquared + 5 * x)
    })

    fitness / 2
  }
}
