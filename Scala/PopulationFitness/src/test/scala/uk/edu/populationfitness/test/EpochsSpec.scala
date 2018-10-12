package uk.edu.populationfitness.test

import org.scalatest.FunSpec
import org.scalactic.Tolerance._
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.history.{Epoch, Epochs, UkPopulationEpochs}
import uk.edu.populationfitness.models.output.{EpochsReader, EpochsWriter}

class EpochsSpec extends  FunSpec {
  private val UNDEFINED_YEAR = -1
  val _testConfig = new Config

  describe("Given a set of epochs") {

    val epochs = new Epochs {
      add(Epoch(_testConfig, -50))
      add(Epoch(_testConfig, 400))
      add(Epoch(_testConfig, 550))
      add(Epoch(_testConfig, 1086))
      add(Epoch(_testConfig, 1300))
      add(Epoch(_testConfig, 1348))
      add(Epoch(_testConfig, 1400))
      add(Epoch(_testConfig, 2016))
      add(Epoch(_testConfig, 2068))
      setFinalEpochYear(-50 + _testConfig.numberOfYears - 1)
    }

    describe("When we iterate over the epochs") {
      var firstYear = UNDEFINED_YEAR
      var lastYear = UNDEFINED_YEAR
      var numberOfYears = 0

      for (e <- epochs.years) {
        if (firstYear == UNDEFINED_YEAR) firstYear = e.year
        lastYear = e.year
        numberOfYears += 1
      }

      it("traverses all the years") {
        assert(-50 == firstYear)
        assert(-50 + _testConfig.numberOfYears - 1 == lastYear)
        assert(_testConfig.numberOfYears == numberOfYears)
      }
    }
  }

  describe("Given a set of epochs") {
    val epochs = new Epochs {
      add(Epoch(_testConfig, -50))
      add(Epoch(_testConfig, 400).fitnessFactor(2.0))
    }

    it("when we iterate over the epochs we find the right fitness factors") {
      assert(1.0 === epochs.epochs(0).fitnessFactor +- 0.1)
      assert(2.0 === epochs.epochs(1).fitnessFactor +- 0.1)
    }

  }

  describe("Given a set of epochs") {
    val epochs = new Epochs {
      add(Epoch(_testConfig, -50).capacity(1000))
      add(Epoch(_testConfig, 400).fitnessFactor(2.0))
    }

    it("When we iterate over the epochs we find the right environment capacity") {
      assert(1000 == epochs.epochs(0).environmentCapacity)
      assert(epochs.epochs(0).environmentCapacity == epochs.epochs(0).expectedMaxPopulation)
      assert(!epochs.epochs(0).isCapacityUnlimited)
      assert(Epoch.UNLIMITED_CAPACITY == epochs.epochs(1).environmentCapacity)
      assert(epochs.epochs(1).isCapacityUnlimited)
      assert(epochs.epochs(0).environmentCapacity == epochs.epochs(0).prevEnvironmentCapacity)
      assert(epochs.epochs(0).environmentCapacity == epochs.epochs(1).prevEnvironmentCapacity)
    }
  }

  describe("Given an epoch") {
    val epoch = new Epoch(_testConfig, 1000) {
      endYear = 1500
      prevEnvironmentCapacity = 1000
      environmentCapacity = 2000
    }

    it("scales") {
      assert(epoch.prevEnvironmentCapacity == epoch.capacityForYear(epoch.startYear))
      assert(epoch.environmentCapacity == epoch.capacityForYear(epoch.endYear))
      assert((epoch.environmentCapacity + epoch.prevEnvironmentCapacity) / 2 == epoch.capacityForYear((epoch.endYear + epoch.startYear) / 2))
    }
  }

  describe("Given the UK epochs") {
    val epochs = UkPopulationEpochs.define(_testConfig)
    val delta = 0.0000000001

    describe("When we write them and then read them") {
      val path = EpochsWriter.writeCsv("epochs", FitnessFunction.Undefined, _testConfig.numberOfGenes, _testConfig.sizeOfEachGene, _testConfig.mutationsPerGene, epochs)
      val found = new Epochs(EpochsReader.readEpochs(_testConfig, path))

      it("gets back all the original epochs") {
        assert(epochs.epochs.size == found.epochs.size)
        for (i <- 0 to epochs.epochs.size - 1) {
          val expected = epochs.epochs(i)
          val actual = found.epochs(i)
          assert(expected.startYear == actual.startYear)
          assert(expected.endYear == actual.endYear)
          assert(expected.environmentCapacity == actual.environmentCapacity)
          assert(expected.breedingProbability == actual.breedingProbability, delta)
          assert(expected.disease == actual.disease)
          assert(expected.fitnessFactor === actual.fitnessFactor +- delta)
          assert(expected.expectedMaxPopulation == actual.expectedMaxPopulation)
          assert(expected.maxAge == actual.maxAge)
          assert(expected.maxBreedingAge == actual.maxBreedingAge)
        }
      }
    }
  }
}