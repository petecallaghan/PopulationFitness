package uk.edu.populationfitness;

import uk.edu.populationfitness.models.genes.Function;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class Tuning {
    public static double population_scale = 1.0;

    public double historic_fit = 1.0;
    public double disease_fit = 50;
    public double modern_fit = 0.01;
    public double modern_kill = 1.003;
    public double historic_kill = 1.066;
    public double modern_breeding = 0.13;

    private static final Tuning sin_pi_over_2_40b = new Tuning();
    private static final Tuning sin_pi_over_2_1000b = new Tuning();

    private static void build(){
        sin_pi_over_2_40b.historic_fit = 1.0;
        sin_pi_over_2_40b.disease_fit = 50;
        sin_pi_over_2_40b.modern_fit = 0.01;
        sin_pi_over_2_40b.modern_kill = 1.003;
        sin_pi_over_2_40b.historic_kill = 1.066;
        sin_pi_over_2_40b.modern_breeding = 0.13;

        sin_pi_over_2_1000b.historic_fit = 14.1;
        sin_pi_over_2_1000b.disease_fit = 50;
        sin_pi_over_2_1000b.modern_fit = 0.01;
        sin_pi_over_2_1000b.modern_kill = 1.003;
        sin_pi_over_2_1000b.historic_kill = 1.066;
        sin_pi_over_2_1000b.modern_breeding = 0.13;
    }

    public static Tuning select(Function function, int size)
    {
        build();
        switch(function)
        {
            case SinPiOver2:
                if (size == 40){
                    return sin_pi_over_2_40b;
                }
                if (size == 1000){
                    return sin_pi_over_2_1000b;
                }
        }
        return new Tuning();
    }
}
