package uk.edu.populationfitness.models.history

import uk.edu.populationfitness.models.Config

object UkPopulationEpochs {
  def define(ukConfig: Config): Epochs = 
    new Epochs {
      // Birth rates derived from from Crude Birth Rates of W&S 1981
      // Src: silvia.carpitella@unipa.it
      add(Epoch(ukConfig, -50).capacity(1500))
      add(Epoch(ukConfig, 1).capacity(2000))
      add(Epoch(ukConfig, 44).capacity(4500))
      add(Epoch(ukConfig, 201).capacity(3000))
      add(Epoch(ukConfig, 411).capacity(1250))
      add(Epoch(ukConfig, 431).capacity(1300))
      add(Epoch(ukConfig, 501).capacity(1300))
      add(Epoch(ukConfig, 751).capacity(1400))
      add(Epoch(ukConfig, 951).capacity(1500))
      add(Epoch(ukConfig, 1067).capacity(2000))
      add(Epoch(ukConfig, 1087).capacity(3700))
      add(Epoch(ukConfig, 1191).capacity(4800))
      add(Epoch(ukConfig, 1221).capacity(5000))
      add(Epoch(ukConfig, 1251).capacity(5300))
      add(Epoch(ukConfig, 1280).capacity(5700))
      add(Epoch(ukConfig, 1291).capacity(5600))
      add(Epoch(ukConfig, 1316).capacity(5000)) // Great Famine of 1315–1317

      add(Epoch(ukConfig, 1326).capacity(5700))
      add(Epoch(ukConfig, 1349).capacity(3000).disease(true)) // Black death

      add(Epoch(ukConfig, 1352).capacity(3000))
      add(Epoch(ukConfig, 1378).capacity(2500))
      add(Epoch(ukConfig, 1401).capacity(2400))
      add(Epoch(ukConfig, 1431).capacity(2300))
      add(Epoch(ukConfig, 1451).capacity(2600))
      add(Epoch(ukConfig, 1491).capacity(2800))
      add(Epoch(ukConfig, 1523).capacity(3400).breedingProbability(0.35))
      add(Epoch(ukConfig, 1542).capacity(3800).breedingProbability(0.35))
      add(Epoch(ukConfig, 1561).capacity(5000).breedingProbability(0.31))
      add(Epoch(ukConfig, 1601).capacity(6300).breedingProbability(0.29))
      add(Epoch(ukConfig, 1651).capacity(6200).breedingProbability(0.26))
      add(Epoch(ukConfig, 1701).capacity(11300).breedingProbability(0.25))
      add(Epoch(ukConfig, 1802).capacity(12500).breedingProbability(0.28))
      add(Epoch(ukConfig, 1812).capacity(13400).breedingProbability(0.28))
      add(Epoch(ukConfig, 1822).capacity(15800).breedingProbability(0.26))
      add(Epoch(ukConfig, 1832).capacity(20200).breedingProbability(0.25))
      add(Epoch(ukConfig, 1842).capacity(27400).breedingProbability(0.22))
      add(Epoch(ukConfig, 1852).capacity(29000).breedingProbability(0.25))
      add(Epoch(ukConfig, 1862).capacity(31500).breedingProbability(0.22))
      add(Epoch(ukConfig, 1872).capacity(35000).breedingProbability(0.21))
      add(Epoch(ukConfig, 1882).capacity(37800).breedingProbability(0.19))
      add(Epoch(ukConfig, 1892).capacity(38200).breedingProbability(0.18))
      add(Epoch(ukConfig, 1902).capacity(42100).breedingProbability(0.13))
      add(Epoch(ukConfig, 1912).capacity(44000).breedingProbability(0.14))
      add(Epoch(ukConfig, 1922).capacity(45000).breedingProbability(0.12))
      add(Epoch(ukConfig, 1932).capacity(50200).breedingProbability(0.07))
      add(Epoch(ukConfig, 1942).capacity(52800).breedingProbability(0.08))
      add(Epoch(ukConfig, 1952).capacity(55900).breedingProbability(0.09))
      add(Epoch(ukConfig, 1962).capacity(56300).breedingProbability(0.10))
      add(Epoch(ukConfig, 1972).capacity(57400).breedingProbability(0.07))
      add(Epoch(ukConfig, 1982).capacity(59300).breedingProbability(0.10))
      add(Epoch(ukConfig, 1992).capacity(63200).breedingProbability(0.09))
      add(Epoch(ukConfig, 2002).capacity(65600).breedingProbability(0.09))
      add(Epoch(ukConfig, 2012).capacity(66270).breedingProbability(0.09))
      setFinalEpochYear(2015)
    }
}
