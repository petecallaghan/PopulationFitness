package uk.edu.populationfitness.output;

import com.opencsv.CSVWriter;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.genes.Function;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("ALL")
public class EpochsWriter {
    private static String filePath(Function function, int size, int mutations){
        String filePath = "functionepochs" + "-" +
                function +
                "-" +
                size +
                "-" +
                mutations +
                ".csv";
        return filePath;
    }

    public static String writeCsv(Function function, int size, int mutations, Epochs epochs) throws IOException {
        String filePath = filePath(function, size, mutations);
        deleteExisting(filePath);
        CSVWriter writer = new CSVWriter(new FileWriter(filePath), ',');
        addHeaderRow(writer);

        for (Epoch epoch: epochs.epochs) {
            add(writer, epoch);
        }

        writer.close();
        return filePath;
    }

    public static void deleteExisting(String path){
        try {
            File existing = new File(path);
            if (!existing.delete()){
                System.out.println("Could not delete "+path);
            }
        }
        catch(Exception ignored){
        }
    }

    private static void add(CSVWriter writer, Epoch epoch) {
        writer.writeNext(new String[]{
                Integer.toString(epoch.start_year),
                Integer.toString(epoch.end_year),
                Double.toString(epoch.kill()),
                Integer.toString(epoch.environment_capacity),
                Double.toString(epoch.breedingProbability()),
                Boolean.toString(epoch.disease()),
                Double.toString(epoch.fitness()),
                Integer.toString(epoch.expected_max_population),
                Double.toString(epoch.averageCapacityFactor()),
                Double.toString(epoch.averageCapacityFactor() * epoch.fitness()),
        });
    }

    private static void addHeaderRow(CSVWriter writer) {
        writer.writeNext(new String[]{
                "Start Year",
                "End Year",
                "Kill Constant",
                "Environment Capacity",
                "Breeding Probability",
                "Disease",
                "Fitness Factor",
                "Expected Max Population",
                "Avg Capacity Factor",
                "Avg Capacity Fitness",
        });
    }
}
