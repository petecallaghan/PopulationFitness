using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class SphereGenes : NormalizingBitSetGenes
    {
        public SphereGenes(Config config) : base(config, 5.12)
        { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return 5.12 * 5.12 * n;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              http://www.sfu.ca/~ssurjano/spheref.html

              f(x) = sum{i=1 to n}[x{i}^2]

              Dimensions: d

             The Sphere function has d local minima except for the global one. It is continuous, convex and unimodal. The plot shows its two-dimensional form.

             Input Domain:

             The function is usually evaluated on the hypercube xi ∈ [-5.12, 5.12], for all i = 1, …, d.

             Global Minimum:

             f(x) = 0, at x = (0,...,0)

             */
            double fitness = 0;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);
                fitness += x * x;
            }
            return fitness;
        }
    }
}
