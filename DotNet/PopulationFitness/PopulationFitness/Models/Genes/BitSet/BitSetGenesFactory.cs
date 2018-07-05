using PopulationFitness.Models.Genes.LocalMinima;
using PopulationFitness.Models.Genes.Ridge;
using PopulationFitness.Models.Genes.Sphere;
using PopulationFitness.Models.Genes.Valley;

namespace PopulationFitness.Models.Genes.BitSet
{
    /**
     * Created by pete.callaghan on 13/07/2017.
     */
    public class BitSetGenesFactory : IGenesFactory
    {
        public IGenes Build(Config config)
        {
            switch (FitnessFunction)
            {
                case Function.Rastrigin:
                    return new RastriginGenes(config);
                case Function.Sphere:
                    return new SphereGenes(config);
                case Function.StyblinksiTang:
                    return new StyblinksiTangGenes(config);
                case Function.Schwefel226:
                    return new Schwefel226Genes(config);
                case Function.Schwefel220:
                    return new Schwefel220Genes(config);
                case Function.Rosenbrock:
                    return new RosenbrockGenes(config);
                case Function.SumOfPowers:
                    return new SumOfPowersGenes(config);
                case Function.SumSquares:
                    return new SumSquaresGenes(config);
                case Function.Ackleys:
                    return new AckleysGenes(config);
                case Function.Alpine:
                    return new AlpineGenes(config);
                case Function.Brown:
                    return new BrownGenes(config);
                case Function.ChungReynolds:
                    return new ChungReynoldsGenes(config);
                case Function.DixonPrice:
                    return new DixonPriceGenes(config);
                case Function.Exponential:
                    return new ExponentialGenes(config);
                case Function.Griewank:
                    return new GriewankGenes(config);
                case Function.Qing:
                    return new QingGenes(config);
                case Function.Salomon:
                    return new SalomonGenes(config);
                case Function.SchumerSteiglitz:
                    return new SchumerSteiglitzGenes(config);
                case Function.Trid:
                    return new TridGenes(config);
                default:
                case Function.Zakharoy:
                    return new ZakharoyGenes(config);
            }
        }

        public Function FitnessFunction { get; set; } = Function.Undefined;
    }
}
