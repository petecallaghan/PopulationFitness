package uk.edu.populationfitness.models.fastmaths;

import java.util.HashMap;

/**
 * Maintains a cache of values identified by longs. Appropriate for values that are expensive to calculate.
 */
public class ExpensiveCalculatedValues {
    private final HashMap<Long, Double> values = new HashMap<>();
    private final ValueCalculator calculator;

    public ExpensiveCalculatedValues(ValueCalculator calculator) {
        this.calculator = calculator;
    }

    /**
     * Finds the value if it has been previously calculated. Otherwise calculates it and stores it in the cache.
     *
     * @param index
     * @return the value corresponding to the index
     */
    public double findOrCalculate(long index){
        if (values.containsKey(index))
        {
            return values.get(index);
        }
        double value = calculator.calculateValue(index);
        values.put(index, value);
        return value;
    }
}
