using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.Valley
{
    public class DixonPriceGenes : NormalizingBitSetGenes
    {

        private static readonly double TwoTenSquared = FastMaths.FastMaths.Pow(210.0, 2);

        private class NormalizationRatioCalculator : IValueCalculator<double>
        {

            public double CalculateValue(long n)
            {
                return 121.0 + TwoTenSquared * ((2.0 + n) / 2.0) * (n - 1);
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new DixonPriceGenes.NormalizationRatioCalculator());

        public DixonPriceGenes(Config config) : base(config, 10.0)
        { }


        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) = {left ({x} rsub {1} -1 right )} ^ {2} +  sum from {i=2} to {n} {i {left ({2x} rsub {i} rsup {2} - {x} rsub {i-1} right )} ^ {2}}
             */

            double fitness = integer_values.Length > 0 ? FastMaths.FastMaths.Pow(Interpolate(integer_values[0]) - 1.0, 2) : 0.0;

            for (int i = 1; i < integer_values.Length; i++)
            {
                double xIMinus1 = Interpolate(integer_values[i - 1]);
                double x = Interpolate(integer_values[i]);
                double value = (2.0 * x * x - xIMinus1);
                fitness += (i + 1) * value * value;
            }

            return fitness;
        }
    }
}
