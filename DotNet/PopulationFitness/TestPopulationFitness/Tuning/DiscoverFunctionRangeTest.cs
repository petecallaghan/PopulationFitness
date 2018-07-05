using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using PopulationFitness.Models.Genes.BitSet;
using PopulationFitness.Models.Genes.Performance;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using Xunit;

namespace TestPopulationFitness.Tuning
{
    public class DiscoverFunctionRangeTest
    {

        private const int NumberOfGenes = 20000;

        private const int SizeOfGenes = 1000;

        private const int PopulationSize = 10;

        [Theory]
        [InlineData(Function.Ackleys)]
        [InlineData(Function.Alpine)]
        [InlineData(Function.Brown)]
        [InlineData(Function.ChungReynolds)]
        [InlineData(Function.DixonPrice)]
        [InlineData(Function.Exponential)]
        [InlineData(Function.Griewank)]
        [InlineData(Function.Qing)]
        [InlineData(Function.Rastrigin)]
        [InlineData(Function.Rosenbrock)]
        [InlineData(Function.Salomon)]
        [InlineData(Function.SchumerSteiglitz)]
        [InlineData(Function.Schwefel220)]
        [InlineData(Function.Schwefel226)]
        [InlineData(Function.Sphere)]
        [InlineData(Function.StyblinksiTang)]
        [InlineData(Function.SumOfPowers)]
        [InlineData(Function.SumSquares)]
        [InlineData(Function.Trid)]
        [InlineData(Function.Zakharoy)]
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

            Debug.WriteLine(function.ToString());
            Debug.Write("(");
            Debug.Write(NumberOfGenes);
            Debug.Write(") min=");
            Debug.Write(min);
            Debug.Write(" max=");
            Debug.WriteLine(max);

            GenesTimer.ShowAll();

            Assert.True(min >= -0.1, "Min above zero");
            Assert.True(max - min >= 0.01, "Usable range");
        }
    }
}
