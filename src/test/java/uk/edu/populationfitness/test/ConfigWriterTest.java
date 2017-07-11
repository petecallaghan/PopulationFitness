package uk.edu.populationfitness.test;

import org.junit.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.output.ConfigWriter;

import java.io.IOException;

/**
 * Created by pete.callaghan on 11/07/2017.
 */
public class ConfigWriterTest {
    @Test public void testWriter() throws IOException {
        Config config = new Config();
        ConfigWriter writer = new ConfigWriter(config);

        writer.write("test.yaml");
    }
}
