using System;

namespace PopulationFitness.Models.FastMaths
{
    /**
     * Trades accuracy for speed by resolving to the nearest degree and caching the corresponding sin and cosine values
     */
    public class CosSineCache
    {
        private static readonly int Degrees = 361;
        private static readonly int Entries = Degrees;
        private readonly double[] cos = new double[Entries];
        private readonly double[] sin = new double[Entries];

        private static readonly CosSineCache Values = new CosSineCache();

        private CosSineCache()
        {
            for (int i = 0; i < Entries; i++)
            {
                double radians = (Math.PI / 180) * i;
                cos[i] = Math.Cos(radians);
                sin[i] = Math.Sin(radians);
            }
        }

        public static double Sin(double angle)
        {
            int angleCircle = (int)angle % 360;
            if (angle < 0)
            {
                return 0 - Values.sin[0 - angleCircle];
            }
            return Values.sin[angleCircle];
        }

        public static double Cos(double angle)
        {
            int angleCircle = (int)angle % 360;
            if (angle < 0)
            {
                return 0 - Values.cos[0 - angleCircle];
            }
            return Values.cos[angleCircle];
        }
    }
}
