package uk.edu.populationfitness.output;

import com.opencsv.CSVReader;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EpochsReader {
    public static List<Epoch> readEpochs(Config config, String path) throws IOException {
        ArrayList<Epoch> epochs = new ArrayList<>();

        CSVReader reader = new CSVReader(new FileReader(path));

        // Read header
        reader.readNext();

        for (String[] row = reader.readNext(); row != null; row = reader.readNext()){
            Epoch epoch = readFromRow(config, row);
            epochs.add(epoch);
        }

        return epochs;
    }

    private static Epoch readFromRow(Config config, String[] row) {
        Epoch epoch = new Epoch(config, Integer.parseInt(row[0]));
        epoch.end_year = Integer.parseInt(row[1]);
        epoch.environment_capacity = Integer.parseInt(row[2]);
        epoch.breedingProbability(Double.parseDouble(row[3]));
        epoch.disease(Boolean.parseBoolean(row[4]));
        epoch.fitness(Double.parseDouble(row[5]));
        epoch.expected_max_population = Integer.parseInt(row[6]);
        epoch.maxAge(Integer.parseInt(row[7]));
        epoch.maxBreedingAge(Integer.parseInt(row[8]));
        epoch.modern(Boolean.parseBoolean(row[12]));
        epoch.updateMaxFitness(Boolean.parseBoolean(row[13]));
        return epoch;
    }
}
