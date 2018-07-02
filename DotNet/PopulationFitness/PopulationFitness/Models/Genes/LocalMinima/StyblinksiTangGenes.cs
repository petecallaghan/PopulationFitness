using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.LocalMinima
{
    public class StyblinksiTangGenes : NormalizingBitSetGenes
    {

        public StyblinksiTangGenes(Config config) : base(config, 5.0) { }

        protected override double CalculateNormalizationRatio(int n)
        {
            return 85.834 * n;
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              http://www.sfu.ca/~ssurjano/stybtang.html

              f(x) = 1/2 * sum{i=1 to n}[x{i}^4 - 16x{i}^2 + 5x{i}]

              Dimensions: d

             The function is usually evaluated on the hypercube xi ∈ [-5, 5], for all i = 1, …, d.

             Global Minimum:

             f(x) = -39.16599d at x = (-2.903534,...-2.903534)
             */
            double fitness = 39.166 * integer_values.Length;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);
                double xSquared = x * x;
                fitness += (xSquared * xSquared - 16 * xSquared + 5 * x);
            }

            return fitness / 2;
        }
    }
}