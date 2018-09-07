package uk.edu.populationfitness.output;

import com.opencsv.CSVWriter;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.Generations;
import uk.edu.populationfitness.models.GenerationsAnalysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AnalysisWriter {
    public static String[] Headers = new String[]{
            "Tuning File",
            "Epochs File",
            "Generations File",
            "Max Expected Population",
            "Historical Disease Population",
            "Historical Disease Killed",
            "Historical Disease Death Rate",
            "Modern Disease Population",
            "Modern Disease Killed",
            "Modern Disease Death Rate",
            "Total Born",
            "Total Killed",
    };

    private static String[] combine(String[] one, String[] two){
        int length = one.length + two.length;
        String[] result = new String[length];
        System.arraycopy(one, 0, result, 0, one.length);
        System.arraycopy(two, 0, result, one.length, two.length);
        return result;
    }

    private static String[] headers(){
        return combine(Headers, TuningWriter.Headers);
    }

    private static String[] toRow(
            String tuningName,
            String epochsName,
            String generationsName,
            GenerationsAnalysis analysis) {
        return new String[]{
            tuningName,
            epochsName,
            generationsName,
            Long.toString(analysis.maxExpectedPopulation),
                Long.toString(analysis.historicalPopulationBeforeDisease),
                Long.toString(analysis.historicalDiseaseTotalDeaths),
                Long.toString((analysis.historicalDiseaseTotalDeaths * 100L) / analysis.historicalPopulationBeforeDisease),
                Long.toString(analysis.modernPopulationBeforeDisease),
                Long.toString(analysis.modernDiseaseTotalDeaths),
                Long.toString((analysis.modernDiseaseTotalDeaths * 100L) / analysis.modernPopulationBeforeDisease),
                Long.toString(analysis.totalBorn),
                Long.toString(analysis.totalKilled),
        };

    }

    private static boolean fileExists(String path) {
        try {
            return (new File(path)).exists();
        }
        catch(Exception ignored){
        }
        return false;
    }

    /**
     * Appends the analysis of the generations to the specified analysis file path, as a CSV
     *
     * @param analysisCsvFilePath
     * @param tuningName
     * @param epochsName
     * @param generationsName
     * @param tuning
     * @param generations
     * @throws IOException
     */
    public static void append(String analysisCsvFilePath,
                              String tuningName,
                              String epochsName,
                              String generationsName,
                              Tuning tuning,
                              Generations generations) throws IOException {
        GenerationsAnalysis analysis = new GenerationsAnalysis(generations.history);
        boolean addHeader = !fileExists(analysisCsvFilePath);
        CSVWriter writer = new CSVWriter(new FileWriter(analysisCsvFilePath, true), ',');
        if (addHeader){
            writer.writeNext(headers());
        }
        writer.writeNext(combine(toRow(tuningName, epochsName, generationsName, analysis), TuningWriter.toRow(tuning)));

        writer.close();
    }
}
