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
    private static String filePath(int parallel_run, int series_run, int number_of_runs, Config config){
        return "generations" + "-" +
                parallel_run +
                "-" +
                series_run +
                "of" +
                number_of_runs +
                "-" +
                config.getGenesFactory().getFitnessFunction() +
                "-genes" +
                config.getNumberOfGenes() +
                "x" +
                config.getSizeOfEachGene() +
                "-pop" +
                config.getInitialPopulation() +
                "-mut" +
                config.getMutationsPerGene() +
                "-" +
                config.id.replaceAll(":", "-") +
                ".csv";
    }

    private static void writeCsv(int parallel_run, int series_run, int number_of_runs, Generations generations, Tuning tuning) throws IOException {
        writeCsv(filePath(parallel_run, series_run, number_of_runs, generations.config), generations, tuning);
    }

    public static String writeCsv(String filePath, Generations generations, Tuning tuning) throws IOException {
        CSVWriter writer = createCsvWriter(filePath);

        addHeaderRow(writer);

        for (GenerationStatistics generation: generations.history) {
            addGenerationRow(writer, generation);
        }

        writer.writeNext(new String[]{ConfigWriter.toString(tuning)});

        writer.close();

        return filePath;
    }

    @NotNull
    private static CSVWriter createCsvWriter(String filePath) throws IOException {
        try{
            return new CSVWriter(new FileWriter(filePath), ',');
        }
        catch(IOException e){
            e.printStackTrace();
            return new CSVWriter(new FileWriter(tryTemporaryVersionOf(filePath)), ',');
        }
    }

    private static String tryTemporaryVersionOf(String filePath) {
        return "~tmp." + filePath;
    }

    private static void addGenerationRow(CSVWriter writer, GenerationStatistics generation) {
        writer.writeNext(new String[]{
                Integer.toString(generation.epoch.start_year),
                Integer.toString(generation.epoch.end_year),
                Double.toString(generation.epoch.capacityForYear(generation.year)),
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
                Double.toString(generation.capacity_factor),
                Double.toString(generation.average_factored_fitness),
                Double.toString(generation.average_mutations),
                Integer.toString(generation.average_life_expectancy),
        });
    }

    private static void addHeaderRow(CSVWriter writer) {
        writer.writeNext(new String[]{
                "Epoch Start Year",
                "Epoch End Year",
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
                "Capacity Factor",
                "Avg Factored Fitness",
                "Avg Mutations",
                "Avg Life Expectancy",
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
    public static Generations combineGenerationsAndWriteResult(int parallel_run,
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
        return "allgenerations" + "-" +
                id +
                ".csv";
    }
}
