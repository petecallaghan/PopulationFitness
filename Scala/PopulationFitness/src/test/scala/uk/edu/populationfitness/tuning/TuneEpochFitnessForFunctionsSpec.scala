package uk.edu.populationfitness.tuning

import org.scalatest.FunSpec
import uk.edu.populationfitness.UkPopulationEpochs
import uk.edu.populationfitness.models._
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.genes.FitnessFunction.FitnessFunction
import uk.edu.populationfitness.models.tuning.{Comparison, EpochFitnessTuning}

object TuneEpochFitnessForFunctionsSpec {
  private val NumberOfGenes = 20000
  private val SizeOfGenes = 1000
  private val PopulationRatio = 100 //25
  private val EpochsPath = "epochs"
  private val TuningPath = "tuning"
  private val TuningPercentage = 15
  private val MutationsPerIndividual = 150
}

class TuneEpochFitnessForFunctionsSpec extends  FunSpec {
  private def tune(function: FitnessFunction, maxFactor: Double): Unit = {
    tune(function, maxFactor, TuneEpochFitnessForFunctionsSpec.TuningPercentage)
  }

  private def tune(function: FitnessFunction, maxFactor: Double, tuningPercentage: Int): Unit = {
    describe("given a population with fitness decided by " + function){
      RepeatableRandom.resetSeed()
      val config = buildConfig(function)
      val epochs = UkPopulationEpochs.define(config)
      epochs.reducePopulation(TuneEpochFitnessForFunctionsSpec.PopulationRatio)
      config.initialPopulation = epochs.first.environmentCapacity
      config.mutationsPerGene = TuneEpochFitnessForFunctionsSpec.MutationsPerIndividual

      describe ("when the fitness factors are tuned"){
        val result = EpochFitnessTuning.tuneAll(epochs, 0.0, maxFactor, 0.000001, tuningPercentage)

        it ("produces sensible results"){
          val tuning = createTuningFromEpochs(config, epochs)
          //showResults(tuning)
          //writeResults(function, config, epochs, tuning)
          //assertTuned(result, tuning)
        }
      }
    }
  }

  private def showResults(tuning: Nothing): Unit = {
    showTuning(tuning)
    //GenesTimer.showAll
  }

  private def showTuning(tuning: Nothing): Unit = {
    System.out.print("Tuned Disease fitness:")
    //System.out.println(tuning.disease_fit)
    System.out.print("Tuned Historic fitness:")
    //System.out.println(tuning.historic_fit)
    System.out.print("Tuned Modern fitness:")
    //System.out.println(tuning.modern_fit)
  }

  private def assertTuned(result: Comparison.Comparison, tuning: Nothing): Unit = { // Ensure that we successfully tuned
    assert(result eq Comparison.WithinRange)
    // Ensure that the tuning result is what we expect
    //Assert.assertTrue(tuning.disease_fit < tuning.modern_fit)
  }

  private def writeResults(function: FitnessFunction, config: Config, epochs: Epochs, tuning: Nothing): Unit = {
    /*
    EpochsWriter.writeCsv(TuneFunctionsTest.EpochsPath, function, config.getNumberOfGenes, config.getSizeOfEachGene, config.getMutationsPerGene, epochs)
    TuningWriter.writeInPath(TuneFunctionsTest.TuningPath, tuning)
    */
  }

  private def setUpGeneTimers(config: Config): Unit = {
    //config.setGenesFactory(new Nothing(config.getGenesFactory))
    //GenesTimer.resetAll
  }

  private def buildConfig(function: FitnessFunction) = {
    val config = new Config {
      fitnessFunction = function
      numberOfGenes = TuneEpochFitnessForFunctionsSpec.NumberOfGenes
      sizeOfEachGene = TuneEpochFitnessForFunctionsSpec.SizeOfGenes
    }
    config.scaleMutationsPerGeneFromBitCount(Config.MutationScale)
    setUpGeneTimers(config)
    config
  }

  private def createTuningFromEpochs(config: Config, epochs: Epochs) = {
    val tuning = null //new Nothing
    /*
    tuning.function = config.getGenesFactory.getFitnessFunction
    tuning.size_of_genes = config.getSizeOfEachGene
    tuning.number_of_genes = config.getNumberOfGenes
    tuning.parallel_runs = 1
    tuning.series_runs = 1
    tuning.mutations_per_gene = config.getMutationsPerGene
    val diseaseEpoch = findDiseaseEpoch(epochs)
    val historicalEpoch = findHistoricalEpoch(epochs)
    val modernEpoch = findModernEpoch(epochs)
    tuning.historic_fit = historicalEpoch.averageCapacityFactor * historicalEpoch.fitness
    tuning.modern_fit = modernEpoch.averageCapacityFactor * modernEpoch.fitness
    tuning.modern_breeding = modernEpoch.breedingProbability
    tuning.disease_fit = diseaseEpoch.averageCapacityFactor * diseaseEpoch.fitness
     */
    tuning
  }

  private def findModernEpoch(epochs: Epochs) = { // Find the modern epoch with the max fitness factor
    var modern = epochs.last
    var max = modern.fitnessFactor * modern.averageCapacityFactor
    for ( i <- epochs.epochs.size - 1 to epochs.epochs.size - 6 by -1) {
      val current = epochs.epochs(i)
      val currentFitness = current.fitnessFactor * current.averageCapacityFactor
      if (max < currentFitness) {
        max = currentFitness
        modern = current
      }
    }
    modern
  }

  private def findHistoricalEpoch(epochs: Epochs): Option[Epoch] = {
    for (epoch <- epochs.epochs) {
      if (epoch.startYear >= 1451) { // Use this epoch as the historical epoch
        return Some(epoch)
      }
    }
    None
  }

  private def findDiseaseEpoch(epochs: Epochs): Option[Epoch] = {
    for (epoch <- epochs.epochs) {
      if (epoch.disease) return Some(epoch)
    }
    None
  }


  describe("Tune " + FitnessFunction.Ackleys) {
    tune(FitnessFunction.Ackleys, 4)
  }
}
