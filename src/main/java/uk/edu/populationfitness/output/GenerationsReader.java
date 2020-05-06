package uk.edu.populationfitness.output;

import com.opencsv.CSVReader;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.GenerationStatistics;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerationsReader {
    public static List<GenerationStatistics> readGenerations(Config config, String path) throws IOException

    {
        ArrayList<GenerationStatistics> generations = new ArrayList<>();

        CSVReader reader = new CSVReader(new FileReader(path));

        // Read header
        reader.readNext();

        // Read until we run out of readable data
        for (String[] row = reader.readNext(); row != null; row = reader.readNext()){
            try {
                generations.add(readFromRow(config, row));
            }
            catch(java.lang.NumberFormatException e){
                break;
            }
        }

        return generations;
    }

    private static GenerationStatistics readFromRow(Config config, String[] row) {
        GenerationStatistics generation = new GenerationStatistics(new Epoch(config,
                Integer.parseInt(row[0])),
                Integer.parseInt(row[4]),
                Integer.parseInt(row[7]),
                Integer.parseInt(row[8]),
                Integer.parseInt(row[9]),
                (int)(Double.parseDouble(row[10]) * 1000),
                (int)(Double.parseDouble(row[11]) * 1000),
                Double.parseDouble(row[15]));
        generation.epoch.end_year = Integer.parseInt(row[1]);
        generation.epoch.environment_capacity = (int)(Double.parseDouble(row[2]));
        generation.epoch.breedingProbability(Double.parseDouble(row[3]));

        generation.epoch.fitness(Double.parseDouble(row[5]));
        generation.epoch.expected_max_population = Integer.parseInt(row[6]);
        generation.average_fitness = Double.parseDouble(row[12]);
        generation.fitness_deviation = Double.parseDouble(row[13]);
        generation.average_age = Double.parseDouble(row[14]);
        generation.average_life_expectancy = Double.parseDouble(row[16]);
        return generation;
    }
}
