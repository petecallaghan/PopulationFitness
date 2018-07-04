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
            expected.function = Function.Ackleys;
            expected.historic_fit = 0.1;
            expected.disease_fit = 0.3;
            expected.modern_breeding = 0.4;
            expected.modern_fit = 0.5;
            expected.number_of_genes = 7;
            expected.size_of_genes = 8;
            expected.mutations_per_gene = 10;
            expected.series_runs = 5;
            expected.parallel_runs = 2;
            TuningWriter.Write(expected, Paths.PathOf("test.csv"));
            TuningReader.Read(actual, Paths.PathOf("test.csv"));
            int delta = 10;

            Assert.Equal(expected.function, actual.function);
            Assert.Equal(expected.historic_fit, actual.historic_fit, delta);
            Assert.Equal(expected.disease_fit, actual.disease_fit, delta);
            Assert.Equal(expected.modern_breeding, actual.modern_breeding, delta);
            Assert.Equal(expected.modern_fit, actual.modern_fit, delta);
            Assert.Equal(expected.number_of_genes, actual.number_of_genes);
            Assert.Equal(expected.size_of_genes, actual.size_of_genes);
            Assert.Equal(expected.mutations_per_gene, actual.mutations_per_gene, delta);
            Assert.Equal(expected.series_runs, actual.series_runs);
            Assert.Equal(expected.parallel_runs, actual.parallel_runs);
        }
    }
}
