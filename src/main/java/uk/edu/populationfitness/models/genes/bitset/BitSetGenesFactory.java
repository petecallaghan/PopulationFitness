package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.*;
import uk.edu.populationfitness.models.genes.localmimina.RastriginGenes;
import uk.edu.populationfitness.models.genes.localmimina.SchwefelGenes;
import uk.edu.populationfitness.models.genes.localmimina.StyblinksiTangGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiAvgGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiLinearGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiOver2Genes;
import uk.edu.populationfitness.models.genes.sphere.SphereGenes;
import uk.edu.populationfitness.models.genes.sphere.SumOfPowersGenes;
import uk.edu.populationfitness.models.genes.sphere.SumSquaresGenes;
import uk.edu.populationfitness.models.genes.valley.RosenbrockGenes;

import static uk.edu.populationfitness.models.genes.Function.SinPiOver2;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class BitSetGenesFactory implements GenesFactory {

    /**
     * Defines the fitness function to use
     */
    public Function function = SinPiOver2;

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
            default:
            case SinPiOver2:
                return new SinPiOver2Genes(config);
        }
    }
}
