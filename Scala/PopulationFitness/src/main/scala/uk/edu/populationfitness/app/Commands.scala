package uk.edu.populationfitness.app

import uk.edu.populationfitness.models.Config

import uk.edu.populationfitness.models.history.Epochs
import uk.edu.populationfitness.models.maths.RepeatableRandom
import uk.edu.populationfitness.models.output.{EpochsReader, TuningReader}
import uk.edu.populationfitness.models.tuning.Tuning

object Commands {
  val Seed = "-s"
  val TuningFile = "-t"
  val EpochsFile = "-e"
  val Id = "-i"
  val RandomSeed = "random"

  /**
    * Defines the path of the tuning file read from arguments.
    */
  var tuningFile = ""

  /**
    * Defines the path of the epochs file read from arguments.
    */
  var epochsFile = ""

  /**
    * The path and name of the experiments file
    */
  val experimentFile = "experiments.csv"

  def tuningAndEpochsFromInputFiles(config: Config, epochs: Epochs, args: Seq[String]): Tuning = {
    var tuning = new Tuning

    if (args.length < 2) showHelp()

    if (args.length % 2 == 1) showHelp()

    for(i <- 0 to args.length - 1 by 2) {
      val argument = args(i).toLowerCase
      val value = args(i + 1)
      try {
        if (argument.startsWith(Seed)) {
          val seed = getSeed(value)
          RepeatableRandom setSeed(seed)
        } else if (argument startsWith(TuningFile)) {
          tuningFile = value
          tuning = TuningReader read(tuningFile)
          config.sizeOfEachGene = tuning.size_of_genes
          config.numberOfGenes = tuning.number_of_genes
          config.fitnessFunction = tuning.function
          config.mutationsPerGene = tuning.mutations_per_gene
        } else if (argument startsWith(EpochsFile)) {
          epochsFile = value
          val read = EpochsReader readEpochs(config, epochsFile)
          epochs addAll(read)
        } else if (argument startsWith(Id)) {
          tuning.id = value
        } else {
          showHelp()
        }
      } catch {
        case e: Exception => System.out.print(e)
      }
    }
    tuning
  }

  private def getSeed(arg: String): Long = { // Will set the seed from the current time if 'random' is chosen
    if (arg.toLowerCase startsWith(RandomSeed)) System currentTimeMillis else arg toLong
  }

  private def showHelp(): Unit = {
    System.out.println("Commands:")
    System.out.println("    -s [seed number]| [random to set a seed from the current time]")
    System.out.println("    -t [csv file containing tuning]")
    System.out.println("    -e [csv file containing epochs]")
    System.out.println("    -i [simulation id - used to generate the name of the output files")
    System.exit(0)
  }
}