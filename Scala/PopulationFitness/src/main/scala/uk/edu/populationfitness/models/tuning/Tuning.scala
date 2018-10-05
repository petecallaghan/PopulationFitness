package uk.edu.populationfitness.models.tuning

import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.genes.FitnessFunction.FitnessFunction

class Tuning {
  var id: String = null
  var function: FitnessFunction = FitnessFunction.Undefined
  var historic_fit = 1.0
  var disease_fit = 50
  var modern_fit = 0.01
  var modern_breeding = 0.07
  var number_of_genes = 10
  var size_of_genes = 4
  var mutations_per_gene = 0.2
  var series_runs = 1
  var parallel_runs = 1
}