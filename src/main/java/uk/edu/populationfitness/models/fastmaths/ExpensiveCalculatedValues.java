package uk.edu.populationfitness.models.fastmaths;

import java.util.HashMap;

/**
 * Maintains a cache of values identified by longs. Appropriate for values that are expensive to calculate.
 */
public class ExpensiveCalculatedValues<T> {
    private final HashMap<Long, T> values = new HashMap<>();
    private final ValueCalculator<T> calculator;

    public ExpensiveCalculatedValues(ValueCalculator<T> calculator) {
        this.calculator = calculator;
    }

    /**
     * Finds the value if it has been previously calculated. Otherwise calculates it and stores it in the cache.
     *
     * @param index
     * @return the value corresponding to the index
     */
    public T findOrCalculate(long index){
        if (values.containsKey(index))
        {
            return values.get(index);
        }
        T value = calculator.calculateValue(index);
        values.put(index, value);
        return value;
    }
}
