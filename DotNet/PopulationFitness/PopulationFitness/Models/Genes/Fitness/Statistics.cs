using System;

namespace PopulationFitness.Models.Genes.Fitness
{
    public class Statistics
    {
        private double min = double.MaxValue;

        private double max = double.MinValue;

        public void Add(double value)
        {
            min = Math.Min(min, value);
            max = Math.Max(max, value);
        }

        public void Show()
        {
            Console.WriteLine("Min=" + min + " Max=" + max);
        }
    }
}
