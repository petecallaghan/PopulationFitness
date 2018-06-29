using System.Collections.Generic;

namespace PopulationFitness.Models.FastMaths
{
    /**
     * Maintains a cache of values identified by longs. Appropriate for values that are expensive to calculate.
     */
    public class ExpensiveCalculatedValues<T>
    {
        private Dictionary<long, T> values = new Dictionary< long, T>();
        private IValueCalculator<T> calculator;

        public ExpensiveCalculatedValues(IValueCalculator<T> calculator)
        {
            this.calculator = calculator;
        }

        /**
         * Finds the value if it has been previously calculated. Otherwise calculates it and stores it in the cache.
         *
         * @param index
         * @return the value corresponding to the index
         */
        public T FindOrCalculate(long index)
        {
            if (values.ContainsKey(index))
            {
                return values.GetValueOrDefault(index);
            }
            T value = calculator.CalculateValue(index);
            values.Add(index, value);
            return value;
        }
    }
}
