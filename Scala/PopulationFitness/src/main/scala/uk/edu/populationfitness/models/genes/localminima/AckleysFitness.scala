package uk.edu.populationfitness.models.genes.localminima

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.maths.{CosSineCache, ExpensiveCalculatedValues}
import uk.edu.populationfitness.models.genes.fitness.NormalizingFitness

object AckleysFitness {

  def calculateValue(n: Long): Double = {
    /*{f left (x right )} over {20 left (1- {e} ^ {-0.2α} right ) +e- {e} ^ {-1}}*/
    20.0 * (1.0 - math.exp(-0.2 * _alpha)) + math.E - math.exp(-1.0)
  }

  private val _normalizationRatios = new ExpensiveCalculatedValues[Double](calculateValue)
  private val _alpha = 4.5
  private val _twentyPlusE = 20.0 + math.E
  private val _twoPi = 2.0 * math.Pi
}

class AckleysFitness(config: Config) extends NormalizingFitness(config, 5.0) {
  override protected def calculateNormalizationRatio(n: Int): Double = AckleysFitness._normalizationRatios findOrCalculate(n)

  override protected def calculate(values: Seq[Double]): Double = {
    /* http://www.cs.unm.edu/~neal.holts/dga/benchmarkFunction/ackley.html
     f left (x right ) =-20 exp {left (-0.2 sqrt {{1} over {n} sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right )} - exp {left ({1} over {n} sum from {i=1} to {n} {cos {left ({2πx} rsub {i} right )}} right ) +20+ exp⁡ (1)}
    */
    trait Sums { var first: Double; var second: Double }
    val sums = new Sums { override var first: Double = 0.0; override var second: Double = 0.0}

    values.foldLeft (sums) ((s, x) => {
      s.first += x * x
      s.second += CosSineCache cos(AckleysFitness._twoPi * x)
      s
    })

    val n = values.length
    -20.0 * math.exp(-0.2 * math.sqrt(sums.first / n)) - math.exp(sums.second / n) + AckleysFitness._twentyPlusE
  }
}
