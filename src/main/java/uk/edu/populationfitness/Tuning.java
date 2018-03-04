package uk.edu.populationfitness;

import uk.edu.populationfitness.models.genes.Function;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class Tuning {
    public String id;
    public Function function = Function.Undefined;
    public double historic_fit = 1.0;
    public double disease_fit = 50;
    public double modern_fit = 0.01;
    public double modern_kill = 1.003;
    public double historic_kill = 1.066;
    public double modern_breeding = 0.13;
    public int number_of_genes = 10;
    public int size_of_genes = 4;
    public double min_fitness = 0.0;
    public double max_fitness = 1.0;
    public double mutations_per_gene = 0.2;
    public int series_runs = 1;
    public int parallel_runs = 1;
}
