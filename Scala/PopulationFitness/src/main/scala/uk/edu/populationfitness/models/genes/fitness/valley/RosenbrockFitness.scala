package uk.edu.populationfitness.models.genes.fitness.valley

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness

class RosenbrockFitness(config: Config) extends NormalizingFitness(config, 10.0) {
  protected def calculateNormalizationRatio(n: Int): Double = if (n > 1) 1222100.0 * (n - 1) else 100.0

  override protected def calculate(values: Seq[Double]): Double = {
    /*
     http://www.sfu.ca/~ssurjano/rosen.html

     f(x) = sum{i=1 to d}[100( (x{i+1} - x{i}^2)^2 + (x{i} - 1)^2 )]

     Dimensions: d

    The Rosenbrock function, also referred to as the Valley or Banana function,
    is a popular test problem for gradient-based optimization algorithms

    The function is unimodal, and the global minimum lies in a narrow, parabolic valley.

    Input Domain:

    The function is usually evaluated on the hypercube xi ∈ [-5, 10], for all i = 1, …, d,
    although it may be restricted to the hypercube xi ∈ [-2.048, 2.048], for all i = 1, …, d.

    Global Minimum:

    f(x) = 0, at x = (1,...,1)

    */
    if (values.length < 1) return 0.0

    if (values.length < 2) {
      val x = values(0)
      return 100.0 * (x - 1) * (x - 1)
    }

    var fitness = 0.0
    for(i <- 0 to values.length - 2) {
      val x = values(i)
      val xplus1 = values(i + 1)
      val xSquared = x * x
      val diff = xplus1 - xSquared
      fitness += 100.0 * (diff * diff + (x - 1) * (x - 1))
    }
    fitness
  }
}
