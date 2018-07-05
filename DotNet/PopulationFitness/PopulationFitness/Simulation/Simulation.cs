using PopulationFitness.Models;

namespace PopulationFitness.Simulation
{
    /***
     * Implement this to run a simulation
     */
    public abstract class Simulation
    {
        private Generations _generations = null;

        public Generations Generations
        {
            get
            {
                lock(this)
                {
                    return _generations;
                }
            }
            set
            {
                lock(this)
                {
                    _generations = value;
                }
            }
        }

        protected readonly int RunNumber;

        protected Simulation(int run)
        {
            RunNumber = run;
        }

        /**
         * Implement this to run the simulation
         */
        public abstract void Start();

        /**
         * Implement this to join the thread
         */
        public abstract void Join();
    }
}
