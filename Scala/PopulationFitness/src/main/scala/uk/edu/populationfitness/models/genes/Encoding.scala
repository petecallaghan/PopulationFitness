package uk.edu.populationfitness.models.genes

trait Encoding {
  /**
    * The number of bits in the encoding
    * @return
    */
  def numberOfBits: Int

  /**
    * @return as an array of integers
    */
  def asIntegers: Array[Long]

  /**
    *
    * @param encoding
    * @return true if the encoding is the same
    */
  def isSame(encoding: Encoding): Boolean
}
