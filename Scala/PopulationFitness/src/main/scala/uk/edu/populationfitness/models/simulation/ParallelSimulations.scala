package uk.edu.populationfitness.models.simulation

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.history.{Epochs, Generations}
import uk.edu.populationfitness.models.output.GenerationsWriter
import uk.edu.populationfitness.models.tuning.Tuning

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._

class ParallelSimulations(val config: Config, val epochs: Epochs, val tuning: Tuning) {
  val resultsFile = GenerationsWriter createResultFileName(tuning.id)

  private def resultsForRun(run: Int) =
    SerialSimulations writtenGeneratedResults(config, epochs, tuning, run)

  private def allSimulations = {
    val simulations = new ListBuffer[Future[Generations]]()
    for(run <- 1 to tuning.parallel_runs){
        simulations += Future[Generations]{
          resultsForRun(run)
        }
    }
    List concat(simulations)
  }

  private def writtenAccumulatedResults(total: Generations, next: Generations)  =
    GenerationsWriter writeCsv(total + next, tuning)

  def runAllAndWriteResults() = {
    val zero = Generations(config)

    val allGenerations = Future.foldLeft (allSimulations) (zero) (writtenAccumulatedResults)

    val total = Await.result(allGenerations, 30 days)

    GenerationsWriter writeCsv(resultsFile, total, tuning)

    total
  }
}
