using System;
using System.Collections.Generic;

namespace PopulationFitness.Models
{
    /**
     * Created by pete.callaghan on 04/07/2017.
     */
    public class GenerationStatistics
    {
        public readonly int NumberBorn;
        public readonly int NumberKilled;
        public readonly int Population;
        public readonly int Year;
        public readonly Epoch Epoch;
        public readonly long BornTime;
        public readonly long KillTime;
        public double AverageFitness;
        public double AverageFactoredFitness;
        public double FitnessDeviation;
        public double AverageAge;
        public double CapacityFactor;
        public double AverageMutations;
        public double AverageLifeExpectancy;

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
            NumberBorn = number_born;
            NumberKilled = number_killed;
            Population = population;
            Year = year;
            Epoch = epoch;
            BornTime = born_time;
            KillTime = kill_time;
            AverageFitness = 0;
            AverageFactoredFitness = 0;
            FitnessDeviation = 0;
            AverageAge = 0;
            CapacityFactor = capacity_factor;
            AverageMutations = average_mutations;
            AverageLifeExpectancy = 0;
        }

        public double BornElapsedInHundredths()
        {
            return (double)(BornTime / 10) / 100;
        }

        public double KillElapsedInHundredths()
        {
            return (double)(KillTime / 10) / 100;
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
            if (first.Year != second.Year) throw new Exception("Cannot add different years");

            GenerationStatistics result = new GenerationStatistics(new Epoch(first.Epoch),
                    first.Year,
                    first.Population + second.Population,
                    first.NumberBorn + second.NumberBorn,
                    first.NumberKilled + second.NumberKilled,
                    first.BornTime + second.BornTime,
                    first.KillTime + second.KillTime,
                    first.CapacityFactor,
                    first.AverageMutations);
            result.AverageAge = Average(first.AverageAge * first.Population + second.AverageAge * second.Population, result.Population);
            result.AverageFitness = Average(first.AverageFitness * first.Population + second.AverageFitness * second.Population, result.Population);
            result.AverageFactoredFitness = Average(first.AverageFactoredFitness * first.Population + second.AverageFactoredFitness * second.Population, result.Population);
            result.Epoch.ExpectedMaxPopulation += second.Epoch.ExpectedMaxPopulation;
            result.Epoch.EnvironmentCapacity += second.Epoch.EnvironmentCapacity;
            result.CapacityFactor = Average(first.CapacityFactor * first.Population + second.CapacityFactor * second.Population, result.Population);
            result.AverageMutations = Average((first.AverageMutations * first.NumberBorn + second.AverageMutations * second.NumberBorn), result.NumberBorn);
            result.AverageLifeExpectancy = Average(first.AverageLifeExpectancy * first.NumberKilled + second.AverageLifeExpectancy * second.NumberKilled, result.NumberKilled);
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
