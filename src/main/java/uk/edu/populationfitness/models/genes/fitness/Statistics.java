package uk.edu.populationfitness.models.genes.fitness;

public class Statistics {
    private double min = Double.MAX_VALUE;

    private double max = Double.MIN_VALUE;

    public void add(double value){
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    public void show(){
        System.out.println("Min="+min+" Max="+max);
    }
}
