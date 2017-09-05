package uk.edu.populationfitness.output;

import org.yaml.snakeyaml.Yaml;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pete.callaghan on 11/07/2017.
 */
public class ConfigWriter {
    /**
     * Writes a YAML encoding of the config to the file
     *
     * @param config
     * @param filename
     * @throws IOException
     */
    public static void write(Tuning config, String filename) throws IOException {
        Yaml encodedConfig = new Yaml();
        FileWriter writer = new FileWriter(filename);
        encodedConfig.dump(config, writer);
        writer.close();
    }

    /**
     * Generates a string YAML encoding of the configuration
     *
     * @param config
     * @return
     */
    public static String toString(Tuning config){
        Yaml encodedConfig = new Yaml();
        return encodedConfig.dump(config);
    }
}
