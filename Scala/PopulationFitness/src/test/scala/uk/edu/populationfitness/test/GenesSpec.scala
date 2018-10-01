package uk.edu.populationfitness.test

import org.scalatest.FunSpec
import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.genes.bitset.BitSetGenes
import uk.edu.populationfitness.models.genes.fitness.Fitness

class GenesSpec extends FunSpec{

  private def thenTheyFallIntoTheFloatRange(genes: BitSetGenes): Unit = {
    it("Then they fall into the float range") {
      val fitness = Fitness(genes.config)(genes)
      assert(0.0 <= fitness)
      assert(1.0 >= fitness)
    }
  }

  private def createFatherDifferentFromMother(config: Config, mother: BitSetGenes) = {
    val father = BitSetGenes.buildRandom(config)
    while ( father.isSame(mother))
      father.mutate
    father
  }

  describe("BitSet Genes"){
    describe("When created"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys}
      val genes = BitSetGenes.buildEmpty(config)

      it("When tested they are all empty"){
        assert(genes.areEmpty)
      }
    }

    describe("when mutated"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys; mutationsPerGene = 100 }
      val genes = BitSetGenes.buildEmpty(config)
      genes.mutate

      it("the number of bits mutated falls inside the probability range"){
        var mutated_count = 0
        for (i <- 0 to genes.numberOfBits) {
          if (genes.getCode(i) == 1) mutated_count += 1
        }

        assert(mutated_count > 0)
        assert(mutated_count <= 2.5 * config.mutationsPerGene)
      }
    }

    describe("random genes"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys}
      val genes = BitSetGenes.buildRandom(config)

      it("the number of bits set falls inside the probability range"){
        var set_count = 0
        var i = 0
        for (i <- 0 to genes.numberOfBits) {
          if (genes.getCode(i) == 1) set_count += 1
        }

        assert(set_count > 0.25 * genes.numberOfBits)
        assert(set_count < 0.75 * genes.numberOfBits)
      }
    }

    describe("Given a set of genes that are random"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys}
      val genes = BitSetGenes.buildRandom(config)

      it("then the genes as integers are non zero") {
        val integers = genes.asIntegers
        assert(integers.length >= 1)
        for (integer <- integers) {
          assert(integer > 0)
        }
      }
    }

    describe("Given a set of genes with zero values that will not mutate"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys; mutationsPerGene = 0 }
      val genes = BitSetGenes.buildEmpty(config)

      describe("When they are mutated"){
        genes.mutate

        it("Then none have changed") {
          var mutatedCount = 0
          for (i <- 0 to genes.numberOfBits){
            if (genes.getCode(i) == 1){
              mutatedCount += 1
            }
          }

          assert(0 == mutatedCount)
        }
      }
    }

    describe("Given a set of genes with non zero values"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys}
      val genes = BitSetGenes.buildRandom(config)

      thenTheyFallIntoTheFloatRange(genes)
    }

    describe("Given a set of genes with large gene encoding and non zero values"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys; numberOfGenes = 10000; sizeOfEachGene = 2250 }
      val genes = BitSetGenes.buildRandom(config)

      thenTheyFallIntoTheFloatRange(genes)
    }

    describe("Given a mother with some mutated genes, a father with some mutated genes and a baby"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys; mutationsPerGene *= 100000 }

      val mother = BitSetGenes.buildRandom(config)
      val father = createFatherDifferentFromMother(config, mother)

      describe("When the baby inherits from the mother and father"){
        val baby = BitSetGenes.inheritFrom(mother, father)

        it("Then the baby's genes are different to both"){
          assert(!baby.isSame(mother))
          assert(!baby.isSame(father))
          assert(baby.mutations > 0)
        }
      }
    }

    describe("Given a mother with some mutated genes, a father with some mutated genes and a baby"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys; mutationsPerGene *= 100000 }
      val mother = BitSetGenes.buildRandom(config)
      val father = createFatherDifferentFromMother(config, mother)

      describe("When the baby inherits from the mother and father"){
        val baby = BitSetGenes.inheritFrom(mother, father)

        it("Then the baby's genes are not zero"){
          assert(!baby.areEmpty)
        }
      }
    }

    describe("Given a mother with some mutated genes, a father with some mutated genes and a baby"){
      val config = new Config { fitnessFunction = FitnessFunction.Ackleys; mutationsPerGene *= 100000 }
      val mother = BitSetGenes.buildRandom(config)
      val father = createFatherDifferentFromMother(config, mother)

      describe("When the baby inherits from the mother and father"){
        val baby = BitSetGenes.inheritFrom(mother, father)

        it("Then the baby's genes have some similarity to both"){
          var similarToFather = false
          var similarToMother = false
          for( i <- 0 to baby.numberOfBits) {
            if (baby.getCode(i) == mother.getCode(i)) similarToMother = true
            if (baby.getCode(i) == father.getCode(i)) similarToFather = true
          }
          assert(similarToMother)
          assert(similarToFather)
        }
      }
    }
  }
}
