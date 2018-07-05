using System;

namespace PopulationFitness.Models.FastMaths
{
    /**
     * Trades accuracy for speed by resolving to the nearest degree and caching the corresponding sin and cosine values
     */
    public class CosSineCache
    {
        private const int Degrees = 361;
        private const int Entries = Degrees;
        private readonly double[] _cos = new double[Entries];
        private readonly double[] _sin = new double[Entries];

        private static readonly CosSineCache Values = new CosSineCache();

        private CosSineCache()
        {
            for (int i = 0; i < Entries; i++)
            {
                double radians = (Math.PI / 180) * i;
                _cos[i] = Math.Cos(radians);
                _sin[i] = Math.Sin(radians);
            }
        }

        public static double Sin(double angle)
        {
            int angleCircle = (int)angle % 360;
            if (angle < 0)
            {
                return 0 - Values._sin[0 - angleCircle];
            }
            return Values._sin[angleCircle];
        }

        public static double Cos(double angle)
        {
            int angleCircle = (int)angle % 360;
            if (angle < 0)
            {
                return 0 - Values._cos[0 - angleCircle];
            }
            return Values._cos[angleCircle];
        }
    }
}
