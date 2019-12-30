package uk.edu.populationfitness.tuning;

import uk.edu.populationfitness.models.GenerationStatistics;
import uk.edu.populationfitness.models.fastmaths.FastMaths;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FitnessStatistics {
    public final Double fitnessesTrend;
    public final Double averageFitness;
    public final Double factoredFitnessesTrend;
    public final Double averagefactoredFitness;
    public final Integer maxStartPopulation;
    public final Integer totalKilled;
    public final Integer percentKilled;

    public FitnessStatistics(List<GenerationStatistics> results) {
        final List<Double> factoredFitnesses = results.stream().map(s -> s.average_factored_fitness).collect(Collectors.toList());
        final List<Double> fitnesses = results.stream().map(s -> s.average_fitness).collect(Collectors.toList());

        fitnessesTrend =FastMaths.linearTrendLineSlopeAsPercentOfAverage(fitnesses);
        averageFitness = fitnesses.stream().mapToDouble(d -> d).average().getAsDouble();
        factoredFitnessesTrend =FastMaths.linearTrendLineSlopeAsPercentOfAverage(factoredFitnesses);
        averagefactoredFitness = factoredFitnesses.stream().mapToDouble(d -> d).average().getAsDouble();
        maxStartPopulation = Collections.max(results.stream().map(s -> s.population + s.number_killed - s.number_born).collect(Collectors.toList()));
        totalKilled = results.stream().map(s -> s.number_killed).reduce(0, (a, b) -> a + b);
        percentKilled = (totalKilled * 100) / maxStartPopulation;
    }

    public void printTrends(String label){
        System.out.print(label);
        System.out.print(" Fit avg:" + averageFitness);
        System.out.println(" Fit trend:" + fitnessesTrend);
        //System.out.print(" FFit avg:" + averagefactoredFitness);
        //System.out.println(" FFit trend:" + factoredFitnessesTrend);
    }

    public void printPopulations(String label){
        System.out.print(label);
        System.out.print(" Pop max:" + maxStartPopulation);
        System.out.print(" Killed:" + totalKilled);
        System.out.println(" %:" + percentKilled);
    }
}
