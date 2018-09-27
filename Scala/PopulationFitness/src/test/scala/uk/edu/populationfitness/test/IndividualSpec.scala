package uk.edu.populationfitness.test

import org.scalatest.FunSpec
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.{Config, Epoch, Individual}

class IndividualSpec extends FunSpec{
  describe("Given an old individual"){
    val config = new Config { fitnessFunction = FitnessFunction.Ackleys }
    val birthYear = 1964
    val currentYear = birthYear + config.maxAge + 10
    val epoch = new Epoch(config, currentYear)
    val individual = Individual(epoch, birthYear)

    it("Then they are ready to die"){
      assert(individual.isReadyToDie(currentYear))
    }
  }

  describe("Given a young individual"){
    val config = new Config { fitnessFunction = FitnessFunction.Ackleys }
    val birthYear = 1964
    val currentYear = birthYear + config.maxAge - 10
    val epoch = new Epoch(config, currentYear)
    val individual = Individual(epoch, birthYear)

    it("Then they are not ready to die"){
      assert(!individual.isReadyToDie(currentYear))
    }
  }

  describe("Given a breeding age individual"){
    val config = new Config { fitnessFunction = FitnessFunction.Ackleys }
    val birthYear = 1964
    val currentYear = birthYear + config.minBreedingAge + 1
    val epoch = new Epoch(config, currentYear)
    val individual = Individual(epoch, birthYear)

    it("Then they can breed"){
      assert(individual.canBreed(currentYear))
    }
  }

  describe("Given a below breeding age individual"){
    val config = new Config { fitnessFunction = FitnessFunction.Ackleys }
    val birthYear = 1964
    val currentYear = birthYear + config.minBreedingAge - 1
    val epoch = new Epoch(config, currentYear)
    val individual = Individual(epoch, birthYear)

    it("Then they cannot breed"){
      assert(!individual.canBreed(currentYear))
    }
  }

  describe("Given an above breeding age individual"){
    val config = new Config { fitnessFunction = FitnessFunction.Ackleys }
    val birthYear = 1964
    val currentYear = birthYear + config.maxBreedingAge + 1
    val epoch = new Epoch(config, currentYear)
    val individual = Individual(epoch, birthYear)

    it("Then they cannot breed"){
      assert(!individual.canBreed(currentYear))
    }
  }

  describe("Given an individual"){
    val epoch = new Epoch(new Config { fitnessFunction = FitnessFunction.Ackleys }, 1977)
    val individual = Individual(epoch, 1964)

    it("Then they have a valid fitness"){
      assert(0.0 < individual.fitness)
      assert(1.0 >= individual.fitness)
    }
  }
}
