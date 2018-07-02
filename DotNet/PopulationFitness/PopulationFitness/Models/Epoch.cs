namespace PopulationFitness.Models
{
    public class Epoch
    {

        private static readonly int UNDEFINED_YEAR = -1;

        // Indicates unlimited capacity
        public static readonly int UNLIMITED_CAPACITY = 0;

        private readonly Config config;

        public readonly int start_year;

        public int end_year = UNDEFINED_YEAR;

        // Defines the fitness adjustment for this epoch
        private double fitness_factor = 1.0;

        // Defines the holding capacity of the environment for this epoch
        public int environment_capacity = UNLIMITED_CAPACITY;

        public int prev_environment_capacity;

        // Use this to turn off fitness. By default fitness is enabled
        public bool enable_fitness = true;

        // Max population actually expected for this epoch
        public int expected_max_population = 0;

        // Probability of a pair breeding in a given year
        private double probability_of_breeding;

        private bool isDisease = false;

        private int max_age;

        private int max_breeding_age;

        private double total_capacity_factor = 0;

        public Epoch(Config config, int start_year)
        {
            this.start_year = start_year;
            this.config = config;
            this.probability_of_breeding = config.GetProbabilityOfBreeding();
            this.max_breeding_age = config.GetMaxBreedingAge();
            this.max_age = config.GetMaxAge();
        }

        public Epoch(Epoch source)
        {
            this.expected_max_population = source.expected_max_population;
            this.start_year = source.start_year;
            this.end_year = source.end_year;
            this.environment_capacity = source.environment_capacity;
            this.enable_fitness = source.enable_fitness;
            this.config = source.config;
            this.fitness_factor = source.fitness_factor;
            this.isDisease = source.isDisease;
            this.probability_of_breeding = source.probability_of_breeding;
            this.total_capacity_factor = source.total_capacity_factor;
            this.prev_environment_capacity = source.prev_environment_capacity;
            this.max_age = source.max_age;
            this.max_breeding_age = source.max_breeding_age;
        }

        public bool IsCapacityUnlimited
        {
            get
            {
                return environment_capacity == UNLIMITED_CAPACITY;
            }
        }

        public int CapacityForYear(int year)
        {
            long capacityRange = environment_capacity - prev_environment_capacity;
            long yearRange = end_year - start_year;
            return prev_environment_capacity + (int)((capacityRange * (year - start_year)) / yearRange);
        }

        public bool IsFitnessEnabled
        {
            get
            {
                return enable_fitness;
            }
        }

        public Epoch BreedingProbability(double probability)
        {
            probability_of_breeding = probability;
            return this;
        }

        public double BreedingProbability()
        {
            return probability_of_breeding;
        }

        public Epoch Disease(bool isDisease)
        {
            this.isDisease = isDisease;
            return this;
        }

        public bool Disease()
        {
            return isDisease;
        }

        public Epoch Fitness(double fitness_factor)
        {
            this.fitness_factor = fitness_factor;
            return this;
        }

        public double Fitness()
        {
            return this.fitness_factor;
        }

        public Epoch Capacity(int environment_capacity)
        {
            this.environment_capacity = environment_capacity;
            this.expected_max_population = environment_capacity;
            return this;
        }

        public Epoch Max(int expected_max_population)
        {
            this.expected_max_population = expected_max_population;
            return this;
        }

        public Epoch AddCapacityFactor(double capacity_factor)
        {
            this.total_capacity_factor += capacity_factor;
            return this;
        }

        public double AverageCapacityFactor()
        {
            return this.total_capacity_factor / (this.end_year - this.start_year + 1);
        }

        public int MaxAge()
        {
            return max_age;
        }

        public Epoch MaxAge(int max_age)
        {
            this.max_age = max_age;
            return this;
        }

        public int MaxBreedingAge()
        {
            return max_breeding_age;
        }

        public Epoch MaxBreedingAge(int max_breeding_age)
        {
            this.max_breeding_age = max_breeding_age;
            return this;
        }


        /**
         * Reduces the populations by the ratio
         *
         * P' = P/ratio
         *
         * @param ratio
         */
        public Epoch ReducePopulation(int ratio)
        {
            this.expected_max_population = this.expected_max_population / ratio;
            this.environment_capacity = this.environment_capacity / ratio;
            this.prev_environment_capacity = this.prev_environment_capacity / ratio;
            return this;
        }

        /**
         * Increases the populations by the ratio
         *
         * P' = P * ratio
         *
         * @param ratio
         */
        public Epoch IncreasePopulation(int ratio)
        {
            this.expected_max_population = this.expected_max_population * ratio;
            this.environment_capacity = this.environment_capacity * ratio;
            this.prev_environment_capacity = this.prev_environment_capacity * ratio;
            return this;
        }

        public Config Config()
        {
            return this.config;
        }
    }
}
