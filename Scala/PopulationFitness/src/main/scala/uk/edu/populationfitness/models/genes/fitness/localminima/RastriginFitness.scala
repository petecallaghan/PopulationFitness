package uk.edu.populationfitness.models.genes.fitness.localminima

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness
import uk.edu.populationfitness.models.maths.CosSineCache

object RastriginFitness {
  private val RastriginTermA = 10.0 // The A term in f{x}=sum _{i=1}^{n}[t[x_{i}^{2}-A\cos(2\pi x_{i})]

  private val TwoPi = 2 * Math.PI
}

class RastriginFitness(config: Config) extends NormalizingFitness(config, 5.12) {
  override protected def calculateNormalizationRatio(n: Int): Double = 40.25 * n

  override protected def calculate(values: Seq[Double]): Double = {
    /*
             This is a tunable Rastrigin function: https://en.wikipedia.org/wiki/Rastrigin_function

             f(x)=sum{i=1 to n}[x{i}^2-  A cos(2pi x{i})]

             The '2pi' term is replaced by 'fitness_factor * pi' to make the function tunable
            */
    values.foldLeft (RastriginFitness.RastriginTermA * values.length) ((fitness, x) =>
      fitness + x * x - RastriginFitness.RastriginTermA * CosSineCache.cos(RastriginFitness.TwoPi * x)
    )
  }
}
