using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class SumSquaresGenes : NormalizingBitSetGenes
    {

        private class NormalizationRatioCalculator : IValueCalculator<double>
        {

            public double CalculateValue(long n)
            {
                double sum = 0.0;

                for (int i = 1; i <= n; i++)
                {
                    sum += 100 * i;
                }
                return sum;
            }
        }

        private static readonly ExpensiveCalculatedValues<double> NormalizationRatios = new ExpensiveCalculatedValues<double>(new NormalizationRatioCalculator());

        public SumSquaresGenes(Config config) : base(config, 10.0)
        { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return NormalizationRatios.FindOrCalculate(n);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              http://www.sfu.ca/~ssurjano/sumsqu.html

              f(x) = sum{i=1 to d}[ i * x{i} ^ 2]

              Dimensions: d

             The Sum Squares function, also referred to as the Axis Parallel Hyper-Ellipsoid function,
             has no local minimum except the global one. It is continuous, convex and unimodal.

             Input Domain:

             The function is usually evaluated on the hypercube xi ∈ [-10, 10], for all i = 1, …, d,
             although this may be restricted to the hypercube xi ∈ [-5.12, 5.12], for all i = 1, …, d.

             Global Minimum:

             f(x) = 0, at x = (0,...,0)

             */
            double fitness = 0;

            for (int i = 0; i < integer_values.Length; i++)
            {
                double x = Interpolate(integer_values[i]);
                fitness += (i + 1) * x * x;
            }
            return fitness;
        }
    }
}
