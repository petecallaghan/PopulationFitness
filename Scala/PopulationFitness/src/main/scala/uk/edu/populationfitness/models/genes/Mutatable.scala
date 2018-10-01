package uk.edu.populationfitness.models.genes

trait Mutatable {

  /**
    * Mutates the encoding and returns the number of mutations
    *
    * @return
    */
  def mutate: Int
}
