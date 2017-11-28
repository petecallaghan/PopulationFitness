package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.*;
import uk.edu.populationfitness.models.genes.localmimina.*;
import uk.edu.populationfitness.models.genes.sinpi.SinPiAvgGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiLinearGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiOver2Genes;
import uk.edu.populationfitness.models.genes.sphere.*;
import uk.edu.populationfitness.models.genes.valley.DixonPriceGenes;
import uk.edu.populationfitness.models.genes.valley.RosenbrockGenes;

import static uk.edu.populationfitness.models.genes.Function.SinPiOver2;
import static uk.edu.populationfitness.models.genes.Function.Undefined;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class BitSetGenesFactory implements GenesFactory {

    /**
     * Defines the fitness function to use
     */
    public Function function = Undefined;

    @Override
    public Genes build(Config config) {
        switch(function){
            case SinPi:
                return new SinPiGenes(config);
            case SinPiAvg:
                return new SinPiAvgGenes(config);
            case SinPiLinear:
                return new SinPiLinearGenes(config);
            case Rastrigin:
                return new RastriginGenes(config);
            case Sphere:
                return new SphereGenes(config);
            case StyblinksiTang:
                return new StyblinksiTangGenes(config);
            case Schwefel:
                return new SchwefelGenes(config);
            case Rosenbrock:
                return new RosenbrockGenes(config);
            case SumOfPowers:
                return new SumOfPowersGenes(config);
            case SumSquares:
                return new SumSquaresGenes(config);
            case Ackleys:
                return new AckleysGenes(config);
            case Alpine:
                return new AlpineGenes(config);
            case Brown:
                return new BrownGenes(config);
            case ChungReynolds:
                return new ChungReynoldsGenes(config);
            case DixonPrice:
                return new DixonPriceGenes(config);
            case Exponential:
                return new ExponentialGenes(config);
            case Griewank:
                return new GriewankGenes(config);
            case Qing:
                return new QingGenes(config);
            case Salomon:
                return new SalomonGenes(config);
            case SchumerSteiglitz:
                return new SchumerSteiglitzGenes(config);
            default:
            case SinPiOver2:
                return new SinPiOver2Genes(config);
        }
    }
}
