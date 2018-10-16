package uk.edu.populationfitness.app

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.history.{Epoch, Epochs}
import uk.edu.populationfitness.models.output.AnalysisWriter
import uk.edu.populationfitness.models.simulation.{ParallelSimulations, SerialSimulations}
import uk.edu.populationfitness.models.tuning.Tuning

object Main {
  private val DiseaseYears = 3
  private val PostDiseaseYears = 30

  def setInitialPopulationFromFirstEpochCapacity(config: Config, epochs: Epochs): Unit = {
    config.initialPopulation = epochs.first.expectedMaxPopulation
  }

  def addSimulatedEpochs(config: Config, epochs: Epochs, tuning: Tuning, diseaseYears: Int, postDiseaseYears: Int): Unit = {
    val diseaseStartYear = epochs.last.endYear + 1
    val recoveryStartYear = diseaseStartYear + diseaseYears
    val finalYear = recoveryStartYear + postDiseaseYears
    val maxExpected = epochs.last.expectedMaxPopulation
    epochs.add(new Epoch(config, diseaseStartYear).fitnessFactor(tuning.disease_fit).max(maxExpected).breedingProbability(tuning.modern_breeding).disease(true))
    epochs.add(new Epoch(config, recoveryStartYear).fitnessFactor(tuning.historic_fit).max(maxExpected))
    epochs.setFinalEpochYear(finalYear)
  }


  def main(args: Array[String]) {
    val config = new Config
    val epochs = new Epochs()

    val tuning = Commands tuningAndEpochsFromInputFiles(config, epochs, args)

    setInitialPopulationFromFirstEpochCapacity(config, epochs)
    addSimulatedEpochs(config, epochs, tuning, DiseaseYears, PostDiseaseYears)

    val simulations = new ParallelSimulations(config, epochs, tuning)

    val results = simulations runAllAndWriteResults()

    AnalysisWriter append(Commands.experimentFile, Commands.tuningFile, Commands.epochsFile,
      simulations.resultsFile, tuning, results)
  }
}
