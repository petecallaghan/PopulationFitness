package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.*;
import uk.edu.populationfitness.models.genes.Ridge.*;
import uk.edu.populationfitness.models.genes.localmimina.*;
import uk.edu.populationfitness.models.genes.reference.*;
import uk.edu.populationfitness.models.genes.sphere.*;
import uk.edu.populationfitness.models.genes.valley.*;

import static uk.edu.populationfitness.models.genes.Function.Undefined;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class BitSetGenesFactory implements GenesFactory {

    /**
     * Defines the fitness function to use
     */
    private Function function = Undefined;

    @Override
    public Genes build(Config config) {
        switch(function){
            case Rastrigin:
                return new RastriginGenes(config);
            case Sphere:
                return new SphereGenes(config);
            case StyblinksiTang:
                return new StyblinksiTangGenes(config);
            case Schwefel226:
                return new Schwefel226Genes(config);
            case Schwefel220:
                return new Schwefel220Genes(config);
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
            case Trid:
                return new TridGenes(config);
            case Zakharoy:
                return new ZakharoyGenes(config);
            case FixedZero:
                return new FixedZeroGenes(config);
            case FixedHalf:
                return new FixedHalfGenes(config);
            case FixedOne:
                return new FixedOneGenes(config);
            case SinX:
                return new SinXGenes(config);
            default:
            case Random:
                return new RandomGenes(config);
        }
    }

    @Override
    public void useFitnessFunction(Function function) {
        this.function = function;
    }

    @Override
    public Function getFitnessFunction() {
        return function;
    }
}
