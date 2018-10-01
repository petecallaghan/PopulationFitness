package uk.edu.populationfitness.models.genes


/**
  * Created by pete.callaghan on 13/07/2017.
  */
object FitnessFunction extends Enumeration {
  type FitnessFunction = Value
  val Rastrigin,
  StyblinksiTang,
  Schwefel226,
  Schwefel220,
  Rosenbrock,
  SumOfPowers,
  SumSquares,
  Undefined,
  Sphere,
  Ackleys,
  Alpine,
  Brown,
  ChungReynolds,
  DixonPrice,
  Exponential,
  Griewank,
  Qing,
  Salomon,
  SchumerSteiglitz,
  Trid,
  Zakharoy = Value
}
