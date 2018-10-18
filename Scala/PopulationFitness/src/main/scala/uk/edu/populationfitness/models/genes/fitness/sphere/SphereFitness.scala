package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness

class SphereFitness(config: Config) extends NormalizingFitness(config, 5.12) {
  override protected def calculateNormalizationRatio(n: Int): Double = 5.12 * 5.12 * n

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             http://www.sfu.ca/~ssurjano/spheref.html

             f(x) = sum{i=1 to n}[x{i}^2]

             Dimensions: d

            The Sphere function has d local minima except for the global one. It is continuous, convex and unimodal. The plot shows its two-dimensional form.

            Input Domain:

            The function is usually evaluated on the hypercube xi ∈ [-5.12, 5.12], for all i = 1, …, d.

            Global Minimum:

            f(x) = 0, at x = (0,...,0)

            */
    values.foldLeft(0.0) ((f, x) => f + x * x)
  }
}
