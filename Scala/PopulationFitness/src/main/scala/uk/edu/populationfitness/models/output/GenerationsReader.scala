package uk.edu.populationfitness.models.output

import java.io.FileReader

import com.opencsv.CSVReader
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.history.{Epoch, GenerationStatistics}

import scala.collection.mutable.ArrayBuffer

object GenerationsReader {
  def readGenerations(config: Config, path: String): Seq[GenerationStatistics] = {
    val generations = new ArrayBuffer[GenerationStatistics]
    val reader: CSVReader = new CSVReader(new FileReader(path))
    // Read header
    reader.readNext
    // Read until we run out of readable data
    var row = reader.readNext
    while ( row != null) {
      generations += readFromRow(config, row)
      row = reader.readNext
    }

    generations
  }

  private def readFromRow(config: Config, row: Seq[String]): GenerationStatistics = {
    val generation: GenerationStatistics = new GenerationStatistics(new Epoch(config,
      row(0).toInt),
      row(5).toInt,
      row(8).toInt,
      row(9).toInt,
      row(10).toInt,
      (row(11).toDouble * 1000).toInt,
      (row(12).toDouble * 1000).toInt,
      row(16).toDouble,
      row(18).toDouble)
    generation.epoch.endYear = row(1).toInt
    generation.epoch.environmentCapacity = row(2).toDouble.toInt
    generation.epoch.enableFitness =row(3).toBoolean
    generation.epoch.breedingProbability(row(4).toDouble)
    generation.epoch.fitnessFactor(row(6).toDouble)
    generation.epoch.expectedMaxPopulation = row(7).toInt
    generation.averageFitness = row(13).toDouble
    generation.fitnessDeviation = row(14).toDouble
    generation.averageAge = row(15).toDouble
    generation.averageFactoredFitness = row(17).toDouble
    generation.averageLifeExpectancy = row(19).toDouble
    generation
  }
}
