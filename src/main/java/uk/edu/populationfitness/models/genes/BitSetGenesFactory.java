package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

import static uk.edu.populationfitness.models.genes.BitSetGenesFactory.Fitness.SinPiOver2;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class BitSetGenesFactory implements GenesFactory {
    public enum Fitness {
        SinPi,
        SinPiOver2;
    }

    /**
     * Defines the fitness function to use
     */
    public Fitness fitness = SinPiOver2;

    @Override
    public Genes build(Config config) {
        switch(fitness){
            case SinPi:
                return new SinPiBitSetGenes(config);
            default:
            case SinPiOver2:
                return new SinPiOver2BitSetGenes(config);
        }
    }
}
