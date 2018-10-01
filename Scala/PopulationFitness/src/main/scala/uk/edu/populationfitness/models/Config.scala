package uk.edu.populationfitness.models

import java.time.Instant

import uk.edu.populationfitness.models.fastmaths.FastMaths
import uk.edu.populationfitness.models.genes.FitnessFunction
import uk.edu.populationfitness.models.genes.FitnessFunction.FitnessFunction

object Config {
  val MutationScale = 1.0 / 30.0
  private val SizeOfLong = java.lang.Long.SIZE
}

class Config {
  var id: String = Instant.now.toString.replaceAll(":", "-")

  private var _numberOfGenes = 4
  private var _geneBitCount = 0
  private var _sizeOfEachGene = 10
  private var _mutationsPerGene = 1.0
  private var _maxGeneValue:Long = 0
  private var _lastMaxGeneValue:Long = 0

  def numberOfGenes = _numberOfGenes
  def numberOfGenes_= (newValue: Int): Unit = {
    _numberOfGenes = newValue
    geneSizeUpdated
  }

  def sizeOfEachGene = _sizeOfEachGene
  def sizeOfEachGene_= (newValue: Int) : Unit = {
    _sizeOfEachGene = newValue
    geneSizeUpdated
  }

  def geneBitCount = _geneBitCount

  def mutationsPerGene = _mutationsPerGene
  def mutationsPerGene_= (newValue: Double): Unit = {
    _mutationsPerGene = newValue
    geneSizeUpdated
  }

  def scaleMutationsPerGeneFromBitCount(scale: Double): Unit = {
    mutationsPerGene = scale * _geneBitCount
  }

  def maxGeneValue = _maxGeneValue

  def lastMaxGeneValue = _lastMaxGeneValue

  private def geneSizeUpdated = {
    _geneBitCount = _numberOfGenes * _sizeOfEachGene
    var excessBits = _geneBitCount % Config.SizeOfLong;
    _lastMaxGeneValue = if (excessBits == 0) java.lang.Long.MAX_VALUE else math.min(java.lang.Long.MAX_VALUE, FastMaths.pow(2, excessBits).toLong - 1);
    _maxGeneValue = if (_geneBitCount < Config.SizeOfLong) _lastMaxGeneValue else java.lang.Long.MAX_VALUE;
  }

  var maxAge: Int = 90
  var maxBreedingAge: Int = 41
  var minBreedingAge: Int = 16
  var probabilityOfBreeding: Double = 0.35 // Derived from Max Crude Birth Rate W&S 1981 1730-2009
  var numberOfYears: Int = 2150
  var initialPopulation : Int = 4000

  {
    numberOfGenes = 4
    sizeOfEachGene = 10
    mutationsPerGene = 1
    scaleMutationsPerGeneFromBitCount(Config.MutationScale)
  }

  var fitnessFunction: FitnessFunction = FitnessFunction.Undefined
}