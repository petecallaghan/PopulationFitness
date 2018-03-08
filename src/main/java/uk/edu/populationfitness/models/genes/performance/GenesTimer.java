package uk.edu.populationfitness.models.genes.performance;

import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.GenesIdentifier;

public class GenesTimer implements Genes {
    private static final long NANOS_PER_MICROS = 1000;

    private final Genes delegate;

    private static final TimingStatistics BuildRandom = new TimingStatistics("Build random genes");
    private static final TimingStatistics Inherit = new TimingStatistics("Inherit from parents");
    private static final TimingStatistics Fitness = new TimingStatistics("Calculate fitness");

    public static void resetAll()
    {
        BuildRandom.reset();
        Inherit.reset();
        Fitness.reset();
    }

    public static void showAll(){
        BuildRandom.show();
        Inherit.show();
        Fitness.show();
    }

    public GenesTimer(Genes delegate){
        this.delegate = delegate;
    }

    private long getElapsed(long start){
        return (System.nanoTime() - start) / NANOS_PER_MICROS;
    }

    @Override
    public void buildEmpty() {
        delegate.buildEmpty();
    }

    @Override
    public void buildFull() {
        delegate.buildFull();
    }

    @Override
    public int getCode(int index) {
        return delegate.getCode(index);
    }

    @Override
    public void buildFromRandom() {
        long start_time = System.nanoTime();
        delegate.buildFromRandom();
        BuildRandom.add(getElapsed(start_time));
    }

    @Override
    public int numberOfBits() {
        return delegate.numberOfBits();
    }

    @Override
    public void mutate() {
        delegate.mutate();
    }

    @Override
    public void inheritFrom(Genes mother, Genes father) {
        long start_time = System.nanoTime();
        delegate.inheritFrom(mother, father);
        Inherit.add(getElapsed(start_time));
    }

    @Override
    public boolean areEmpty() {
        return delegate.areEmpty();
    }

    @Override
    public double fitness() {
        long start_time = System.nanoTime();
        double fitness = delegate.fitness();
        Fitness.add(getElapsed(start_time));
        return fitness;
    }

    @Override
    public boolean isEqual(Genes other) {
        return delegate.isEqual(other);
    }

    @Override
    public Genes getImplementation() {
        return delegate;
    }

    @Override
    public long[] asIntegers() {
        return delegate.asIntegers();
    }

    @Override
    public GenesIdentifier identifier() {
        return delegate.identifier();
    }
}
