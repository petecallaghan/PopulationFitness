package uk.edu.populationfitness.models.output

import java.io.FileReader

import com.opencsv.CSVReader
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.tuning.Tuning

object TuningReader {
  def read(file: String): Tuning = {
    val reader = new CSVReader(new FileReader(file))
    reader.readNext // read the header
    read(reader)
  }

  private def read(reader: CSVReader): Tuning = {
    val tuning = new Tuning
    val row = reader.readNext
    tuning.function = FitnessFunction.withName(row(0))
    tuning.historic_fit = row(1).toDouble
    tuning.disease_fit = row(2).toDouble
    tuning.modern_fit = row(3).toDouble
    tuning.modern_breeding = row(4).toDouble
    tuning.size_of_genes = row(5).toInt
    tuning.number_of_genes = row(6).toInt
    tuning.mutations_per_gene = row(7).toDouble
    tuning.series_runs = row(8).toInt
    tuning.parallel_runs = row(9).toInt
    tuning
  }
}
