package uk.edu.populationfitness.models.output

import java.io.{File, FileWriter}

import com.opencsv.CSVWriter
import uk.edu.populationfitness.models.genes.FitnessFunction.FitnessFunction
import uk.edu.populationfitness.models.history.{Epoch, Epochs}

object EpochsWriter{

  def writeCsv(path: String, function: FitnessFunction, genes: Int, size: Int, mutations: Double, epochs: Epochs): String = {
    val filePath = path + "/" + "functionepochs" + "-" + function + "-" + genes + "-" + size + "-" + mutations.toInt + ".csv"
    deleteExisting(filePath)
    val writer: CSVWriter = new CSVWriter(new FileWriter(filePath), ',')
    addHeaderRow(writer)
    epochs.epochs.foreach(add(writer, _))
    writer.close()
    filePath
  }

  def deleteExisting(path: String): Unit = {
    try {
      val existing: File = new File(path)
      if (!(existing.delete)) {
        System.out.println("Could not delete " + path)
      }
    } catch {
      case ignored: Exception =>
    }
  }

  private def add(writer: CSVWriter, epoch: Epoch): Unit = {
    writer.writeNext(Array[String](
      epoch.startYear.toString,
      epoch.endYear.toString,
      epoch.environmentCapacity.toString,
      epoch.breedingProbability.toString,
      epoch.disease.toString,
      epoch.fitnessFactor.toString,
      epoch.expectedMaxPopulation.toString,
      epoch.maxAge.toString,
      epoch.maxBreedingAge.toString,
      epoch.averageCapacityFactor.toString,
      (epoch.averageCapacityFactor * epoch.fitnessFactor).toString,
      epoch.config.mutationsPerGene.toString))
  }

  private def addHeaderRow(writer: CSVWriter): Unit = {
    writer.writeNext(Array[String](
      "Start Year",
      "End Year",
      "Environment Capacity",
      "Breeding Probability",
      "Disease",
      "Fitness Factor",
      "Expected Max Population",
      "Max Age",
      "Max Breeding Age",
      "Avg Capacity Factor",
      "Avg Capacity Fitness",
      "Mutations"))
  }
}
