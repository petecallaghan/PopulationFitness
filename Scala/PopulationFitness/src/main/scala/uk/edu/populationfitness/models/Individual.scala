package uk.edu.populationfitness.models

import uk.edu.populationfitness.models.genes.Fitness
import uk.edu.populationfitness.models.genes.bitset.BitSetGenes

object Individual{
  def inheritFromParents(epoch: Epoch, birthYear: Int, mother: Individual, father: Individual): Individual = {
    new Individual(epoch, birthYear, BitSetGenes.inheritFrom(mother.genes, father.genes))
  }

  def apply(epoch: Epoch, birthYear: Int): Individual = new Individual(epoch, birthYear, BitSetGenes.buildRandom(epoch.config))
}

class Individual private[models] (val epoch: Epoch, val birthYear: Int, val genes: BitSetGenes) {

  private val _fitness: Fitness = uk.edu.populationfitness.models.genes.fitness.Fitness(epoch.config)

  def fitness: Double = _fitness(genes)

  def age(currentYear: Int): Int = math.abs(currentYear - birthYear)

  def isReadyToDie(currentYear: Int): Boolean = age(currentYear) >= epoch.maxAge

  def canBreed(currentYear: Int): Boolean = {
    val a = age(currentYear)
    (a >= epoch.config.minBreedingAge) && (a <= epoch.maxBreedingAge)
  }
}
