package uk.edu.populationfitness.test

import org.scalatest.FunSpec
import org.scalactic.Tolerance._
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.history.{Epoch, Epochs, GenerationStatistics, Generations}
import uk.edu.populationfitness.models.output.{GenerationsReader, GenerationsWriter}
import uk.edu.populationfitness.models.tuning.Tuning

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

  private def assertAreEqual(expected: Seq[GenerationStatistics], actual: Seq[GenerationStatistics]): Unit = {
    assert(expected.length == actual.length)
    for(i <- 0 to expected.length - 1){
      assertAreEqual(expected(i), actual(i))      
    }
  }

  private def assertAreEqual(e: GenerationStatistics, a: GenerationStatistics): Unit = {
    assert(e.epoch.startYear == a.epoch.startYear)
    assert(e.epoch.endYear == a.epoch.endYear)
    assert(e.epoch.environmentCapacity == a.epoch.environmentCapacity)
    assert(e.epoch.isFitnessEnabled == a.epoch.isFitnessEnabled)
    assert(e.epoch.breedingProbability === a.epoch.breedingProbability +- 0.01)
    assert(e.year == a.year)
    assert(e.epoch.fitnessFactor === a.epoch.fitnessFactor +- 0.000001)
    assert(e.epoch.expectedMaxPopulation == a.epoch.expectedMaxPopulation)
    assert(e.population == a.population)
    assert(e.numberBorn == a.numberBorn)
    assert(e.numberKilled == a.numberKilled)
    assert(e.bornElapsedInHundredths === a.bornElapsedInHundredths +- 0.001)
    assert(e.killElapsedInHundredths === a.killElapsedInHundredths +- 0.001)
    assert(e.averageFitness === a.averageFitness +- 0.001)
    assert(e.averageFactoredFitness === a.averageFactoredFitness +- 0.001)
    assert(e.fitnessDeviation === a.fitnessDeviation +- 0.001)
    assert(e.averageAge === a.averageAge +- .001)
    assert(e.capacityFactor === a.capacityFactor +- 0.001)
    assert(e.averageMutations === a.averageMutations +- 0.001)
    assert(e.averageLifeExpectancy === a.averageLifeExpectancy +- 0.001)
  }

  describe("Given a standard configuration") {
    val config = new Config {
      minBreedingAge = 1
      fitnessFunction = FitnessFunction.Ackleys
    }
    val generations = new Generations(config)
    describe("With some epochs"){
      val epochs = new Epochs
      epochs add new Epoch(config, -50).fitnessFactor(1.0).capacity(4000)
      epochs setFinalEpochYear -40

      describe("When the simulation runs through the epochs"){
        val generated = generations createForAllEpochs epochs

        it("Then we get a history of the simulation"){
          assert(11 == generated.history.size)
        }
      }
    }
  }

  describe("Given two sets of generation statistics") {
    val epoch = Epoch(new Config, -50)
    val first = new GenerationStatistics(epoch, epoch.startYear, 100, 10, 20, 12, 13, 1.0, 2.0)
    val second = new GenerationStatistics(epoch, epoch.startYear, 23, 1, 5, 120, 78, 1.0, 2.0)

    describe("When they are added"){
      val result = first + second

      it ("Then the results are correct"){
        assertAreAdded(first, second, result)
      }
    }
  }

  describe("Given two collections of statistics"){
    val epoch = Epoch(new Config, -50)

    val first = List(new GenerationStatistics(epoch, epoch.startYear, 100, 10, 20, 12, 13, 1.0, 2.0))
    val second = List(new GenerationStatistics(epoch, epoch.startYear, 23, 1, 5, 120, 78, 1.0, 2.0))

    first(0).averageAge = 10.5
    second(0).averageAge = 20.3
    first(0).averageLifeExpectancy = 50.5
    second(0).averageLifeExpectancy = 60.76

    describe("When they are added"){
      val result = GenerationStatistics.add(first, second)

      it ("Then we have a collection of additions"){
        assertAreAdded(first(0), second(0), result(0))
      }
    }
  }

  describe("Given a standard configuration ..."){
    val config = new Config {  
      minBreedingAge = 1 // so we get some babies
      fitnessFunction = FitnessFunction.Ackleys
    }
    val generations = new Generations(config)
    
    describe("... with some epochs ..."){
      val epochs = new Epochs
      epochs.add(new Epoch(config, -25).fitnessFactor(1.0).capacity(4000))
      epochs.add(new Epoch(config, 0).fitnessFactor(1.0).capacity(4000))
      epochs.add(new Epoch(config, 25).fitnessFactor(1.0).capacity(4000))
      epochs.setFinalEpochYear(50)

      describe("... and some results written to a file"){
        val generated = generations createForAllEpochs epochs
        val path = GenerationsWriter writeCsv("test-results.csv", generated, new Tuning)
        
        describe("When the results are read back"){
          val readResult = GenerationsReader readGenerations(config, path)
          
          it("then they are the same as those written"){
            assertAreEqual(generated.history, readResult)
          }
        }
      }
    }
  }
}
