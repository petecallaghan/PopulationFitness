using System;

namespace PopulationFitness.Models.Genes.Performance
{
    class TimingStatistics
    {

        private readonly String name;
        private double total;
        private int count;
        private long _max;
        private long _min;

        public TimingStatistics(String name)
        {
            this.name = name;
            Reset();
        }

        public void Add(long value)
        {
            count++;
            total += value;
            _min = Math.Min(value, _min);
            _max = Math.Max(value, _max);
        }

        public long Min
        {
            get
            {
                return count == 0 ? 0 : _min;
            }
        }

        public long Max
        {
            get
            {
                return count == 0 ? 0 : _max;
            }
        }

        private long Mean
        {
            get
            {
                return count > 0 ? (long)Math.Round(total / count) : 0;
            }
        }

        public void Reset()
        {
            total = 0;
            count = 0;
            _min = long.MaxValue;
            _max = long.MinValue;
        }

        public void Show()
        {
            Console.Write(name);
            if (count > 0)
            {
                Console.Write(" Min=");
                Console.Write(Min);
                Console.Write("(micros) Max=");
                Console.Write(Max);
                Console.Write("(micros) Mean=");
                Console.Write(Mean);
                Console.Write("(micros) Num=");
                Console.Write(count);
                Console.Write(" Tot=");
                Console.Write(Math.Round(total) / 1000);
                Console.WriteLine(" (millis)");
            }
            else
            {
                Console.WriteLine(" None");
            }
        }
    }
}
