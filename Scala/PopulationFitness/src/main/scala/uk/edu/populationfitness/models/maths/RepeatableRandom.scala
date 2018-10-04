package uk.edu.populationfitness.models.maths

import java.util.SplittableRandom

/**
  * Provides a single sequence of random numbers from a repeatable seed.
  *
  * Created by pete.callaghan on 07/07/2017.
  */
object RepeatableRandom {
  private val DEFAULT_SEED = 31L
  private var random = new SplittableRandom(DEFAULT_SEED)

  /**
    * Changes the seed for the sequence. Call this before generating any random numbers
    *
    * @param seed
    */
  def setSeed(seed: Long): Unit = {
    random = new SplittableRandom(seed)
  }

  /**
    * Resets the seed to the default
    */
  def resetSeed(): Unit = {
    random = new SplittableRandom(DEFAULT_SEED)
  }

  /**
    *
    * @return the next random number between 0 (inclusive) and 1 (exclusive).
    */
  def generateNext: Double = random.nextDouble

  /**
    * @param range the range
    * @return the next random number between 0 (inclusive) and range  (exclusive)
    */
  def generateNextInt(range: Double): Int = (random.nextDouble * range).toInt

  def generateNextLong(min: Long, max: Long): Long = random.nextLong(min, max)
}
