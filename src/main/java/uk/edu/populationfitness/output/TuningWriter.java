package uk.edu.populationfitness.output;

import com.opencsv.CSVWriter;
import uk.edu.populationfitness.Tuning;

import java.io.FileWriter;
import java.io.IOException;

import static uk.edu.populationfitness.output.EpochsWriter.deleteExisting;

public class TuningWriter {
    public static String[] Headers = new String[]{
            "Function",
            "Historic Fit",
            "Disease Fit",
            "Modern Fit",
            "Modern Breeding",
            "Size of Genes",
            "Number of Genes",
            "Mutations",
            "SeriesRuns",
            "ParallelRuns",
    };

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

    public static String[] toRow(Tuning tuning){
        return new String[]{
                tuning.function.toString(),
                Double.toString(tuning.historic_fit),
                Double.toString(tuning.disease_fit),
                Double.toString(tuning.modern_fit),
                Double.toString(tuning.modern_breeding),
                Integer.toString(tuning.size_of_genes),
                Integer.toString(tuning.number_of_genes),
                Double.toString(tuning.mutations_per_gene),
                Integer.toString(tuning.series_runs),
                Integer.toString(tuning.parallel_runs),
        };
    }

    private static void addRow(CSVWriter writer, Tuning tuning) {
        writer.writeNext(toRow(tuning));
    }

    private static void addHeaderRow(CSVWriter writer) {
        writer.writeNext(Headers);
    }
}
