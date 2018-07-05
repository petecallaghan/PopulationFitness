using System;

namespace PopulationFitness.Models.Genes.Fitness
{
    public class Statistics
    {
        private double _min = double.MaxValue;

        private double _max = double.MinValue;

        public void Add(double value)
        {
            _min = Math.Min(_min, value);
            _max = Math.Max(_max, value);
        }

        public void Show()
        {
            Console.WriteLine("Min=" + _min + " Max=" + _max);
        }
    }
}
