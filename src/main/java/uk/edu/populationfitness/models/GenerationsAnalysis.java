package uk.edu.populationfitness.models;

public class GenerationsAnalysis {
    public final long maxExpectedPopulation;

    public final long historicalPopulationBeforeDisease;

    public final long historicalDiseaseTotalDeaths;

    public final long modernPopulationBeforeDisease;

    public final long modernDiseaseTotalDeaths;

    public final long totalBorn;

    public final long totalKilled;

    public GenerationsAnalysis(Iterable<GenerationStatistics> history) {
        long maxPop = 0;
        long born = 0;
        long killed = 0;
        long previousPopulation = 0;
        Epoch historicalDisease = null;
        Epoch modernDisease = null;
        long historicalPop = 0;
        long modernPop = 0;
        long historicalKilled = 0;
        long modernKilled = 0;
        for (GenerationStatistics generation: history) {
            maxPop = Math.max(maxPop, generation.epoch.expected_max_population);
            born += generation.number_born;
            killed += generation.number_killed;
            if (generation.epoch.disease()){
                if (historicalDisease == null){
                    historicalDisease = generation.epoch;
                    historicalPop = previousPopulation;
                    historicalKilled = 0;
                }
                if (historicalDisease.start_year == generation.epoch.start_year){
                    // Historical disease
                    historicalKilled += generation.number_killed;
                }
                else
                {
                    // Modern disease
                    if (modernDisease == null){
                        modernDisease = generation.epoch;
                        modernPop = previousPopulation;
                        modernKilled = 0;
                    }

                    if (modernDisease.start_year == generation.epoch.start_year){
                        // Modern disease
                        modernKilled += generation.number_killed;
                    }
                    else {
                        // Reset to later epoch
                        modernDisease = generation.epoch;
                        modernPop = previousPopulation;
                        modernKilled = generation.number_killed;
                    }
                }
            }

            previousPopulation = generation.population;
        }

        maxExpectedPopulation = maxPop;
        historicalPopulationBeforeDisease = historicalPop;
        historicalDiseaseTotalDeaths = historicalKilled;
        modernPopulationBeforeDisease = modernPop;
        modernDiseaseTotalDeaths = modernKilled;
        totalBorn = born;
        totalKilled = killed;
    }
}
