package uk.edu.populationfitness.output;

import com.opencsv.CSVWriter;
import uk.edu.populationfitness.Tuning;

import java.io.FileWriter;
import java.io.IOException;

import static uk.edu.populationfitness.output.EpochsWriter.deleteExisting;

public class TuningWriter {
    public static void writeInPath(String path, Tuning tuning) throws  IOException{
        final String filename = path + "/" + tuning.function.toString() + "-" + tuning.number_of_genes + "-" + tuning.size_of_genes + ".csv";
        write(tuning, filename);
    }

    public static void write(Tuning tuning, String filename) throws IOException {
        deleteExisting(filename);
        CSVWriter writer = new CSVWriter(new FileWriter(filename), ',');
        addHeaderRow(writer);
        addRow(writer, tuning);
        writer.close();
    }

    private static void addRow(CSVWriter writer, Tuning tuning) {
        writer.writeNext(new String[]{
                tuning.function.toString(),
                Double.toString(tuning.historic_fit),
                Double.toString(tuning.disease_fit),
                Double.toString(tuning.modern_fit),
                Double.toString(tuning.modern_kill),
                Double.toString(tuning.historic_kill),
                Double.toString(tuning.modern_breeding),
                Integer.toString(tuning.size_of_genes),
                Integer.toString(tuning.number_of_genes),
                Double.toString(tuning.min_fitness),
                Double.toString(tuning.max_fitness),
                Double.toString(tuning.mutations_per_gene),
                Integer.toString(tuning.series_runs),
                Integer.toString(tuning.parallel_runs),
        });
    }

    private static void addHeaderRow(CSVWriter writer) {
        writer.writeNext(new String[]{
                "Function",
                "Historic Fit",
                "Disease Fit",
                "Modern Fit",
                "Modern Kill",
                "Historic Kill",
                "Modern Breeding",
                "Size of Genes",
                "Number of Genes",
                "Min Fitness",
                "Max Fitness",
                "Mutations",
                "SeriesRuns",
                "ParallelRuns",
        });
    }
}
