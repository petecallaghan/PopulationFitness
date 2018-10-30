package uk.edu.populationfitness.tuning

import org.scalatest.FunSpec
import uk.edu.populationfitness.models._
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.genes.FitnessFunction.FitnessFunction
import uk.edu.populationfitness.models.history.{Epoch, Epochs, UkPopulationEpochs}
import uk.edu.populationfitness.models.maths.RepeatableRandom
import uk.edu.populationfitness.models.output.{EpochsWriter, TuningWriter}
import uk.edu.populationfitness.models.tuning.{Comparison, EpochFitnessTuning, Tuning}

object TuneEpochFitnessForFunctionsSpec {
  private val NumberOfGenes = 2000 // 20000
  private val SizeOfGenes = 100 // 1000
  private val PopulationRatio = 100 //25
  private val EpochsPath = "epochs"
  private val TuningPath = "tuning"
  private val TuningPercentage = 15
  private val MutationsPerIndividual = 1.5 // 150
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
          showResults(tuning)
          writeResults(function, config, epochs, tuning)
          assertTuned(result, tuning)
        }
      }
    }
  }

  private def showResults(tuning: Tuning): Unit = {
    showTuning(tuning)
  }

  private def showTuning(tuning: Tuning): Unit = {
    System.out.print("Tuned Disease fitness:")
    System.out.println(tuning.disease_fit)
    System.out.print("Tuned Historic fitness:")
    System.out.println(tuning.historic_fit)
    System.out.print("Tuned Modern fitness:")
    System.out.println(tuning.modern_fit)
  }

  private def assertTuned(result: Comparison.Comparison, tuning: Tuning): Unit = { // Ensure that we successfully tuned
    assert(result eq Comparison.WithinRange)
    // Ensure that the tuning result is what we expect
    assert(tuning.disease_fit < tuning.modern_fit)
  }

  private def writeResults(function: FitnessFunction, config: Config, epochs: Epochs, tuning: Tuning): Unit = {
    EpochsWriter.writeCsv(Paths.EpochsPath, function, config.numberOfGenes, config.sizeOfEachGene, config.mutationsPerGene, epochs)
    TuningWriter.writeInPath(Paths.TuningPath, tuning)
  }

  private def buildConfig(function: FitnessFunction) = {
    val config = new Config {
      fitnessFunction = function
      numberOfGenes = TuneEpochFitnessForFunctionsSpec.NumberOfGenes
      sizeOfEachGene = TuneEpochFitnessForFunctionsSpec.SizeOfGenes
    }
    config.scaleMutationsPerGeneFromBitCount(Config.MutationScale)
    config
  }

  private def possibleFitnessFactor(epoch: Option[Epoch]): Double = {
    if (epoch.isEmpty) 0.0 else epoch.get.averageCapacityFactor * epoch.get.fitnessFactor
  }

  private def createTuningFromEpochs(config: Config, epochs: Epochs) = {
    val tuning = new Tuning {
      function = config.fitnessFunction
      size_of_genes = config.sizeOfEachGene
      number_of_genes = config.numberOfGenes
      parallel_runs = 1
      series_runs = 1
      mutations_per_gene = config.mutationsPerGene
    }
    val diseaseEpoch = findDiseaseEpoch(epochs)
    val historicalEpoch = findHistoricalEpoch(epochs)
    val modernEpoch = findModernEpoch(epochs)
    tuning.historic_fit = possibleFitnessFactor(historicalEpoch)
    tuning.modern_fit = modernEpoch.averageCapacityFactor * modernEpoch.fitnessFactor
    tuning.modern_breeding = modernEpoch.breedingProbability
    tuning.disease_fit = possibleFitnessFactor(diseaseEpoch)
    tuning
  }

  private def findModernEpoch(epochs: Epochs): Epoch = { // Find the modern epoch with the max fitness factor
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

  describe("Tune " + FitnessFunction.Rastrigin) {
    tune(FitnessFunction.Rastrigin, 10.0)
  }

  describe("Tune " + FitnessFunction.Sphere) {
    tune(FitnessFunction.Sphere, 5, 25)
  }

  describe("Tune " + FitnessFunction.StyblinksiTang) {
    tune(FitnessFunction.StyblinksiTang, 10, 30)
  }

  describe("Tune " + FitnessFunction.Schwefel226) {
    tune(FitnessFunction.Schwefel226, 20, 40)
  }

  describe("Tune " + FitnessFunction.Rosenbrock) {
   tune(FitnessFunction.Rosenbrock, 10)
  }

  describe("Tune " + FitnessFunction.SumOfPowers) {
    tune(FitnessFunction.SumOfPowers, 10, 30)
  }

  describe("Tune " + FitnessFunction.SumSquares) {
    tune(FitnessFunction.SumSquares, 20, 25)
  }

  describe("Tune " + FitnessFunction.Ackleys) {
    tune(FitnessFunction.Ackleys, 3)
  }

  describe("Tune " + FitnessFunction.Alpine) {
    tune(FitnessFunction.Alpine, 20, 40)
  }

  describe("Tune " + FitnessFunction.Brown) {
    tune(FitnessFunction.Brown, 10, 25)
  }

  describe("Tune " + FitnessFunction.ChungReynolds) {
    tune(FitnessFunction.ChungReynolds, 8, 40)
  }

  describe("Tune " + FitnessFunction.DixonPrice) {
    tune(FitnessFunction.DixonPrice, 8, 30)
  }

  describe("Tune " + FitnessFunction.Exponential) {
    tune(FitnessFunction.Exponential, 4, 20)
  }

  describe("Tune " + FitnessFunction.Griewank) {
    tune(FitnessFunction.Griewank, 400)
  }

  describe("Tune " + FitnessFunction.Qing) {
    tune(FitnessFunction.Qing, 8, 25)
  }

  describe("Tune " + FitnessFunction.Salomon) {
    tune(FitnessFunction.Salomon, 4)
  }

  describe("Tune " + FitnessFunction.SchumerSteiglitz) {
    tune(FitnessFunction.SchumerSteiglitz, 8, 25)
  }

  describe("Tune " + FitnessFunction.Schwefel220) {
    tune(FitnessFunction.Schwefel220, 4, 15)
  }

  describe("Tune " + FitnessFunction.Trid) {
    tune(FitnessFunction.Trid, 100.0, 25)
  }

  describe("Tune " + FitnessFunction.Zakharoy) {
    tune(FitnessFunction.Zakharoy, 100.0, 25)
  }
}