package uk.edu.populationfitness.models.output

import java.io.{File, FileWriter, IOException}

import com.opencsv.CSVWriter
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.history.{GenerationStatistics, Generations}
import uk.edu.populationfitness.models.tuning.Tuning

object GenerationsWriter {
  private val previousFileName = new ThreadLocal[String]

  private def filePath(parallel_run: Int, series_run: Int, number_of_runs: Int, config: Config) = "generations" +
    "-" + parallel_run +
    "-" + series_run +
    "of" + number_of_runs +
    "-" + config.fitnessFunction +
    "-genes" + config.numberOfGenes +
    "x" + config.sizeOfEachGene +
    "-pop" + config.initialPopulation +
    "-mut" + config.mutationsPerGene +
    "-" + config.id.replaceAll(":", "-") + ".csv"

  private def writeCsv(parallel_run: Int, series_run: Int, number_of_runs: Int, generations: Generations, tuning: Tuning): String =
    writeCsv(filePath(parallel_run, series_run, number_of_runs, generations.config), generations, tuning)

  def writeCsv(filePath: String, generations: Generations, tuning: Tuning): String = {
    val writer = createCsvWriter(filePath)
    addHeaderRow(writer)
    for (generation <- generations.history) {
      addGenerationRow(writer, generation)
    }
    writer.close
    filePath
  }

  private def createCsvWriter(filePath: String) = try
    new CSVWriter(new FileWriter(filePath), ',')
  catch {
    case e: IOException =>
      e.printStackTrace()
      new CSVWriter(new FileWriter(tryTemporaryVersionOf(filePath)), ',')
  }

  private def tryTemporaryVersionOf(filePath: String) = "~tmp." + filePath

  private def addGenerationRow(writer: CSVWriter, generation: GenerationStatistics): Unit = {
    writer.writeNext(Array[String](
      generation.epoch.startYear.toString,
      generation.epoch.endYear.toString,
      generation.epoch.capacityForYear(generation.year).toString,
      generation.epoch.isFitnessEnabled.toString,
      generation.epoch.breedingProbability.toString,
      generation.year.toString,
      generation.epoch.fitnessFactor.toString,
      generation.epoch.expectedMaxPopulation.toString,
      generation.population.toString,
      generation.numberBorn.toString,
      generation.numberKilled.toString,
      generation.bornElapsedInHundredths.toString,
      generation.killElapsedInHundredths.toString,
      generation.averageFitness.toString,
      generation.fitnessDeviation.toString,
      generation.averageAge.toString,
      generation.capacityFactor.toString,
      generation.averageFactoredFitness.toString,
      generation.averageMutations.toString,
      generation.averageLifeExpectancy.toString))
  }

  private def addHeaderRow(writer: CSVWriter): Unit = {
    writer.writeNext(Array[String](
      "Epoch Start Year",
      "Epoch End Year",
      "Epoch Environment Capacity",
      "Epoch Enable Fitness",
      "Epoch Breeding Probability",
      "Year",
      "Epoch Fitness Factor",
      "Epoch Expected Max Population",
      "Population",
      "Number Born",
      "Number Killed",
      "Born Elapsed",
      "Kill Elapsed",
      "Avg Fitness",
      "Fitness Deviation",
      "Average Age",
      "Capacity Factor",
      "Avg Factored Fitness",
      "Avg Mutations",
      "Avg Life Expectancy"))
  }

  def combineGenerationsAndWriteResult(parallel_run: Int, series_run: Int, current: Generations, total: Generations, tuning: Tuning): Generations = {
    val newTotal = if (total == null) current else total.add(current)
    val resultsFile = writeCsv(parallel_run, series_run, tuning.series_runs * tuning.parallel_runs, total, tuning)
    deletePreviousFile(resultsFile)
    newTotal
  }

  private def deletePreviousFile(resultsFile: String): Unit = {
    val previousFile = previousFileName.get
    if (previousFile != null) {
      val file = new File(previousFile)
      file.delete
    }
    previousFileName.set(resultsFile)
  }

  def createResultFileName(id: String): String = "allgenerations" + "-" + id + ".csv"
}
