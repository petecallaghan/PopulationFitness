namespace PopulationFitness.Models.FastMaths
{
    public interface IValueCalculator<out T>
    {
        /**
         * @param index the source index that identifies the value
         * @return the value corresponding to the index
         */
        T CalculateValue(long index);
    }
}
