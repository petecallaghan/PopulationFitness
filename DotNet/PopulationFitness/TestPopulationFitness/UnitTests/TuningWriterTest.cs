using PopulationFitness;
using PopulationFitness.Models.Genes;
using PopulationFitness.Output;
using Xunit;

namespace TestPopulationFitness.UnitTests
{
    public class TuningWriterTest
    {
        [Fact]
        public void TestWrite()
        {
            var expected = new PopulationFitness.Tuning();
            var actual = new PopulationFitness.Tuning();
            expected.Function = Function.Ackleys;
            expected.HistoricFit = 0.1;
            expected.DiseaseFit = 0.3;
            expected.ModernBreeding = 0.4;
            expected.ModernFit = 0.5;
            expected.NumberOfGenes = 7;
            expected.SizeOfGenes = 8;
            expected.MutationsPerGene = 10;
            expected.SeriesRuns = 5;
            expected.ParallelRuns = 2;
            TuningWriter.Write(expected, Paths.PathOf("test.csv"));
            TuningReader.Read(actual, Paths.PathOf("test.csv"));
            int delta = 10;

            Assert.Equal(expected.Function, actual.Function);
            Assert.Equal(expected.HistoricFit, actual.HistoricFit, delta);
            Assert.Equal(expected.DiseaseFit, actual.DiseaseFit, delta);
            Assert.Equal(expected.ModernBreeding, actual.ModernBreeding, delta);
            Assert.Equal(expected.ModernFit, actual.ModernFit, delta);
            Assert.Equal(expected.NumberOfGenes, actual.NumberOfGenes);
            Assert.Equal(expected.SizeOfGenes, actual.SizeOfGenes);
            Assert.Equal(expected.MutationsPerGene, actual.MutationsPerGene, delta);
            Assert.Equal(expected.SeriesRuns, actual.SeriesRuns);
            Assert.Equal(expected.ParallelRuns, actual.ParallelRuns);
        }
    }
}
