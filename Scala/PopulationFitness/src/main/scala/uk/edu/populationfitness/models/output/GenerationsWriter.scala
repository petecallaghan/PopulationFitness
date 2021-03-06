package uk.edu.populationfitness.models.output

import java.io.{File, FileWriter, IOException}

import com.opencsv.CSVWriter
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.history.{GenerationStatistics, Generations}
import uk.edu.populationfitness.models.tuning.Tuning

object GenerationsWriter {
  private val previousFileName = new ThreadLocal[String]

  private def filePath(parallel_run: Int, series_run: Int, config: Config, tuning: Tuning) = "generations" +
    "-" + parallel_run +
    "-" + series_run +
    "of" + parallel_run * series_run +
    "-" + config.fitnessFunction +
    "-genes" + config.numberOfGenes +
    "x" + config.sizeOfEachGene +
    "-pop" + config.initialPopulation +
    "-mut" + config.mutationsPerGene +
    "-" + tuning.id.replaceAll(":", "-") + ".csv"

  private def writeCsv(parallel_run: Int, series_run: Int, generations: Generations, tuning: Tuning): String =
    writeCsv(filePath(parallel_run, series_run, generations.config, tuning), generations, tuning)

  def writeCsv(filePath: String, generations: Generations, tuning: Tuning): String = {
    val writer = createCsvWriter(filePath)
    addHeaderRow(writer)
    generations.history.foreach(addGenerationRow(writer, _))
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

  private def addGenerationRow(writer: CSVWriter, stats: GenerationStatistics): Unit = {
    writer.writeNext(Array[String](
      stats.epoch.startYear.toString,
      stats.epoch.endYear.toString,
      stats.epoch.capacityForYear(stats.year).toString,
      stats.epoch.isFitnessEnabled.toString,
      stats.epoch.breedingProbability.toString,
      stats.year.toString,
      stats.epoch.fitnessFactor.toString,
      stats.epoch.expectedMaxPopulation.toString,
      stats.population.toString,
      stats.numberBorn.toString,
      stats.numberKilled.toString,
      stats.bornElapsedInHundredths.toString,
      stats.killElapsedInHundredths.toString,
      stats.averageFitness.toString,
      stats.fitnessDeviation.toString,
      stats.averageAge.toString,
      stats.capacityFactor.toString,
      stats.averageFactoredFitness.toString,
      stats.averageMutations.toString,
      stats.averageLifeExpectancy.toString))
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

  private def deletePreviousFile(resultsFile: String): Unit = {
    val previousFile = previousFileName.get
    if (previousFile != null) {
      val file = new File(previousFile)
      file.delete
    }
    previousFileName.set(resultsFile)
  }

  def writeCsv(generations: Generations, tuning: Tuning): Generations = {
    val resultsFile = writeCsv(generations.parallelRun, generations.seriesRun, generations, tuning)
    deletePreviousFile(resultsFile)
    generations
  }

  def createResultFileName(id: String): String = "allgenerations" + "-" + id + ".csv"
}
