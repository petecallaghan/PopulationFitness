using PopulationFitness.Models.Genes.BitSet;

namespace PopulationFitness.Models.Genes.Sphere
{
    public class ChungReynoldsGenes : NormalizingBitSetGenes
    {

        private static readonly double TenToThe8th = FastMaths.FastMaths.Pow(10.0, 8);

        public ChungReynoldsGenes(Config config) : base(config, 100.0)
        {
        }

        protected override double CalculateNormalizationRatio(int n)
        {
            return TenToThe8th * FastMaths.FastMaths.Pow(n, 2);
        }

        protected override double CalculateFitnessFromIntegers(long[] integer_values)
        {
            /*
              f left (x right ) = {left (sum from {i=1} to {n} {{x} rsub {i} rsup {2}} right )} ^ {2}
             */
            double fitness = 0.0;

            foreach (long integer_value in integer_values)
            {
                double x = Interpolate(integer_value);

                fitness += x * x;
            }

            return fitness * fitness;
        }
    }
}
