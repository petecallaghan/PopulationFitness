package uk.edu.populationfitness.models.fastmaths;

/***
 * Implement this to populate cached calculated values
 */
public interface ValueCalculator {
    /**
     *
     * @param index the source index that identifies the value
     * @return the value corresponding to the index
     */
    double calculateValue(long index);
}
