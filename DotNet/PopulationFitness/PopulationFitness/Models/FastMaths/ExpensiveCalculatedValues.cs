using System.Collections.Generic;

namespace PopulationFitness.Models.FastMaths
{
    /**
     * Maintains a cache of values identified by longs. Appropriate for values that are expensive to calculate.
     */
    public class ExpensiveCalculatedValues<T>
    {
        private Dictionary<long, T> _values = new Dictionary< long, T>();
        private IValueCalculator<T> _calculator;

        public ExpensiveCalculatedValues(IValueCalculator<T> calculator)
        {
            _calculator = calculator;
        }

        /**
         * Finds the value if it has been previously calculated. Otherwise calculates it and stores it in the cache.
         *
         * @param index
         * @return the value corresponding to the index
         */
        public T FindOrCalculate(long index)
        {
            lock (_values)
            {
                if (_values.ContainsKey(index))
                {
                    return _values.GetValueOrDefault(index);
                }
                T value = _calculator.CalculateValue(index);
                _values.Add(index, value);
                return value;
            }
        }
    }
}
