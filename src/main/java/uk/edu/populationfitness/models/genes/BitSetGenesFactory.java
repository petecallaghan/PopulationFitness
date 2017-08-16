package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

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
                return new SinPiBitSetGenes(config);
            case SinPiAvg:
                return new SinPiAvgBitSetGenes(config);
            case SinPiLinear:
                return new SinPiLinearBitSetGenes(config);
            default:
            case SinPiOver2:
                return new SinPiOver2BitSetGenes(config);
        }
    }
}
