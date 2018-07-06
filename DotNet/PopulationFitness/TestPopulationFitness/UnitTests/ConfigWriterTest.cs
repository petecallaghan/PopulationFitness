using PopulationFitness.Models;
using PopulationFitness.Output;
using System.Threading;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class ConfigWriterTest
    {
        [Test]
        public void TestWriter()
        {
            // Given a config
            PopulationFitness.Tuning tuning = new PopulationFitness.Tuning();

            // Write it out to a file and don't complain about it
            ConfigWriter.Write(tuning, Paths.PathOf("test.yaml"));
        }

        [Test]
        public void TestUniqueConfigIdentifiers()
        {
            // Given two configs created at different times
            Config config1 = new Config();
            Thread.Sleep(1000);
            Config config2 = new Config();

            // Then they have unique encodings
            Assert.AreNotEqual(config1.Id, config2.Id);
        }
    }

}
