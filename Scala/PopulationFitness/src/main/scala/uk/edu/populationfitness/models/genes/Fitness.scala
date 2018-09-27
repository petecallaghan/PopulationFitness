package uk.edu.populationfitness.models.genes

/**
  * Implement this to provide a way of calculating fitness from genes
  *
  * Created by pete.callaghan on 13/07/2017.
  */
trait Fitness {
  /**
    * Calculate the fitness in a range of 0..1
    *
    * @param encoding the encoding to calculate fitness from
    * @return
    */
  def apply(encoding: Encoding): Double
}

