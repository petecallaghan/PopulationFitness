package uk.edu.populationfitness.models.genes.bitset

import uk.edu.populationfitness.models.Config
import uk.edu.populationfitness.models.genes.{Encoding, Mutatable}
import uk.edu.populationfitness.models.maths.RepeatableRandom

import scala.collection.mutable

object BitSetGenes {
  private def build(config: Config, filler: (Long)=> Long) : BitSetGenes = {
    val numberOfInts = (config.geneBitCount / java.lang.Long.SIZE) + (if (java.lang.Long.SIZE % config.geneBitCount == 0) 0 else 1)
    new BitSetGenes(config, new Array[Long](numberOfInts).map(filler))
  }

  def buildEmpty(config: Config): BitSetGenes = {
    build(config, (_) => {0L})
  }

  def buildFull(config: Config): BitSetGenes = {
    build(config, (_) => {1L})
  }

  def buildRandom(config: Config): BitSetGenes = {
    val max = config.maxGeneValue
    val lastMax = config.lastMaxGeneValue
    val last = config.geneBitCount - 1
    build(config, (i) => { RepeatableRandom.generateNextLong(0, if (i == last) lastMax else max) })
  }

  def inheritFrom(mother: BitSetGenes, father: BitSetGenes): BitSetGenes = {
    val motherEncoding = mother.asIntegers
    val fatherEncoding = father.asIntegers
    var baby = new BitSetGenes(mother config, _genes = new Array[Long](Math.max(motherEncoding.size, fatherEncoding.size)))
    // Randomly picks the code index that crosses over from mother to father
    val crossOverWord = math.min(1 + RepeatableRandom.generateNextInt(baby._genes.length - 1), baby._genes.length - 1)
    val motherLength = math.min(crossOverWord + 1, motherEncoding.size)
    val fatherLength = math.min(baby._genes.size - crossOverWord - 1, fatherEncoding.size - crossOverWord - 1)
    System.arraycopy(motherEncoding, 0, baby._genes, 0, motherLength)
    if (fatherLength > 0) System.arraycopy(fatherEncoding, crossOverWord + 1, baby._genes, crossOverWord + 1, fatherLength)
    baby.mutate
    baby
  }

  private def mutatedValue(gene: Long, max: Long) = gene + RepeatableRandom.generateNextLong(0 - gene, max)
}

class BitSetGenes private[bitset](val config: Config, private val _genes: Array[Long]) extends Encoding with Mutatable{

  private var _mutationCount : Int = 0

  def mutations : Int = _mutationCount

  override def asIntegers: Array[Long] = _genes

  def getCode(index: Int): Int = {
    val set = new mutable.BitSet(_genes)
    if (set(index)) 1 else 0
  }

  override def mutate: Int = {
    if (config.mutationsPerGene <= 0) return 0
    val mutation_genes_interval = 1 + (_genes.length * 2.0 / config.mutationsPerGene).asInstanceOf[Long]
    mutateGenesWithMutationInterval(config, mutation_genes_interval)
  }

  private def mutateGenesWithMutationInterval(config: Config, mutation_genes_interval: Long) = {
    val max = config.maxGeneValue
    val lastMax = config.lastMaxGeneValue
    val last = _genes.length - 1
    _mutationCount = 0
    var i = RepeatableRandom generateNextInt mutation_genes_interval
    while ( i < _genes.length ) {
      _genes(i) = BitSetGenes mutatedValue(_genes(i), if (i == last) lastMax else max)
      _mutationCount += 1
      i += Math.max(1, RepeatableRandom.generateNextLong(0, mutation_genes_interval).asInstanceOf[Int])
    }
    _mutationCount
  }

  def areEmpty: Boolean = {
    _genes.find(x => x != 0 ) match {
      case Some(x) => false
      case None => true
    }
  }

  /**
    * The number of bits in the encoding
    *
    * @return
    */
  override def numberOfBits: Int = config.geneBitCount

  /**
    *
    * @param encoding
    * @return true if the encoding is the same
    */
  override def isSame(encoding: Encoding): Boolean = {
    return _genes sameElements encoding.asIntegers
  }
}
