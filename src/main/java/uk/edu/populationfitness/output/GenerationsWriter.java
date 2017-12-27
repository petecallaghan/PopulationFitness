package uk.edu.populationfitness.output;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.GenerationStatistics;
import uk.edu.populationfitness.models.Generations;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pete.callaghan on 11/07/2017.
 */
public class GenerationsWriter {
    public static String filePath(int parallel_run, int series_run, int number_of_runs, Config config){
        StringBuffer filePath = new StringBuffer("generations");
        filePath.append("-");
        filePath.append(parallel_run);
        filePath.append("-");
        filePath.append(series_run);
        filePath.append("of");
        filePath.append(number_of_runs);
        filePath.append("-");
        filePath.append(config.genesFactory.getFitnessFunction());
        filePath.append("-genes");
        filePath.append(config.number_of_genes);
        filePath.append("x");
        filePath.append(config.size_of_each_gene);
        filePath.append("-pop");
        filePath.append(config.initial_population);
        filePath.append("-mut");
        filePath.append(config.mutations_per_gene);
        filePath.append("-");
        filePath.append(config.id.replaceAll(":", "-"));
        filePath.append(".csv");
        return filePath.toString();
    }

    public static String writeCsv(int parallel_run, int series_run, int number_of_runs, Generations generations, Tuning tuning) throws IOException {
        return writeCsv(filePath(parallel_run, series_run, number_of_runs, generations.config), generations, tuning);
    }

    public static String writeCsv(String filePath, Generations generations, Tuning tuning) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(filePath), ',');
        addHeaderRow(writer);

        for (GenerationStatistics generation: generations.history) {
            addGenerationRow(writer, generation);
        }

        writer.writeNext(new String[]{ConfigWriter.toString(tuning)});

        writer.close();

        return filePath;
    }

    private static void addGenerationRow(CSVWriter writer, GenerationStatistics generation) {
        writer.writeNext(new String[]{
                Integer.toString(generation.epoch.start_year),
                Integer.toString(generation.epoch.end_year),
                Double.toString(generation.epoch.kill()),
                Double.toString(generation.epoch.environment_capacity),
                Boolean.toString(generation.epoch.isFitnessEnabled()),
                Double.toString(generation.epoch.breedingProbability()),
                Integer.toString(generation.year),
                Double.toString(generation.epoch.fitness()),
                Integer.toString(generation.epoch.expected_max_population),
                Integer.toString(generation.population),
                Integer.toString(generation.number_born),
                Integer.toString(generation.number_killed),
                Double.toString(generation.bornElapsedInHundredths()),
                Double.toString(generation.killElapsedInHundredths()),
                Double.toString(generation.average_fitness),
                Double.toString(generation.fitness_deviation),
                Integer.toString(generation.average_age),
        });
    }

    private static void addHeaderRow(CSVWriter writer) {
        writer.writeNext(new String[]{
                "Epoch Start Year",
                "Epoch End Year",
                "Epoch Kill Constant",
                "Epoch Environment Capacity",
                "Epoch Enable Fitness",
                "Epoch Breeding Probability",
                "Year",
                "Epoch Fitness Factor",
                "Epoch Expected Max Population",
                "Population",
                "Number Born",
                "Number Killed",
                "Born Elapsed",
                "Kill Elapsed",
                "Avg Fitness",
                "Fitness Deviation",
                "Average Age",
        });
    }

    /**
     * Adds the two generation histories together and writes the result as a CSV file.
     *
     * @param parallel_run
     * @param series_run
     * @param current
     * @param total
     * @param tuning
     * @return
     * @throws IOException
     */
    public static Generations CombineGenerationsAndWriteResult(int parallel_run,
                                                               int series_run,
                                                               Generations current,
                                                               Generations total,
                                                               Tuning tuning) throws IOException {
        total = (total == null ? current : Generations.add(total, current));
        writeCsv(parallel_run, series_run, tuning.series_runs * tuning.parallel_runs, total, tuning);
        return total;
    }

    /**
     * Creates a result file name from the file id.
     *
     * @param id
     * @return the resulting file name
     */
    public static String createResultFileName(String id){
        StringBuffer name = new StringBuffer("allgenerations");
        name.append("-");
        name.append(id);
        name.append(".csv");
        return name.toString();
    }
}
