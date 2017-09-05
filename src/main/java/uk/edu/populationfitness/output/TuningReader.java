package uk.edu.populationfitness.output;

import com.opencsv.CSVReader;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.genes.Function;

import java.io.FileReader;
import java.io.IOException;

public class TuningReader {
    public static Tuning read(Tuning tuning, String file) throws IOException{
        CSVReader reader = new CSVReader(new FileReader(file));

        // Read header
        reader.readNext();
        read(reader, tuning);
        return tuning;
    }

    private static void read(CSVReader reader, Tuning tuning) throws IOException {
        String[] row = reader.readNext();
        tuning.function = Function.valueOf(row[0]);
        tuning.historic_fit = Double.parseDouble(row[1]);
        tuning.disease_fit = Double.parseDouble(row[2]);
        tuning.modern_fit = Double.parseDouble(row[3]);
        tuning.modern_kill = Double.parseDouble(row[4]);
        tuning.historic_kill = Double.parseDouble(row[5]);
        tuning.modern_breeding = Double.parseDouble(row[6]);
        tuning.number_of_genes = Integer.parseInt(row[7]);
        tuning.size_of_genes = Integer.parseInt(row[8]);
    }
}
