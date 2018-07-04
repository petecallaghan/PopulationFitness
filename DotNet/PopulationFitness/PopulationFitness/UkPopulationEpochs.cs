using PopulationFitness.Models;

namespace PopulationFitness
{
    public class UkPopulationEpochs
    {
        public static Epochs Define(Config config)
        {
            Epochs epochs = new Epochs();

            // Birth rates derived from from Crude Birth Rates of W&S 1981

            // Src: silvia.carpitella@unipa.it
            epochs.AddNextEpoch(new Epoch(config, -50).Capacity(1500));
            epochs.AddNextEpoch(new Epoch(config, 1).Capacity(2000));
            epochs.AddNextEpoch(new Epoch(config, 44).Capacity(4500));
            epochs.AddNextEpoch(new Epoch(config, 201).Capacity(3000));
            epochs.AddNextEpoch(new Epoch(config, 411).Capacity(1250));
            epochs.AddNextEpoch(new Epoch(config, 431).Capacity(1300));
            epochs.AddNextEpoch(new Epoch(config, 501).Capacity(1300));
            epochs.AddNextEpoch(new Epoch(config, 751).Capacity(1400));
            epochs.AddNextEpoch(new Epoch(config, 951).Capacity(1500));
            epochs.AddNextEpoch(new Epoch(config, 1067).Capacity(2000));
            epochs.AddNextEpoch(new Epoch(config, 1087).Capacity(3700));
            epochs.AddNextEpoch(new Epoch(config, 1191).Capacity(4800));
            epochs.AddNextEpoch(new Epoch(config, 1221).Capacity(5000));
            epochs.AddNextEpoch(new Epoch(config, 1251).Capacity(5300));
            epochs.AddNextEpoch(new Epoch(config, 1280).Capacity(5700));
            epochs.AddNextEpoch(new Epoch(config, 1291).Capacity(5600));
            epochs.AddNextEpoch(new Epoch(config, 1316).Capacity(5000)); // Great Famine of 1315–1317
            epochs.AddNextEpoch(new Epoch(config, 1326).Capacity(5700));
            epochs.AddNextEpoch(new Epoch(config, 1349).Capacity(3000).Disease(true)); // Black death
            epochs.AddNextEpoch(new Epoch(config, 1352).Capacity(3000));
            epochs.AddNextEpoch(new Epoch(config, 1378).Capacity(2500));
            epochs.AddNextEpoch(new Epoch(config, 1401).Capacity(2400));
            epochs.AddNextEpoch(new Epoch(config, 1431).Capacity(2300));
            epochs.AddNextEpoch(new Epoch(config, 1451).Capacity(2600));
            epochs.AddNextEpoch(new Epoch(config, 1491).Capacity(2800));
            epochs.AddNextEpoch(new Epoch(config, 1523).Capacity(3400).BreedingProbability(0.35));
            epochs.AddNextEpoch(new Epoch(config, 1542).Capacity(3800).BreedingProbability(0.35));
            epochs.AddNextEpoch(new Epoch(config, 1561).Capacity(5000).BreedingProbability(0.31));
            epochs.AddNextEpoch(new Epoch(config, 1601).Capacity(6300).BreedingProbability(0.29));
            epochs.AddNextEpoch(new Epoch(config, 1651).Capacity(6200).BreedingProbability(0.26));
            epochs.AddNextEpoch(new Epoch(config, 1701).Capacity(11300).BreedingProbability(0.25));
            epochs.AddNextEpoch(new Epoch(config, 1802).Capacity(12500).BreedingProbability(0.28));
            epochs.AddNextEpoch(new Epoch(config, 1812).Capacity(13400).BreedingProbability(0.28));
            epochs.AddNextEpoch(new Epoch(config, 1822).Capacity(15800).BreedingProbability(0.26));
            epochs.AddNextEpoch(new Epoch(config, 1832).Capacity(20200).BreedingProbability(0.25));
            epochs.AddNextEpoch(new Epoch(config, 1842).Capacity(27400).BreedingProbability(0.22));
            epochs.AddNextEpoch(new Epoch(config, 1852).Capacity(29000).BreedingProbability(0.25));
            epochs.AddNextEpoch(new Epoch(config, 1862).Capacity(31500).BreedingProbability(0.22));
            epochs.AddNextEpoch(new Epoch(config, 1872).Capacity(35000).BreedingProbability(0.21));
            epochs.AddNextEpoch(new Epoch(config, 1882).Capacity(37800).BreedingProbability(0.19));
            epochs.AddNextEpoch(new Epoch(config, 1892).Capacity(38200).BreedingProbability(0.18));
            epochs.AddNextEpoch(new Epoch(config, 1902).Capacity(42100).BreedingProbability(0.13));
            epochs.AddNextEpoch(new Epoch(config, 1912).Capacity(44000).BreedingProbability(0.14));
            epochs.AddNextEpoch(new Epoch(config, 1922).Capacity(45000).BreedingProbability(0.12));
            epochs.AddNextEpoch(new Epoch(config, 1932).Capacity(50200).BreedingProbability(0.07));
            epochs.AddNextEpoch(new Epoch(config, 1942).Capacity(52800).BreedingProbability(0.08));
            epochs.AddNextEpoch(new Epoch(config, 1952).Capacity(55900).BreedingProbability(0.09));
            epochs.AddNextEpoch(new Epoch(config, 1962).Capacity(56300).BreedingProbability(0.10));
            epochs.AddNextEpoch(new Epoch(config, 1972).Capacity(57400).BreedingProbability(0.07));
            epochs.AddNextEpoch(new Epoch(config, 1982).Capacity(59300).BreedingProbability(0.10));
            epochs.AddNextEpoch(new Epoch(config, 1992).Capacity(63200).BreedingProbability(0.09));
            epochs.AddNextEpoch(new Epoch(config, 2002).Capacity(65600).BreedingProbability(0.09));
            epochs.AddNextEpoch(new Epoch(config, 2012).Capacity(66270).BreedingProbability(0.09));
            epochs.SetFinalEpochYear(2015);

            return epochs;
        }
    }
}
