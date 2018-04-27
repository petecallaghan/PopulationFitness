package uk.edu.populationfitness.test;

import org.junit.Test;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.output.TuningReader;
import uk.edu.populationfitness.output.TuningWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TuningWriterTest {
    @Test public void testWrite() throws IOException{
        Tuning expected = new Tuning();
        Tuning actual = new Tuning();
        expected.function = Function.Ackleys;
        expected.historic_fit = 0.1;
        expected.disease_fit = 0.3;
        expected.modern_breeding = 0.4;
        expected.modern_fit = 0.5;
        expected.number_of_genes = 7;
        expected.size_of_genes = 8;
        expected.mutations_per_gene = 10;
        expected.series_runs = 5;
        expected.parallel_runs = 2;
        TuningWriter.write(expected, "test.csv");
        TuningReader.read(actual, "test.csv");
        double delta = 0.0000000001;

        assertEquals(expected.function, actual.function);
        assertEquals(expected.historic_fit, actual.historic_fit, delta);
        assertEquals(expected.disease_fit, actual.disease_fit, delta);
        assertEquals(expected.modern_breeding, actual.modern_breeding, delta);
        assertEquals(expected.modern_fit, actual.modern_fit, delta);
        assertEquals(expected.number_of_genes, actual.number_of_genes);
        assertEquals(expected.size_of_genes, actual.size_of_genes);
        assertEquals(expected.mutations_per_gene, actual.mutations_per_gene, delta);
        assertEquals(expected.series_runs, actual.series_runs);
        assertEquals(expected.parallel_runs, actual.parallel_runs);
    }
}
