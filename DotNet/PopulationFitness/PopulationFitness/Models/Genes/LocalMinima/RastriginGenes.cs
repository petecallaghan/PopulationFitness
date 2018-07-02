using PopulationFitness.Models.FastMaths;
using PopulationFitness.Models.Genes.BitSet;
using System;

namespace PopulationFitness.Models.Genes.LocalMinima
{
    public class RastriginGenes : NormalizingBitSetGenes
    {

    private const long RastriginTermA = 10;  // The A term in f{x}=sum _{i=1}^{n}[t[x_{i}^{2}-A\cos(2\pi x_{i})]

    private const double TwoPi = 2 * Math.PI;

    public RastriginGenes(Config config) : base(config, 5.12) { }

    protected override double CalculateNormalizationRatio(int n)
    {
        return 40.25 * n;
    }

    protected override double CalculateFitnessFromIntegers(long[] integer_values)
    {
        /*
          This is a tunable Rastrigin function: https://en.wikipedia.org/wiki/Rastrigin_function

          f(x)=sum{i=1 to n}[x{i}^2-  A cos(2pi x{i})]

          The '2pi' term is replaced by 'fitness_factor * pi' to make the function tunable
         */
        return GetRastriginFitnessUsingCos(RastriginTermA * integer_values.Length, integer_values);
    }

    private double GetRastriginFitnessUsingCos(double fitness, long[] integer_values)
    {
        foreach (long integer_value in integer_values)
        {
            double x = Interpolate(integer_value);
            fitness += x * x - RastriginTermA * CosSineCache.Cos(TwoPi * x);
        }
        return fitness;
    }
}
}
