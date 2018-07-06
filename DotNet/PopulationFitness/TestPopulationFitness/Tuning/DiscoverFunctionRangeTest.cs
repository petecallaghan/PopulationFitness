using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using PopulationFitness.Models.Genes.BitSet;
using PopulationFitness.Models.Genes.Performance;
using System;
using System.Collections.Generic;
using NUnit.Framework;

namespace TestPopulationFitness.Tuning
{
    [TestFixture]
    public class DiscoverFunctionRangeTest
    {
        private const int NumberOfGenes = 20000;
        private const int SizeOfGenes = 1000;
        private const int PopulationSize = 10;

        [TestCase(Function.Ackleys)]
        [TestCase(Function.Alpine)]
        [TestCase(Function.Brown)]
        [TestCase(Function.ChungReynolds)]
        [TestCase(Function.DixonPrice)]
        [TestCase(Function.Exponential)]
        [TestCase(Function.Griewank)]
        [TestCase(Function.Qing)]
        [TestCase(Function.Rastrigin)]
        [TestCase(Function.Rosenbrock)]
        [TestCase(Function.Salomon)]
        [TestCase(Function.SchumerSteiglitz)]
        [TestCase(Function.Schwefel220)]
        [TestCase(Function.Schwefel226)]
        [TestCase(Function.Sphere)]
        [TestCase(Function.StyblinksiTang)]
        [TestCase(Function.SumOfPowers)]
        [TestCase(Function.SumSquares)]
        [TestCase(Function.Trid)]
        [TestCase(Function.Zakharoy)]
        public void Discover(Function function)
        {
            var factory = new GenesTimerFactory(new BitSetGenesFactory())
            {
                FitnessFunction = function
            };
            // Given a number of randomly generated genes
            Config config = new Config
            {
                NumberOfGenes = NumberOfGenes,
                SizeOfEachGene = SizeOfGenes,
                GenesFactory = factory
            };
            GenesTimer.ResetAll();

            var genes = new List<IGenes>();

            var empty = factory.Build(config);
            empty.BuildEmpty();
            genes.Add(empty);

            var full = factory.Build(config);
            full.BuildFull();
            genes.Add(full);

            for (int i = 0; i < PopulationSize; i++)
            {
                var next = factory.Build(config);
                next.BuildFromRandom();
                next.Mutate();
                genes.Add(next);
            }

            double min = double.MaxValue;
            double max = double.MinValue;
            foreach (var g in genes)
            {
                double fitness = g.Fitness;

                if (fitness < min) min = fitness;
                if (fitness > max) max = fitness;
            }

            Console.WriteLine(function.ToString());
            Console.Write("(");
            Console.Write(NumberOfGenes);
            Console.Write(") min=");
            Console.Write(min);
            Console.Write(" max=");
            Console.WriteLine(max);

            GenesTimer.ShowAll();

            Assert.True(min >= -0.1, "Min above zero");
            Assert.True(max - min >= 0.01, "Usable range");
        }
    }
}
