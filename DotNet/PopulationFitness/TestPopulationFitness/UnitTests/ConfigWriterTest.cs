using PopulationFitness;
using PopulationFitness.Models;
using PopulationFitness.Output;
using System.Threading;
using Xunit;

namespace TestPopulationFitness.UnitTests
{
    public class ConfigWriterTest
    {
        [Fact]
        public void TestWriter()
        {
            // Given a config
            PopulationFitness.Tuning tuning = new PopulationFitness.Tuning();

            // Write it out to a file and don't complain about it
            ConfigWriter.Write(tuning, Paths.PathOf("test.yaml"));
        }

        [Fact]
        public void TestUniqueConfigIdentifiers()
        {
            // Given two configs created at different times
            Config config1 = new Config();
            Thread.Sleep(1000);
            Config config2 = new Config();

            // Then they have unique encodings
            Assert.NotEqual(config1.id, config2.id);
        }
    }

}
