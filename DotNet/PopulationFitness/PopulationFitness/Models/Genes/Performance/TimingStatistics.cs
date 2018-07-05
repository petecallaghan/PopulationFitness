using System;
using System.Diagnostics;

namespace PopulationFitness.Models.Genes.Performance
{
    class TimingStatistics
    {

        private readonly String _name;
        private double _total;
        private int _count;
        private long _max;
        private long _min;

        public TimingStatistics(String name)
        {
            this._name = name;
            Reset();
        }

        public void Add(long value)
        {
            _count++;
            _total += value;
            _min = Math.Min(value, _min);
            _max = Math.Max(value, _max);
        }

        public long Min
        {
            get
            {
                return _count == 0 ? 0 : _min;
            }
        }

        public long Max
        {
            get
            {
                return _count == 0 ? 0 : _max;
            }
        }

        private long Mean
        {
            get
            {
                return _count > 0 ? (long)Math.Round(_total / _count) : 0;
            }
        }

        public void Reset()
        {
            _total = 0;
            _count = 0;
            _min = long.MaxValue;
            _max = long.MinValue;
        }

        public void Show()
        {
            Debug.Write(_name);
            if (_count > 0)
            {
                Debug.Write(" Min=");
                Debug.Write(Min);
                Debug.Write("(micros) Max=");
                Debug.Write(Max);
                Debug.Write("(micros) Mean=");
                Debug.Write(Mean);
                Debug.Write("(micros) Num=");
                Debug.Write(_count);
                Debug.Write(" Tot=");
                Debug.Write(Math.Round(_total) / 1000);
                Debug.WriteLine(" (millis)");
            }
            else
            {
                Debug.WriteLine(" None");
            }
        }
    }
}
