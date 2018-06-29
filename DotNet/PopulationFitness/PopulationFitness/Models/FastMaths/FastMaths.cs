namespace PopulationFitness.Models.FastMaths
{
    public class FastMaths
    {
        /**
         * Optimised version of power
         * @param baseValue
         * @param power
         * @return base raised to the power
         */
        public static double Pow(double baseValue, long power)
        {
            double result = 1;
            while (power > 0)
            {
                if ((power & 1) == 1)
                {
                    result *= baseValue;
                }
                power >>= 1;
                baseValue *= baseValue;
            }
            return result;
        }
    }
}
