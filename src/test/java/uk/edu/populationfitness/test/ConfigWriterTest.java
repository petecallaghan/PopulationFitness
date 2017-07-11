package uk.edu.populationfitness.test;

import org.junit.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.output.ConfigWriter;

import java.io.IOException;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by pete.callaghan on 11/07/2017.
 */
public class ConfigWriterTest {
    @Test public void testWriter() throws IOException {
        // Given a config
        Config config = new Config();

        // Write it out to a file and don't complain about it
        ConfigWriter.write(config, "test.yaml");
    }

    @Test public void testUniqueConfigIdentifiers() throws InterruptedException {
        // Given two configs created at different times
        Config config1 = new Config();
        Thread.sleep(1l);
        Config config2 = new Config();

        // Then they have unique identifiers
        assertNotEquals(config1.id, config2.id);
    }

    @Test public void testStringEncoding() throws InterruptedException {
        // Given two configs created at different times
        Config config1 = new Config();
        Thread.sleep(1l);
        Config config2 = new Config();

        // Then they have unique encodings
        assertNotEquals(ConfigWriter.toString(config1), ConfigWriter.toString(config2));
    }
}
