using PopulationFitness.Models;
using PopulationFitness.Models.Genes.BitSet;
using PopulationFitness.Models.Genes.LocalMinima;
using NUnit.Framework;

namespace TestPopulationFitness.UnitTests
{
    [TestFixture]
    public class GenesTest
    {
        [TestCase]
        public void TestGenesAreEmpty()
        {
            // Given a set of genes with zero value
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildEmpty();

            // When tested they are all empty
            Assert.True(genes.AreEmpty);
        }

        [TestCase]
        public void TestMutatedGenesAreNotAllZero()
        {
            // Given a set of genes that are empty and a high probability that they will mutate
            Config config = new Config();
            config.MutationsPerGene = 100;
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildEmpty();

            // When they are mutated
            genes.Mutate();

            // Then the number of bits mutated falls inside the probability range
            int mutated_count = 0;
            for (int i = 0; i < genes.NumberOfBits; i++)
            {
                if (genes.GetCode(i) == 1)
                {
                    mutated_count++;
                }
            }

            Assert.True(mutated_count > 0);
            Assert.True(mutated_count <= 2.5 * config.MutationsPerGene);
        }

        [TestCase]
        public void TestRandomGenesAreNotAllZero()
        {
            // Given a set of genes that are random
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildFromRandom();

            // Then the number of bits set falls inside the probability range
            int set_count = 0;
            for (int i = 0; i < genes.NumberOfBits; i++)
            {
                if (genes.GetCode(i) == 1)
                {
                    set_count++;
                }
            }

            Assert.True(set_count > 0.25 * genes.NumberOfBits);
            Assert.True(set_count < 0.75 * genes.NumberOfBits);
        }

        [TestCase]
        public void TestGenesAsIntegers()
        {
            // Given a set of genes that are random
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildFromRandom();

            // Then the genes as integers are non zero
            long[] integers = genes.AsIntegers;
            Assert.True(integers.Length >= 1);
            foreach (long integer in integers)
            {
                Assert.True(integer > 0);
            }
        }

        [TestCase]
        public void TestMutationCanBeDisabled()
        {
            // Given a set of genes with zero values that will not mutate
            Config config = new Config();
            config.MutationsPerGene = 0;
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildEmpty();

            // When they are mutated
            genes.Mutate();

            // Then none have changed
            int mutated_count = 0;
            for (int i = 0; i < genes.NumberOfBits; i++)
            {
                if (genes.GetCode(i) == 1)
                {
                    mutated_count++;
                }
            }

            Assert.AreEqual(0, mutated_count);
        }

        private void ThenTheyFallIntoTheFloatRange(BitSetGenes genes)
        {
            // Then they fall into the float range
            double fitness = genes.Fitness;
            Assert.True(0.0 <= fitness);
            Assert.True(1.0 >= fitness);
        }

        [TestCase]
        public void TestGenesAsFloatUseDefaultRange()
        {
            // Given a set of genes with non zero values
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildFromRandom();

            ThenTheyFallIntoTheFloatRange(genes);
        }

        [TestCase]
        public void TestGenesWithLargeBitCoding()
        {
            // Given a set of genes with non zero values
            Config config = new Config();
            config.NumberOfGenes = 10000;
            config.SizeOfEachGene = 2250;
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildFromRandom();

            ThenTheyFallIntoTheFloatRange(genes);
        }

        private BitSetGenes CreateFatherDifferentFromMother(Config config, BitSetGenes mother)
        {
            BitSetGenes father = new AckleysGenes(config);
            father.BuildFromRandom();

            while (father.IsEqual(mother))
            {
                father.Mutate();
            }
            return father;
        }

        [TestCase]
        public void TestBabyIsNotIdenticalToMotherOrFather()
        {
            // Given a mother with some mutated genes, a father with some mutated genes and a baby
            Config config = new Config();
            config.MutationsPerGene = (config.MutationsPerGene * 100000);
            BitSetGenes mother = new AckleysGenes(config);
            mother.BuildFromRandom();
            BitSetGenes father = CreateFatherDifferentFromMother(config, mother);
            BitSetGenes baby = new AckleysGenes(config);

            // When the baby inherits from the mother and father
            int mutated = baby.InheritFrom(mother, father);

            // Then the baby's genes are different to both
            Assert.False(baby.IsEqual(mother));
            Assert.False(baby.IsEqual(father));
            Assert.AreNotEqual(0, mutated);
        }

        [TestCase]
        public void TestBabyIsNotZero()
        {
            // Given a mother with some mutated genes, a father with some mutated genes and a baby
            Config config = new Config();
            config.MutationsPerGene = (config.MutationsPerGene * 10);
            BitSetGenes mother = new AckleysGenes(config);
            mother.BuildFromRandom();
            BitSetGenes father = CreateFatherDifferentFromMother(config, mother);
            BitSetGenes baby = new AckleysGenes(config);

            // When the baby inherits from the mother and father
            baby.InheritFrom(mother, father);

            // Then the baby's genes are non zero
            Assert.False(baby.AreEmpty);
        }

        [TestCase]
        public void TestBabyIsSimilarToMotherAndFather()
        {
            // Given a mother with some mutated genes, a father with some mutated genes and a baby
            Config config = new Config();
            config.MutationsPerGene = config.MutationsPerGene * 10;
            BitSetGenes mother = new AckleysGenes(config);
            mother.BuildFromRandom();
            BitSetGenes father = CreateFatherDifferentFromMother(config, mother);
            BitSetGenes baby = new AckleysGenes(config);

            // When the baby inherits from the mother and father
            baby.InheritFrom(mother, father);

            bool similar_to_father = false;
            bool similar_to_mother = false;
            // Then the baby's genes have some similarity to both
            for (int i = 0; i < baby.NumberOfBits; i++)
            {
                if (baby.GetCode(i) == mother.GetCode(i))
                {
                    similar_to_mother = true;
                }
                if (baby.GetCode(i) == father.GetCode(i))
                {
                    similar_to_father = true;
                }
            }
            Assert.True(similar_to_mother);
            Assert.True(similar_to_father);
        }
    }
}
