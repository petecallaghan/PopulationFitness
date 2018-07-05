using PopulationFitness.Models.Genes;
using System;

namespace PopulationFitness.Models
{
    /**
     * Created by pete.callaghan on 03/07/2017.
     */
    public class Individual
    {
        private readonly Epoch _epoch;

        public readonly int BirthYear;

        public readonly IGenes Genes;

        public Individual(Epoch epoch, int birth_year)
        {
            _epoch = epoch;
            BirthYear = birth_year;
            Genes = epoch.Config.GenesFactory.Build(epoch.Config);
        }

        public int Age(int current_year)
        {
            return Math.Abs(current_year - BirthYear);
        }

        public bool IsReadyToDie(int current_year)
        {
            return Age(current_year) >= _epoch.MaxAge();
        }

        public bool CanBreed(int current_year)
        {
            int age = Age(current_year);
            return age >= _epoch.Config.MinBreedingAge && age <= _epoch.MaxBreedingAge();
        }

        public int InheritFromParents(Individual mother, Individual father)
        {
            return Genes.InheritFrom(mother.Genes, father.Genes);
        }
    }
}