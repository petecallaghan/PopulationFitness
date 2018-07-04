using System;
using System.Collections.Generic;

namespace PopulationFitness.Models
{
    public class Epochs
    {
        public readonly List<Epoch> epochs;

        public Epochs()
        {
            epochs = new List<Epoch>();
        }

        /***
         * Adds an epoch with the specified start year.
         * Sets the end year of the preceding epoch to the year
         * before the start of this epoch (if there is a previous epoch)
         *
         * @param epoch
         */
        public void AddNextEpoch(Epoch epoch)
        {
            if (epochs.Count > 0)
            {
                Last.end_year = epoch.start_year - 1;
                epoch.prev_environment_capacity = Last.environment_capacity;
            }
            else
            {
                epoch.prev_environment_capacity = epoch.environment_capacity;
            }

            epochs.Add(epoch);
        }

        public void AddAll(List<Epoch> epochs)
        {
            foreach (Epoch e in epochs)
            {
                AddNextEpoch(e);
            }
        }

        /***
         * Set the end year of the readonly epoch
         *
         * @param last_year
         */
        public void SetFinalEpochYear(int last_year)
        {
            Last.end_year = last_year;
        }

        /***
         *
         * @return the last epoch
         */
        public Epoch Last
        {
            get
            {
                return epochs[epochs.Count - 1];
            }
        }

        public Epoch First
        {
            get
            {
                return epochs[0];
            }
        }

        /**
         * Reduces the populations for all epochs by the same ratio
         *
         * P' = P/ratio
         *
         * @param ratio
         */
        public void ReducePopulation(int ratio)
        {
            foreach (Epoch epoch in epochs)
            {
                epoch.ReducePopulation(ratio);
            }
        }

        /**
         * Increases the populations for all epochs by the same ratio
         *
         * P' = P * ratio
         *
         * @param ratio
         */
        public void IncreasePopulation(int ratio)
        {
            foreach (Epoch epoch in epochs)
            {
                epoch.IncreasePopulation(ratio);
            }
        }

        public void PrintFitnessFactors()
        {
            foreach (Epoch epoch in epochs)
            {
                Console.Write("Epoch ");
                Console.Write(epoch.start_year);
                Console.Write(" f=");
                Console.WriteLine(epoch.Fitness());
            }
        }
    }
}
