namespace PopulationFitness.Models.Genes
{
    /**
     * Implement this to provide genes
     *
     * Created by pete.callaghan on 13/07/2017.
     */
    public interface IGenes
    {

        /**
         * Builds a set of genes with none set
         */
        void BuildEmpty();

        /**
         * Builds a set of genes with all set
         */
        void BuildFull();

        /**
         * Gets the value for a single code
         *
         * @param index
         * @return
         */
        int GetCode(int index);

        /**
         * Builds a set of genes with random values
         */
        void BuildFromRandom();

        /**
         * The number of codes in the gene
         *
         * @return
         */
        int NumberOfBits { get; }

        /**
         * Randomly mutate
         *
         * @return the number of bits mutated
         */
        int Mutate();

        /**
         * Inherit from parents.
         *
         * @param mother
         * @param father
         *
         * @return the number of bits mutated for the offspring
         */
        int InheritFrom(IGenes mother, IGenes father);

        /**
         *
         * @return true if none of the codes are set
         */
        bool AreEmpty { get; }

        /**
         * Calculate the fitness in a range of 0..1

         * @return
         */
        double Fitness { get; }

        /**
         *
         * @param other
         * @return true if the encodings are all the same
         */
        bool IsEqual(IGenes other);

        /**
         * @return the class implementing the interface
         */
        IGenes Implementation { get; }

        /**
         *
         * @return the genes as an array of integers
         */
        long[] AsIntegers { get; }

        /**
         *
         * @return the unique identifier the genes
         */
        IGenesIdentifier Identifier { get; }
    }

}
