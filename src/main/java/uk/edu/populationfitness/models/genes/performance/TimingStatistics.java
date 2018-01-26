package uk.edu.populationfitness.models.genes.performance;

class TimingStatistics {

    private final String name;
    private double total;
    private int count;
    private long min;
    private long max;

    public TimingStatistics(String name){
        this.name = name;
        reset();
    }

    public void add(long value){
        count++;
        total += value;
        min = Math.min(value, min);
        max = Math.max(value, max);
    }

    private int count(){
        return this.count;
    }

    public long min(){
        return count == 0 ? 0 : this.min;
    }

    public long max(){
        return count == 0 ? 0 : this.max;
    }

    private long mean(){
        return count > 0 ? Math.round(total / count): 0;
    }

    public void reset()
    {
        total = 0;
        count = 0;
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
    }

    public void show(){
        System.out.print(name);
        if (count > 0){
            System.out.print(" Min=");
            System.out.print(min);
            System.out.print("(micros) Max=");
            System.out.print(max);
            System.out.print("(micros) Mean=");
            System.out.print(mean());
            System.out.print("(micros) Num=");
            System.out.print(count());
            System.out.print(" Tot=");
            System.out.print(Math.round(total) / 1000);
            System.out.println(" (millis)");
        }
        else
        {
            System.out.println(" None");
        }
    }
}
