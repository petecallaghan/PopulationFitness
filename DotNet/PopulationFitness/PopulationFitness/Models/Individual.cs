using PopulationFitness.Models.Genes;
using System;

namespace PopulationFitness.Models
{
    /**
     * Created by pete.callaghan on 03/07/2017.
     */
    public class Individual
    {
        private readonly Epoch epoch;

        public readonly int birth_year;

        public readonly IGenes genes;

        public Individual(Epoch epoch, int birth_year)
        {
            this.epoch = epoch;
            this.birth_year = birth_year;
            this.genes = epoch.Config().GetGenesFactory().Build(epoch.Config());
        }

        public int Age(int current_year)
        {
            return Math.Abs(current_year - birth_year);
        }

        public bool IsReadyToDie(int current_year)
        {
            return Age(current_year) >= epoch.MaxAge();
        }

        public bool CanBreed(int current_year)
        {
            int age = Age(current_year);
            return age >= epoch.Config().GetMinBreedingAge() && age <= epoch.MaxBreedingAge();
        }

        public int InheritFromParents(Individual mother, Individual father)
        {
            return genes.InheritFrom(mother.genes, father.genes);
        }
    }
}