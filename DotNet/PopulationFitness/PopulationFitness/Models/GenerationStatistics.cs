using System;
using System.Collections.Generic;

namespace PopulationFitness.Models
{
    /**
     * Created by pete.callaghan on 04/07/2017.
     */
    public class GenerationStatistics
    {
        public readonly int number_born;
        public readonly int number_killed;
        public readonly int population;
        public readonly int year;
        public readonly Epoch epoch;
        private readonly long born_time;
        private readonly long kill_time;
        public double average_fitness;
        public double average_factored_fitness;
        public double fitness_deviation;
        public double average_age;
        public double capacity_factor;
        public double average_mutations;
        public double average_life_expectancy;

        public GenerationStatistics(Epoch epoch,
                                    int year,
                                    int population,
                                    int number_born,
                                    int number_killed,
                                    long born_time,
                                    long kill_time,
                                    double capacity_factor,
                                    double average_mutations)
        {
            this.number_born = number_born;
            this.number_killed = number_killed;
            this.population = population;
            this.year = year;
            this.epoch = epoch;
            this.born_time = born_time;
            this.kill_time = kill_time;
            this.average_fitness = 0;
            this.average_factored_fitness = 0;
            this.fitness_deviation = 0;
            this.average_age = 0;
            this.capacity_factor = capacity_factor;
            this.average_mutations = average_mutations;
        }

        public double BornElapsedInHundredths()
        {
            return (double)(born_time / 10) / 100;
        }

        public double KillElapsedInHundredths()
        {
            return (double)(kill_time / 10) / 100;
        }

        private static long Average(long value, long count)
        {
            if (count < 1)
            {
                return value;
            }
            return value / count;
        }

        private static double Average(double value, long count)
        {
            if (count < 1)
            {
                return value;
            }
            return value / count;
        }

        public static GenerationStatistics Add(GenerationStatistics first, GenerationStatistics second)
        {
            if (first.year != second.year) throw new Exception("Cannot add different years");

            GenerationStatistics result = new GenerationStatistics(new Epoch(first.epoch),
                    first.year,
                    first.population + second.population,
                    first.number_born + second.number_born,
                    first.number_killed + second.number_killed,
                    first.born_time + second.born_time,
                    first.kill_time + second.kill_time,
                    first.capacity_factor,
                    first.average_mutations);
            result.average_age = Average(first.average_age * first.population + second.average_age * second.population, result.population);
            result.average_fitness = Average(first.average_fitness * first.population + second.average_fitness * second.population, result.population);
            result.average_factored_fitness = Average(first.average_factored_fitness * first.population + second.average_factored_fitness * second.population, result.population);
            result.epoch.expected_max_population += second.epoch.expected_max_population;
            result.epoch.environment_capacity += second.epoch.environment_capacity;
            result.capacity_factor = Average(first.capacity_factor * first.population + second.capacity_factor * second.population, result.population);
            result.average_mutations = Average((first.average_mutations * first.number_born + second.average_mutations * second.number_born), result.number_born);
            result.average_life_expectancy = Average(first.average_life_expectancy * first.number_killed + second.average_life_expectancy * second.number_killed, result.number_killed);
            return result;
        }

        public static List<GenerationStatistics> Add(List<GenerationStatistics> first, List<GenerationStatistics> second)
        {
            List<GenerationStatistics> result = new List<GenerationStatistics>();

            if (first.Count != second.Count)
            {
                throw new Exception("Cannot add different numbers of generations");
            }

            for (int i = 0; i < first.Count; i++)
            {
                result.Add(Add(first[i], second[i]));
            }

            return result;
        }
    }
}
