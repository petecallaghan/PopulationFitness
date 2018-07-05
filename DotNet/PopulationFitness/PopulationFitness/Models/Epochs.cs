using System;
using System.Collections.Generic;

namespace PopulationFitness.Models
{
    public class Epochs
    {
        public readonly List<Epoch> All;

        public Epochs()
        {
            All = new List<Epoch>();
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
            if (All.Count > 0)
            {
                Last.EndYear = epoch.StartYear - 1;
                epoch.PrevEnvironmentCapacity = Last.EnvironmentCapacity;
            }
            else
            {
                epoch.PrevEnvironmentCapacity = epoch.EnvironmentCapacity;
            }

            All.Add(epoch);
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
            Last.EndYear = last_year;
        }

        /***
         *
         * @return the last epoch
         */
        public Epoch Last
        {
            get
            {
                return All[All.Count - 1];
            }
        }

        public Epoch First
        {
            get
            {
                return All[0];
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
            foreach (Epoch epoch in All)
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
            foreach (Epoch epoch in All)
            {
                epoch.IncreasePopulation(ratio);
            }
        }

        public void PrintFitnessFactors()
        {
            foreach (Epoch epoch in All)
            {
                Console.Write("Epoch ");
                Console.Write(epoch.StartYear);
                Console.Write(" f=");
                Console.WriteLine(epoch.Fitness());
            }
        }
    }
}
