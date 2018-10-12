package uk.edu.populationfitness.models.output

import java.io.FileWriter

import com.opencsv.CSVWriter
import uk.edu.populationfitness.models.tuning.Tuning

object TuningWriter {
  val Headers = Array[String](
    "Function",
    "Historic Fit",
    "Disease Fit",
    "Modern Fit",
    "Modern Breeding",
    "Size of Genes",
    "Number of Genes",
    "Mutations",
    "SeriesRuns",
    "ParallelRuns")

  def writeInPath(path: String, tuning: Tuning): Unit = {
    val filename = path + "/" + tuning.function.toString + "-" + tuning.number_of_genes + "-" + tuning.size_of_genes + ".csv"
    write(tuning, filename)
  }

  def write(tuning: Tuning, filename: String): Unit = {
    EpochsWriter.deleteExisting(filename)
    val writer = new CSVWriter(new FileWriter(filename), ',')
    writer.writeNext(Headers)
    writer.writeNext(toRow(tuning))
    writer.close()
  }

  def toRow(tuning: Tuning) = Array[String](
    tuning.function.toString,
    tuning.historic_fit.toString,
    tuning.disease_fit.toString,
    tuning.modern_fit.toString,
    tuning.modern_breeding.toString,
    tuning.size_of_genes.toString,
    tuning.number_of_genes.toString,
    tuning.mutations_per_gene.toString,
    tuning.series_runs.toString,
    tuning.parallel_runs.toString)
}
