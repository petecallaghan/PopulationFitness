using PopulationFitness;
using PopulationFitness.Models.Genes;
using PopulationFitness.Output;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class TuningWriterTest
    {
        [TestCase]
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
            double delta = 1.0E-10;

            Assert.AreEqual(expected.Function, actual.Function);
            Assert.AreEqual(expected.HistoricFit, actual.HistoricFit, delta);
            Assert.AreEqual(expected.DiseaseFit, actual.DiseaseFit, delta);
            Assert.AreEqual(expected.ModernBreeding, actual.ModernBreeding, delta);
            Assert.AreEqual(expected.ModernFit, actual.ModernFit, delta);
            Assert.AreEqual(expected.NumberOfGenes, actual.NumberOfGenes);
            Assert.AreEqual(expected.SizeOfGenes, actual.SizeOfGenes);
            Assert.AreEqual(expected.MutationsPerGene, actual.MutationsPerGene, delta);
            Assert.AreEqual(expected.SeriesRuns, actual.SeriesRuns);
            Assert.AreEqual(expected.ParallelRuns, actual.ParallelRuns);
        }
    }
}
