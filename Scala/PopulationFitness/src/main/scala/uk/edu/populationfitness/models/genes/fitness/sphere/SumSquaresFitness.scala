package uk.edu.populationfitness.models.genes.fitness.sphere

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.ExpensiveCalculatedValues

object SumSquaresFitness {

  def calculateValue(n: Long): Double = {
    var sum = 0.0
    for(i <- 1 to n.toInt)
    {
      sum += 100 * i
    }
    sum
  }

  private val NormalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
}

class SumSquaresFitness (config: Config) extends NormalizingFitness(config, 10.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = SumSquaresFitness.NormalizationRatios.findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             http://www.sfu.ca/~ssurjano/sumsqu.html

             f(x) = sum{i=1 to d}[ i * x{i} ^ 2]

             Dimensions: d

            The Sum Squares function, also referred to as the Axis Parallel Hyper-Ellipsoid function,
            has no local minimum except the global one. It is continuous, convex and unimodal.

            Input Domain:

            The function is usually evaluated on the hypercube xi ∈ [-10, 10], for all i = 1, …, d,
            although this may be restricted to the hypercube xi ∈ [-5.12, 5.12], for all i = 1, …, d.

            Global Minimum:

            f(x) = 0, at x = (0,...,0)

            */
    var fitness = 0.0
    for(i <- 0 to values.length - 1) {
      val x = values(i)
      fitness += (i + 1) * x * x
    }
    fitness
  }
}
