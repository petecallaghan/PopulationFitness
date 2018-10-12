package uk.edu.populationfitness.models.output

import java.io.{File, FileWriter}

import com.opencsv.CSVWriter
import uk.edu.populationfitness.models.history.{Generations, GenerationsAnalysis}
import uk.edu.populationfitness.models.tuning.Tuning

object AnalysisWriter {
  var Headers: Array[String] = Array[String](
    "Tuning File",
    "Epochs File",
    "Generations File",
    "Max Expected Population",
    "Historical Disease Population",
    "Historical Disease Killed",
    "Historical Disease Death Rate",
    "Modern Disease Population",
    "Modern Disease Killed",
    "Modern Disease Death Rate",
    "Total Born",
    "Total Killed")

  private val _headers = Headers ++ TuningWriter.Headers

  private def toRow(tuningName: String, epochsName: String, generationsName: String, analysis: GenerationsAnalysis) = Array[String](
    tuningName,
    epochsName,
    generationsName,
    analysis.maxExpectedPopulation.toString,
    analysis.historicalPopulationBeforeDisease.toString,
    analysis.historicalDiseaseTotalDeaths.toString,
    ((analysis.historicalDiseaseTotalDeaths * 100L) / analysis.historicalPopulationBeforeDisease).toString,
    analysis.modernPopulationBeforeDisease.toString,
    analysis.modernDiseaseTotalDeaths.toString,
    ((analysis.modernDiseaseTotalDeaths * 100L) / analysis.modernPopulationBeforeDisease).toString,
    analysis.totalBorn.toString,
    analysis.totalKilled.toString)

  private def fileExists(path: String): Boolean = {
    try
      return new File(path).exists
    catch {
      case ignored: Exception =>
    }
    false
  }

  /**
    * Appends the analysis of the generations to the specified analysis file path, as a CSV
    *
    * @param analysisCsvFilePath
    * @param tuningName
    * @param epochsName
    * @param generationsName
    * @param tuning
    * @param generations
    */
  def append(analysisCsvFilePath: String, tuningName: String, epochsName: String, generationsName: String, tuning: Tuning, generations: Generations): Unit = {
    val analysis = new GenerationsAnalysis(generations.history)
    val addHeader = !fileExists(analysisCsvFilePath)
    val writer = new CSVWriter(new FileWriter(analysisCsvFilePath, true), ',')
    if (addHeader) writer.writeNext(_headers)
    writer.writeNext(toRow(tuningName, epochsName, generationsName, analysis) ++ TuningWriter.toRow(tuning))
    writer.close()
  }
}
