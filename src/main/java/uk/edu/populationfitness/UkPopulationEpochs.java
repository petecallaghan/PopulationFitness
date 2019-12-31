package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;

public class UkPopulationEpochs {
    public static Epochs define(Config config){
        Epochs epochs = new Epochs();

        // Birth rates derived from from Crude Birth Rates of W&S 1981

        // Src: silvia.carpitella@unipa.it
        addPrehistoricEpochs(config, epochs);

        epochs.addNextEpoch(new Epoch(config, 1).capacity(2000));
        epochs.addNextEpoch(new Epoch(config, 44).capacity(4500));
        epochs.addNextEpoch(new Epoch(config, 201).capacity(3000));
        epochs.addNextEpoch(new Epoch(config, 411).capacity(1250));
        epochs.addNextEpoch(new Epoch(config, 431).capacity(1300));
        epochs.addNextEpoch(new Epoch(config, 501).capacity(1300));
        epochs.addNextEpoch(new Epoch(config, 751).capacity(1400));
        epochs.addNextEpoch(new Epoch(config, 951).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, 1067).capacity(2000));
        epochs.addNextEpoch(new Epoch(config, 1087).capacity(3700));
        epochs.addNextEpoch(new Epoch(config, 1191).capacity(4800));
        epochs.addNextEpoch(new Epoch(config, 1221).capacity(5000));
        epochs.addNextEpoch(new Epoch(config, 1251).capacity(5300));
        epochs.addNextEpoch(new Epoch(config, 1280).capacity(5700));
        epochs.addNextEpoch(new Epoch(config, 1291).capacity(5600));
        epochs.addNextEpoch(new Epoch(config, 1316).capacity(5000)); // Great Famine of 1315–1317
        epochs.addNextEpoch(new Epoch(config, 1326).capacity(5700));
        epochs.addNextEpoch(new Epoch(config, 1349).capacity(3000).disease(true)); // Black death
        epochs.addNextEpoch(new Epoch(config, 1352).capacity(3000));
        epochs.addNextEpoch(new Epoch(config, 1378).capacity(2500));
        epochs.addNextEpoch(new Epoch(config, 1401).capacity(2400));
        epochs.addNextEpoch(new Epoch(config, 1431).capacity(2300));
        epochs.addNextEpoch(new Epoch(config, 1451).capacity(2600));
        epochs.addNextEpoch(new Epoch(config, 1491).capacity(2800));
        epochs.addNextEpoch(new Epoch(config, 1523).capacity(3400).breedingProbability(0.35));
        epochs.addNextEpoch(new Epoch(config, 1542).capacity(3800).breedingProbability(0.35));
        epochs.addNextEpoch(new Epoch(config, 1561).capacity(5000).breedingProbability(0.31));
        epochs.addNextEpoch(new Epoch(config, 1601).capacity(6300).breedingProbability(0.29));
        epochs.addNextEpoch(new Epoch(config, 1651).capacity(6200).breedingProbability(0.26));

        epochs.addNextEpoch(new Epoch(config, 1701).capacity(11300).breedingProbability(0.25));
        epochs.addNextEpoch(new Epoch(config, 1802).capacity(12500).breedingProbability(0.28));
        epochs.addNextEpoch(new Epoch(config, 1812).capacity(13400).breedingProbability(0.28));
        epochs.addNextEpoch(new Epoch(config, 1822).capacity(15800).breedingProbability(0.26));
        epochs.addNextEpoch(new Epoch(config, 1832).capacity(20200).breedingProbability(0.25));
        epochs.addNextEpoch(new Epoch(config, 1842).capacity(27400).breedingProbability(0.22));
        epochs.addNextEpoch(new Epoch(config, 1852).capacity(29000).breedingProbability(0.25));
        epochs.addNextEpoch(new Epoch(config, 1862).capacity(31500).breedingProbability(0.22));
        epochs.addNextEpoch(new Epoch(config, 1872).capacity(35000).breedingProbability(0.21));
        epochs.addNextEpoch(new Epoch(config, 1882).capacity(37800).breedingProbability(0.19));
        epochs.addNextEpoch(new Epoch(config, 1892).capacity(38200).breedingProbability(0.18));
        epochs.addNextEpoch(new Epoch(config, 1902).capacity(42100).breedingProbability(0.13));
        epochs.addNextEpoch(new Epoch(config, 1912).capacity(44000).breedingProbability(0.14));
        epochs.addNextEpoch(new Epoch(config, 1922).capacity(45000).breedingProbability(0.12));
        epochs.addNextEpoch(new Epoch(config, 1932).capacity(50200).breedingProbability(0.07));
        epochs.addNextEpoch(new Epoch(config, 1952).capacity(55900).breedingProbability(0.09));
        epochs.addNextEpoch(new Epoch(config, 1962).capacity(56300).breedingProbability(0.10));
        epochs.addNextEpoch(new Epoch(config, 1972).capacity(57400).breedingProbability(0.07).modern(true));
        epochs.addNextEpoch(new Epoch(config, 1982).capacity(59300).breedingProbability(0.10).modern(true));
        epochs.addNextEpoch(new Epoch(config, 1992).capacity(63200).breedingProbability(0.09).modern(true));
        epochs.addNextEpoch(new Epoch(config, 2002).capacity(65600).breedingProbability(0.09).modern(true));
        epochs.addNextEpoch(new Epoch(config, 2012).capacity(66270).breedingProbability(0.09).modern(true));
        epochs.addNextEpoch(new Epoch(config, 2015).capacity(66440).breedingProbability(0.09).modern(true));
        epochs.setFinalEpochYear(2018);

        return epochs;
    }

    private static void addPrehistoricEpochs(Config config, Epochs epochs) {
        epochs.addNextEpoch(new Epoch(config, -5050).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -5040).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -5030).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -5020).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -5010).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -5000).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4900).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4800).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4700).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4600).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4500).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4400).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4300).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4200).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -4100).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -3550).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -3050).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -2550).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -2050).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -1550).capacity(1500));
        epochs.addNextEpoch(new Epoch(config, -1050).capacity(1500));
    }
}
