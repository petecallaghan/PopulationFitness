namespace PopulationFitness.Models
{
    public class Epoch
    {

        private static readonly int UNDEFINED_YEAR = -1;

        // Defines the fitness adjustment for this epoch
        private double _fitnessFactor = 1.0;

        // Probability of a pair breeding in a given year
        private double _probabilityOfBreeding;

        private bool _isDisease = false;
        private int _maxAge;
        private int _maxBreedingAge;
        private double _totalCapacityFactor = 0;

        // Indicates unlimited capacity
        public static readonly int UNLIMITED_CAPACITY = 0;
        public readonly int StartYear;
        public int EndYear = UNDEFINED_YEAR;

        // Defines the holding capacity of the environment for this epoch
        public int EnvironmentCapacity = UNLIMITED_CAPACITY;

        public int PrevEnvironmentCapacity;

        // Use this to turn off fitness. By default fitness is enabled
        public bool EnableFitness = true;

        // Max population actually expected for this epoch
        public int ExpectedMaxPopulation = 0;

        public Epoch(Config config, int start_year)
        {
            StartYear = start_year;
            Config = config;
            _probabilityOfBreeding = config.ProbabilityOfBreeding;
            _maxBreedingAge = config.MaxBreedingAge;
            _maxAge = config.MaxAge;
        }

        public Epoch(Epoch source)
        {
            ExpectedMaxPopulation = source.ExpectedMaxPopulation;
            StartYear = source.StartYear;
            EndYear = source.EndYear;
            EnvironmentCapacity = source.EnvironmentCapacity;
            EnableFitness = source.EnableFitness;
            Config = source.Config;
            _fitnessFactor = source._fitnessFactor;
            _isDisease = source._isDisease;
            _probabilityOfBreeding = source._probabilityOfBreeding;
            _totalCapacityFactor = source._totalCapacityFactor;
            PrevEnvironmentCapacity = source.PrevEnvironmentCapacity;
            _maxAge = source._maxAge;
            _maxBreedingAge = source._maxBreedingAge;
        }

        public bool IsCapacityUnlimited
        {
            get
            {
                return EnvironmentCapacity == UNLIMITED_CAPACITY;
            }
        }

        public int CapacityForYear(int year)
        {
            long capacityRange = EnvironmentCapacity - PrevEnvironmentCapacity;
            long yearRange = EndYear - StartYear;
            return PrevEnvironmentCapacity + (int)((capacityRange * (year - StartYear)) / yearRange);
        }

        public bool IsFitnessEnabled
        {
            get
            {
                return EnableFitness;
            }
        }

        public Epoch BreedingProbability(double probability)
        {
            _probabilityOfBreeding = probability;
            return this;
        }

        public double BreedingProbability()
        {
            return _probabilityOfBreeding;
        }

        public Epoch Disease(bool isDisease)
        {
            _isDisease = isDisease;
            return this;
        }

        public bool Disease()
        {
            return _isDisease;
        }

        public Epoch Fitness(double fitness_factor)
        {
            _fitnessFactor = fitness_factor;
            return this;
        }

        public double Fitness()
        {
            return _fitnessFactor;
        }

        public Epoch Capacity(int environment_capacity)
        {
            EnvironmentCapacity = environment_capacity;
            ExpectedMaxPopulation = environment_capacity;
            return this;
        }

        public Epoch Max(int expected_max_population)
        {
            ExpectedMaxPopulation = expected_max_population;
            return this;
        }

        public Epoch AddCapacityFactor(double capacity_factor)
        {
            _totalCapacityFactor += capacity_factor;
            return this;
        }

        public double AverageCapacityFactor()
        {
            return _totalCapacityFactor / (EndYear - StartYear + 1);
        }

        public int MaxAge()
        {
            return _maxAge;
        }

        public Epoch MaxAge(int max_age)
        {
            _maxAge = max_age;
            return this;
        }

        public int MaxBreedingAge()
        {
            return _maxBreedingAge;
        }

        public Epoch MaxBreedingAge(int max_breeding_age)
        {
            _maxBreedingAge = max_breeding_age;
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
            ExpectedMaxPopulation = ExpectedMaxPopulation / ratio;
            EnvironmentCapacity = EnvironmentCapacity / ratio;
            PrevEnvironmentCapacity = PrevEnvironmentCapacity / ratio;
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
            ExpectedMaxPopulation = ExpectedMaxPopulation * ratio;
            EnvironmentCapacity = EnvironmentCapacity * ratio;
            PrevEnvironmentCapacity = PrevEnvironmentCapacity * ratio;
            return this;
        }

        public Config Config { get; }
    }
}
