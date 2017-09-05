package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;

public class UkPopulationEpochs {
    public static Epochs define(Config config){
        Epochs epochs = new Epochs(config);

        final double historic_kill = 1.066;

        // https://en.wikipedia.org/wiki/Demography_of_England
        epochs.addNextEpoch(new Epoch(config, -50).max(4000).kill(historic_kill).capacity(4000));
        epochs.addNextEpoch(new Epoch(config, 400).max(1700).kill(historic_kill).capacity(1700));

        epochs.addNextEpoch(new Epoch(config, 1086).max(3100).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1190).max(3970).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1220).max(4230).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1250).max(4430).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1279).max(4750).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1290).max(4690).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1315).max(4120).kill(historic_kill)); // Great Famine of 1315–1317
        epochs.addNextEpoch(new Epoch(config, 1325).max(4810).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1348).max(2600).kill(historic_kill).disease(true)); // Black death
        epochs.addNextEpoch(new Epoch(config, 1351).max(2500).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1377).max(2080).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1400).max(2020).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1430).max(1900).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1450).max(2140).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1490).max(2350).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1522).max(2830).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1541).max(3200).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1560).max(4110).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1600).max(5310).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1650).max(5200).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1700).max(7755).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1801).max(8762).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1811).max(10402).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1821).max(12012).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1831).max(13659).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1841).max(15289).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1851).max(18325).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1861).max(21361).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1871).max(24297).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1881).max(27231).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1891).max(30000).kill(historic_kill));
        epochs.setFinalEpochYear(1901);

        return epochs;
    }
}
