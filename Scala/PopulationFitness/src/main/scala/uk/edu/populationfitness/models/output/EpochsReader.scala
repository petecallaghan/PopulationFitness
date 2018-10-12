package uk.edu.populationfitness.models.output

import java.io.FileReader

import com.opencsv.CSVReader
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.history.Epoch

import scala.collection.mutable.ArrayBuffer

object EpochsReader {
  def readEpochs(config: Config, path: String): Seq[Epoch] = {
    val reader = new CSVReader(new FileReader(path))
    // Read header
    reader.readNext
    var row = reader.readNext
    val epochs = new ArrayBuffer[Epoch]
    while ( row != null) {
      epochs += readFromRow(config, row)
      row = reader.readNext
    }
    epochs
  }

  private def readFromRow(config: Config, row: Array[String]): Epoch = {
    val epoch = new Epoch(config, row(0).toInt)
    {
      endYear = row(1).toInt
      environmentCapacity = row(2).toInt
      breedingProbability(row(3).toDouble)
      disease(row(4).toBoolean)
      fitnessFactor = row(5).toDouble
      expectedMaxPopulation = row(6).toInt
      maxAge(row(7).toInt)
      maxBreedingAge(row(8).toInt)
    }
    epoch
  }
}