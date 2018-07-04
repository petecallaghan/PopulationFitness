using PopulationFitness.Models;
using PopulationFitness.Models.Genes.BitSet;
using PopulationFitness.Models.Genes.LocalMinima;
using Xunit;

namespace TestPopulationFitness.UnitTests
{
    public class GenesTest
    {
        [Fact]
        public void TestGenesAreEmpty()
        {
            // Given a set of genes with zero value
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildEmpty();

            // When tested they are all empty
            Assert.True(genes.AreEmpty());
        }

        [Fact]
        public void TestMutatedGenesAreNotAllZero()
        {
            // Given a set of genes that are empty and a high probability that they will mutate
            Config config = new Config();
            config.SetMutationsPerGene(100);
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildEmpty();

            // When they are mutated
            genes.Mutate();

            // Then the number of bits mutated falls inside the probability range
            int mutated_count = 0;
            for (int i = 0; i < genes.NumberOfBits(); i++)
            {
                if (genes.GetCode(i) == 1)
                {
                    mutated_count++;
                }
            }

            Assert.True(mutated_count > 0);
            Assert.True(mutated_count <= 2.5 * config.GetMutationsPerGene());
        }

        [Fact]
        public void TestRandomGenesAreNotAllZero()
        {
            // Given a set of genes that are random
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildFromRandom();

            // Then the number of bits set falls inside the probability range
            int set_count = 0;
            for (int i = 0; i < genes.NumberOfBits(); i++)
            {
                if (genes.GetCode(i) == 1)
                {
                    set_count++;
                }
            }

            Assert.True(set_count > 0.25 * genes.NumberOfBits());
            Assert.True(set_count < 0.75 * genes.NumberOfBits());
        }

        [Fact]
        public void TestGenesAsIntegers()
        {
            // Given a set of genes that are random
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildFromRandom();

            // Then the genes as integers are non zero
            long[] integers = genes.AsIntegers();
            Assert.True(integers.Length >= 1);
            foreach (long integer in integers)
            {
                Assert.True(integer > 0);
            }
        }

        [Fact]
        public void TestMutationCanBeDisabled()
        {
            // Given a set of genes with zero values that will not mutate
            Config config = new Config();
            config.SetMutationsPerGene(0);
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildEmpty();

            // When they are mutated
            genes.Mutate();

            // Then none have changed
            int mutated_count = 0;
            for (int i = 0; i < genes.NumberOfBits(); i++)
            {
                if (genes.GetCode(i) == 1)
                {
                    mutated_count++;
                }
            }

            Assert.Equal(0, mutated_count);
        }

        private void ThenTheyFallIntoTheFloatRange(BitSetGenes genes)
        {
            // Then they fall into the float range
            double fitness = genes.Fitness();
            Assert.True(0.0 <= fitness);
            Assert.True(1.0 >= fitness);
        }

        [Fact]
        public void TestGenesAsFloatUseDefaultRange()
        {
            // Given a set of genes with non zero values
            Config config = new Config();
            BitSetGenes genes = new AckleysGenes(config);
            genes.BuildFromRandom();

            ThenTheyFallIntoTheFloatRange(genes);
        }

        [Fact]
        public void TestGenesWithLargeBitCoding()
        {
            // Given a set of genes with non zero values
            Config config = new Config();
            config.SetNumberOfGenes(10000);
            config.SetSizeOfEachGene(2250);
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

        [Fact]
        public void TestBabyIsNotIdenticalToMotherOrFather()
        {
            // Given a mother with some mutated genes, a father with some mutated genes and a baby
            Config config = new Config();
            config.SetMutationsPerGene(config.GetMutationsPerGene() * 100000);
            BitSetGenes mother = new AckleysGenes(config);
            mother.BuildFromRandom();
            BitSetGenes father = CreateFatherDifferentFromMother(config, mother);
            BitSetGenes baby = new AckleysGenes(config);

            // When the baby inherits from the mother and father
            int mutated = baby.InheritFrom(mother, father);

            // Then the baby's genes are different to both
            Assert.False(baby.IsEqual(mother));
            Assert.False(baby.IsEqual(father));
            Assert.NotEqual(0, mutated);
        }

        [Fact]
        public void TestBabyIsNotZero()
        {
            // Given a mother with some mutated genes, a father with some mutated genes and a baby
            Config config = new Config();
            config.SetMutationsPerGene(config.GetMutationsPerGene() * 10);
            BitSetGenes mother = new AckleysGenes(config);
            mother.BuildFromRandom();
            BitSetGenes father = CreateFatherDifferentFromMother(config, mother);
            BitSetGenes baby = new AckleysGenes(config);

            // When the baby inherits from the mother and father
            baby.InheritFrom(mother, father);

            // Then the baby's genes are non zero
            Assert.False(baby.AreEmpty());
        }

        [Fact]
        public void TestBabyIsSimilarToMotherAndFather()
        {
            // Given a mother with some mutated genes, a father with some mutated genes and a baby
            Config config = new Config();
            config.SetMutationsPerGene(config.GetMutationsPerGene() * 10);
            BitSetGenes mother = new AckleysGenes(config);
            mother.BuildFromRandom();
            BitSetGenes father = CreateFatherDifferentFromMother(config, mother);
            BitSetGenes baby = new AckleysGenes(config);

            // When the baby inherits from the mother and father
            baby.InheritFrom(mother, father);

            bool similar_to_father = false;
            bool similar_to_mother = false;
            // Then the baby's genes have some similarity to both
            for (int i = 0; i < baby.NumberOfBits(); i++)
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
