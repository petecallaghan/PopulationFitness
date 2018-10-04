package uk.edu.populationfitness.test

import org.scalatest.FunSpec
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.history.{Epoch, Epochs, GenerationStatistics, Generations}

class GenerationsSpec extends FunSpec {
  private def assertAreAdded(first: GenerationStatistics, second: GenerationStatistics, result: GenerationStatistics): Unit = {
    assert(result.year == first.year)
    assert(result.year == second.year)
    assert(result.epoch.startYear == first.epoch.startYear)
    assert(result.epoch.endYear == first.epoch.endYear)
    assert(result.epoch.expectedMaxPopulation == first.epoch.expectedMaxPopulation + second.epoch.expectedMaxPopulation)
    assert(result.epoch.environmentCapacity == first.epoch.environmentCapacity + second.epoch.environmentCapacity)
    assert(result.population == first.population + second.population)
    assert(result.numberBorn == first.numberBorn + second.numberBorn)
    assert(result.numberKilled == first.numberKilled + second.numberKilled)
    assert(result.capacityFactor == (first.capacityFactor * first.population + second.capacityFactor * second.population) / result.population, 0.00001)
    assert(result.averageAge == (first.averageAge * first.population + second.averageAge * second.population) / result.population, 0.00001)
    assert(result.averageLifeExpectancy == (first.averageLifeExpectancy * first.numberKilled + second.averageLifeExpectancy * second.numberKilled) / result.numberKilled, 0.00001)
  }

  describe("Given a standard configuration") {
    val config = new Config {
      minBreedingAge = 1
      fitnessFunction = FitnessFunction.Ackleys
    }
    val generations = new Generations
    describe("With some epochs"){
      val epochs = new Epochs
      epochs addNextEpoch new Epoch(config, -50).fitnessFactor(1.0).capacity(4000)
      epochs setFinalEpochYear -40

      describe("When the simulation runs through the epochs"){
        val history = generations createForAllEpochs epochs

        it("Then we get a history of the simulation"){
          assert(11 == history.size)
        }
      }
    }
  }

  describe("Given two sets of generation statistics") {
    val config = new Config
    val epoch = new Epoch(config, -50)
    val first = new GenerationStatistics(epoch, epoch.startYear, 100, 10, 20, 12, 13, 1.0, 2.0)
    val second = new GenerationStatistics(epoch, epoch.startYear, 23, 1, 5, 120, 78, 1.0, 2.0)

    describe("When they are added"){
      val result = first + second

      it ("Then the results are correct"){
        assertAreAdded(first, second, result)
      }
    }
  }
}
