﻿using PopulationFitness.Models;
using PopulationFitness.Models.Genes;
using PopulationFitness.Models.Genes.BitSet;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class GenesDistributionTest
    {

        private const int Population = 40000;

        private const int NumberOfGenes = 100;

        private const int SizeOfGenes = 10;

        [TestCase(Function.Schwefel220, 1.0)]
        [TestCase(Function.SchumerSteiglitz, 1.0)]
        [TestCase(Function.Qing, 1.0)]
        [TestCase(Function.Griewank, 1.0)]
        [TestCase(Function.Exponential, 1.0)]
        [TestCase(Function.DixonPrice, 1.0)]
        [TestCase(Function.ChungReynolds, 1.0)]
        [TestCase(Function.Brown, 1.0)]
        [TestCase(Function.Alpine, 1.0)]
        [TestCase(Function.Ackleys, 1.0)]
        [TestCase(Function.SumOfPowers, 0.05)]
        [TestCase(Function.Rastrigin, 1.0)]
        [TestCase(Function.StyblinksiTang, 1.0)]
        [TestCase(Function.Rosenbrock, 1.0)]
        [TestCase(Function.Schwefel226, 0.01)]
        [TestCase(Function.Sphere, 1.0)]
        [TestCase(Function.SumSquares, 1.0)]
        public void GenesAreDistributedWithoutExcessiveSpikes(Function function, double fitness_factor)
        {
            BitSetGenesFactory factory = new BitSetGenesFactory();
            RepeatableRandom.ResetSeed();

            factory.FitnessFunction = function;
            // Given a number of randomly generated genes
            Config config = new Config
            {
                NumberOfGenes = NumberOfGenes,
                SizeOfEachGene = SizeOfGenes
            };

            var genes = new List<IGenes>();
            for (int i = 0; i < Population; i++)
            {
                var next = factory.Build(config);
                next.BuildFromRandom();
                genes.Add(next);
            }

            // When the fitnesses are counted into a distribution
            var fitnesses = new int[100];
            for (int i = 0; i < fitnesses.Length; i++)
            {
                fitnesses[i] = 0;
            }

            foreach (IGenes g in genes)
            {
                double fitness = g.Fitness * fitness_factor;
                int i = Math.Abs(Math.Min(99, (int)(fitness * 100)));
                fitnesses[i]++;
            }

            // Then the gene fitness is distributed without excessive spikes
            Debug.WriteLine(function.ToString());
            foreach (int f in fitnesses)
            {
                Debug.WriteLine(f);
                Assert.True(f < 20 * (genes.Count / fitnesses.Length));
            }
        }
    }
}
