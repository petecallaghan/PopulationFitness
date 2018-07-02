using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.LocalMinima
{
    public class AckleysGenes : NormalizingBitSetGenes
    {
        private class NormalizationRatioCalculator : IValueCalculator<Double> {
            public double CalculateValue(long n)
            {
                /*
                  {f left (x right )} over {20 left (1- {e} ^ {-0.2α} right ) +e- {e} ^ {-1}}
                 */
                return 20.0 * (1.0 - Math.Exp(-0.2 * Alpha)) + Math.E - Math.Exp(-1.0);
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new NormalizationRatioCalculator());

        private const double Alpha = 4.5;

        private const double TwentyPlusE = 20.0 + Math.E;

        private const double TwoPi = 2.0 * Math.PI;

        public AckleysGenes(Config config) : base(config, 5.0) { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              http://www.cs.unm.edu/~neal.holts/dga/benchmarkFunction/ackley.html

              f left (x right ) =-20 exp {left (-0.2 sqrt {{1} over {n} sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right )} - exp {left ({1} over {n} sum from {i=1} to {n} {cos {left ({2πx} rsub {i} right )}} right ) +20+ exp⁡ (1)}
             */
            double firstSum = 0.0;
            double secondSum = 0.0;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);
                firstSum += x * x;
                secondSum += CosSineCache.Cos(TwoPi * x);
            }

            double n = integer_values.Length;

            return -20.0 * Math.Exp(-0.2 * Math.Sqrt(firstSum / n)) - Math.Exp(secondSum / n) + TwentyPlusE;
        }
    }
}
