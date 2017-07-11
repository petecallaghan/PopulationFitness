package uk.edu.populationfitness.output;

import org.yaml.snakeyaml.Yaml;
import uk.edu.populationfitness.models.Config;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pete.callaghan on 11/07/2017.
 */
public class ConfigWriter {
    private final Config config;

    public ConfigWriter(Config config){
        this.config = config;
    }

    public void write(String filename) throws IOException {
        Yaml encodedConfig = new Yaml();
        FileWriter writer = new FileWriter(filename);
        encodedConfig.dump(config, writer);
        writer.close();
    }
}
