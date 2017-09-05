package uk.edu.populationfitness.output;

import com.opencsv.CSVWriter;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.genes.Function;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EpochsWriter {
    private static String filePath(Function function, int size){
        StringBuffer filePath = new StringBuffer("functionepochs");
        filePath.append("-");
        filePath.append(function);
        filePath.append("-");
        filePath.append(size);
        filePath.append(".csv");
        return filePath.toString();
    }

    public static void writeCsv(Function function, int size, Epochs epochs) throws IOException {
        String filePath = filePath(function, size);
        deleteExisting(filePath);
        CSVWriter writer = new CSVWriter(new FileWriter(filePath), ',');
        addHeaderRow(writer);

        for (Epoch epoch: epochs.epochs) {
            add(writer, epoch);
        }

        writer.close();
    }

    private static void deleteExisting(String path){
        try {
            File existing = new File(path);
            existing.delete();
        }
        catch(Exception e){
        }
    }

    private static void add(CSVWriter writer, Epoch epoch) {
        writer.writeNext(new String[]{
                Integer.toString(epoch.start_year),
                Integer.toString(epoch.end_year),
                Double.toString(epoch.kill()),
                Double.toString(epoch.environment_capacity),
                Boolean.toString(epoch.isFitnessEnabled()),
                Double.toString(epoch.breedingProbability()),
                Boolean.toString(epoch.disease()),
                Double.toString(epoch.fitness()),
                Integer.toString(epoch.expected_max_population),
        });
    }

    private static void addHeaderRow(CSVWriter writer) {
        writer.writeNext(new String[]{
                "Start Year",
                "End Year",
                "Kill Constant",
                "Environment Capacity",
                "Enable Fitness",
                "Breeding Probability",
                "Disease",
                "Fitness Factor",
                "Expected Max Population",
        });
    }
}
