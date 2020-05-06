package uk.edu.populationfitness.models.population;

class Killed {
    private int max = 0;
    private int count = 0;
    private double total_age_at_death = 0;

    void add(long age){
        count++;
        total_age_at_death += age;
    }

    void setLimit(int max){
        this.max = max;
        count = 0;
        total_age_at_death = 0;
    }

    int remaining() {
        return max - count;
    }

    int count(){
        return this.count;
    }

    double averageAgeKilled(){
        return count < 1 ? 0.0 : Math.round(total_age_at_death / count);
    }
}
