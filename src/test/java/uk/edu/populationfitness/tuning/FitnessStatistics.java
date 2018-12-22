package uk.edu.populationfitness.tuning;

import uk.edu.populationfitness.models.GenerationStatistics;
import uk.edu.populationfitness.models.fastmaths.FastMaths;

import java.util.List;
import java.util.stream.Collectors;

public class FitnessStatistics {
    public final Double deviationsTrend;
    public final Double fitnessesTrend;
    public final Double averageDeviation;
    public final Double averageFitness;

    public FitnessStatistics(List<GenerationStatistics> results) {
        final List<Double> deviations = results.stream().map(s -> s.fitness_deviation).collect(Collectors.toList());
        final List<Double> fitnesses = results.stream().map(s -> s.average_fitness).collect(Collectors.toList());
        deviationsTrend = FastMaths.linearTrendLineSlopeAsPercentOfAverage(deviations);
        fitnessesTrend =FastMaths.linearTrendLineSlopeAsPercentOfAverage(fitnesses);
        averageDeviation = deviations.stream().mapToDouble(d -> d).average().getAsDouble();
        averageFitness = fitnesses.stream().mapToDouble(d -> d).average().getAsDouble();
    }

    public void print(String label){
        System.out.print(label);
        System.out.print(" SD avg:" + averageDeviation);
        System.out.print(" SD trend:" + deviationsTrend);
        System.out.print(" Fit avg:" + averageFitness);
        System.out.println(" Fit trend:" + fitnessesTrend);
    }
}
