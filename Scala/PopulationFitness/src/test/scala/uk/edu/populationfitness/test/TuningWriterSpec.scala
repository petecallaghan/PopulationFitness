package uk.edu.populationfitness.test

import org.scalatest.FunSpec
import org.scalactic.Tolerance._
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.output.{TuningReader, TuningWriter}
import uk.edu.populationfitness.models.tuning.Tuning

class TuningWriterSpec extends FunSpec {
  describe("Given some tuning"){
    val expected = new Tuning{
      function = FitnessFunction.Ackleys
      historic_fit = 0.1
      disease_fit = 0.3
      modern_breeding = 0.4
      modern_fit = 0.5
      number_of_genes = 7
      size_of_genes = 8
      mutations_per_gene = 10
      series_runs = 5
      parallel_runs = 2
    }
    TuningWriter.write(expected, "test.csv")

    describe("When we write and then read"){
      val actual = TuningReader.read( "test.csv")
      
      it("the original values have been read"){
        val delta = 0.0000000001

        assert(expected.function == actual.function)
        assert(expected.historic_fit === actual.historic_fit +- delta)
        assert(expected.disease_fit === actual.disease_fit +- delta)
        assert(expected.modern_breeding === actual.modern_breeding +- delta)
        assert(expected.modern_fit === actual.modern_fit +- delta)
        assert(expected.number_of_genes == actual.number_of_genes)
        assert(expected.size_of_genes == actual.size_of_genes)
        assert(expected.mutations_per_gene === actual.mutations_per_gene +- delta)
        assert(expected.series_runs == actual.series_runs)
        assert(expected.parallel_runs == actual.parallel_runs)
      }
    }
  }
}
