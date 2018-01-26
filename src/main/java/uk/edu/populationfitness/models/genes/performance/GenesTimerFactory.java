package uk.edu.populationfitness.models.genes.performance;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.GenesFactory;

public class GenesTimerFactory implements GenesFactory {
    private final GenesFactory delegate;

    public GenesTimerFactory(GenesFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public Genes build(Config config) {
        Genes genes = delegate.build(config);
        return new GenesTimer(genes);
    }

    @Override
    public void useFitnessFunction(Function function) {
        delegate.useFitnessFunction(function);
    }

    @Override
    public Function getFitnessFunction() {
        return delegate.getFitnessFunction();
    }
}
