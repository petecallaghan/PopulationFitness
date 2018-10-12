package uk.edu.populationfitness.models.tuning

import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.genes.FitnessFunction.FitnessFunction

class Tuning {
  var id: String = null
  var function: FitnessFunction = FitnessFunction.Undefined
  var historic_fit: Double = 1.0
  var disease_fit: Double = 50.0
  var modern_fit: Double = 0.01
  var modern_breeding: Double = 0.07
  var number_of_genes:Int = 10
  var size_of_genes:Int = 4
  var mutations_per_gene: Double = 0.2
  var series_runs:Int = 1
  var parallel_runs:Int = 1
}