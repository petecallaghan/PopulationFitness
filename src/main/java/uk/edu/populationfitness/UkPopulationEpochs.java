package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;

public class UkPopulationEpochs {
    public static Epochs define(Config config){
        Epochs epochs = new Epochs();

        final double historic_kill = 1.066;
        final double modern_breeding = 0.13;
        final double modern_kill = 1.003;

        // Src: silvia.carpitella@unipa.it
        epochs.addNextEpoch(new Epoch(config, -50).kill(historic_kill).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, 1).kill(historic_kill).capacity(2000));
        epochs.addNextEpoch(new Epoch(config, 44).kill(historic_kill).capacity(4500));
        epochs.addNextEpoch(new Epoch(config, 201).kill(historic_kill).capacity(3000));
        epochs.addNextEpoch(new Epoch(config, 411).kill(historic_kill).capacity(1250));
        epochs.addNextEpoch(new Epoch(config, 431).kill(historic_kill).capacity(1300));
        epochs.addNextEpoch(new Epoch(config, 501).kill(historic_kill).capacity(1300));
        epochs.addNextEpoch(new Epoch(config, 751).kill(historic_kill).capacity(1400));
        epochs.addNextEpoch(new Epoch(config, 951).kill(historic_kill).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, 1067).kill(historic_kill).capacity(2000));
        epochs.addNextEpoch(new Epoch(config, 1087).kill(historic_kill).capacity(3700));
        epochs.addNextEpoch(new Epoch(config, 1191).kill(historic_kill).capacity(4800));
        epochs.addNextEpoch(new Epoch(config, 1221).kill(historic_kill).capacity(5000));
        epochs.addNextEpoch(new Epoch(config, 1251).kill(historic_kill).capacity(5300));
        epochs.addNextEpoch(new Epoch(config, 1280).kill(historic_kill).capacity(5700));
        epochs.addNextEpoch(new Epoch(config, 1291).kill(historic_kill).capacity(5600));
        epochs.addNextEpoch(new Epoch(config, 1316).kill(historic_kill).capacity(5000)); // Great Famine of 1315–1317
        epochs.addNextEpoch(new Epoch(config, 1326).kill(historic_kill).capacity(5700));
        epochs.addNextEpoch(new Epoch(config, 1349).kill(historic_kill).capacity(3000).disease(true)); // Black death
        epochs.addNextEpoch(new Epoch(config, 1352).kill(historic_kill).capacity(3000));
        epochs.addNextEpoch(new Epoch(config, 1378).kill(historic_kill).capacity(2500));
        epochs.addNextEpoch(new Epoch(config, 1401).kill(historic_kill).capacity(2400));
        epochs.addNextEpoch(new Epoch(config, 1431).kill(historic_kill).capacity(2300));
        epochs.addNextEpoch(new Epoch(config, 1451).kill(historic_kill).capacity(2600));
        epochs.addNextEpoch(new Epoch(config, 1491).kill(historic_kill).capacity(2800));
        epochs.addNextEpoch(new Epoch(config, 1523).kill(historic_kill).capacity(3400));
        epochs.addNextEpoch(new Epoch(config, 1542).kill(historic_kill).capacity(3800));
        epochs.addNextEpoch(new Epoch(config, 1561).kill(historic_kill).capacity(5000));
        epochs.addNextEpoch(new Epoch(config, 1601).kill(historic_kill).capacity(6300));
        epochs.addNextEpoch(new Epoch(config, 1651).kill(historic_kill).capacity(6200));
        epochs.addNextEpoch(new Epoch(config, 1701).kill(historic_kill).capacity(11300));
        epochs.addNextEpoch(new Epoch(config, 1802).kill(historic_kill).capacity(12500));
        epochs.addNextEpoch(new Epoch(config, 1812).kill(historic_kill).capacity(13400));
        epochs.addNextEpoch(new Epoch(config, 1822).kill(historic_kill).capacity(15800));
        epochs.addNextEpoch(new Epoch(config, 1832).kill(historic_kill).capacity(20200));
        epochs.addNextEpoch(new Epoch(config, 1842).kill(historic_kill).capacity(27400));
        epochs.addNextEpoch(new Epoch(config, 1852).kill(historic_kill).capacity(29000));
        epochs.addNextEpoch(new Epoch(config, 1862).kill(historic_kill).capacity(31500));
        epochs.addNextEpoch(new Epoch(config, 1872).kill(historic_kill).capacity(35000));
        epochs.addNextEpoch(new Epoch(config, 1882).kill(historic_kill).capacity(37800));
        epochs.addNextEpoch(new Epoch(config, 1892).kill(historic_kill).capacity(38200));
        epochs.addNextEpoch(new Epoch(config, 1902).kill(modern_kill).capacity(42100).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1912).kill(modern_kill).capacity(44000).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1922).kill(modern_kill).capacity(45000).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1932).kill(modern_kill).capacity(50200).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1942).kill(modern_kill).capacity(52800).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1952).kill(modern_kill).capacity(55900).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1962).kill(modern_kill).capacity(56300).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1972).kill(modern_kill).capacity(57400).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1982).kill(modern_kill).capacity(59300).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 1992).kill(modern_kill).capacity(63200).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 2002).kill(modern_kill).capacity(65600).breedingProbability(modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 2012).kill(modern_kill).capacity(66270).breedingProbability(modern_breeding));
        epochs.setFinalEpochYear(2015);

        return epochs;
    }
}
