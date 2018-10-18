package uk.edu.populationfitness.models.genes.fitness.localminima

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.{CosSineCache, ExpensiveCalculatedValues}

object Schwefel226Fitness {
  private val SchwefelConstant = 418.982
  private val SchwefelConstant2 = 420.9687

  def calculateValue(n: Long): Double =
    SchwefelConstant * n + SchwefelConstant2 * CosSineCache.sin(Math.sqrt(SchwefelConstant2)) * n

  private val NormalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
}

class Schwefel226Fitness(config: Config) extends NormalizingFitness(config, 50000.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = Schwefel226Fitness.NormalizationRatios.findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             http://www.sfu.ca/~ssurjano/schwef.html

             f(x) = 418.9829d - sum{i=1 to d}[x{i} * sin(sqrt(mod(x{i})))]

             Dimensions: d

            The Schwefel226 function is complex, with many local minima. The plot shows the two-dimensional form of the function.

            Input Domain:

            The function is usually evaluated on the hypercube xi ∈ [-500, 500], for all i = 1, …, d.

            Global Minimum:

            f(x) = 0, at x = (420.9687,...,420.9687)

            */
    values.foldLeft (Schwefel226Fitness.SchwefelConstant * values.length) ((fitness, x) =>
      fitness - (x * CosSineCache.sin(Math.sqrt(Math.abs(x))))
    )
  }
}
