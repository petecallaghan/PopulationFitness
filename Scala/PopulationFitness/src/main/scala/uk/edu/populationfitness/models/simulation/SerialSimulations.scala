package uk.edu.populationfitness.models.simulation

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.history.{Epochs, Generations}
import uk.edu.populationfitness.models.output.GenerationsWriter
import uk.edu.populationfitness.models.tuning.Tuning

object SerialSimulations {
  def writtenGeneratedResults(config: Config, epochs: Epochs, tuning: Tuning, parallelRun: Int): Generations = {
    var total = Generations(config)

    for(run <- 1 to tuning.series_runs){
      total = total + Generations(config, epochs, run, parallelRun)
      GenerationsWriter writeCsv(total, tuning)
    }

    total
  }
}
