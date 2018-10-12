package uk.edu.populationfitness.models.history

class GenerationsAnalysis private {

  private var maxPop: Long = 0
  private var born: Long = 0
  private var killed: Long = 0
  private var previousPopulation: Long = 0
  private var historicalPop: Long = 0
  private var modernPop: Long = 0
  private var historicalKilled: Long = 0
  private var modernKilled: Long = 0

  private var historicalDisease: Option[Epoch] = None
  private var modernDisease: Option[Epoch] = None

  def maxExpectedPopulation = maxPop
  def historicalPopulationBeforeDisease = historicalPop
  def historicalDiseaseTotalDeaths = historicalKilled
  def modernPopulationBeforeDisease = modernPop
  def modernDiseaseTotalDeaths = modernKilled
  def totalBorn = born
  def totalKilled = killed

  def this(statistics: Seq[GenerationStatistics]) {
    this

    statistics.foldLeft(this)(_.add(_))
  }

  private def ensureHistoricalDefined(epoch: Epoch): Unit = {
    historicalDisease match {
      case None => {
        historicalDisease = Some(epoch)
        historicalPop = previousPopulation
        historicalKilled = 0
      }
      case _ =>
    }
  }

  private def ensureModernDefined(epoch: Epoch): Unit = {
    historicalDisease match {
      case None => {
        modernDisease = Some(epoch)
        modernPop = previousPopulation
        modernKilled = 0
      }
      case _ =>
    }
  }

  private def add(generation: GenerationStatistics): GenerationsAnalysis = {
    maxPop = Math.max(maxPop, generation.epoch.expectedMaxPopulation)
    born += generation.numberBorn
    killed += generation.numberKilled

    if (generation.epoch.disease) {
      ensureHistoricalDefined(generation.epoch)

      if (historicalDisease.get.startYear == generation.epoch.startYear) { // Historical disease
        historicalKilled += generation.numberKilled
      }
      else { // Modern disease
        ensureModernDefined(generation.epoch)

        if (modernDisease.get.startYear == generation.epoch.startYear)
          modernKilled += generation.numberKilled
        else { // Reset to later epoch
          modernDisease = Some(generation.epoch)
          modernPop = previousPopulation
          modernKilled = generation.numberKilled
        }
      }
    }
    previousPopulation = generation.population
    this
  }
}
