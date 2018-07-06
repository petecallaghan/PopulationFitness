using System;
using CsvHelper;

namespace PopulationFitness.Output
{
    internal static class CsvReaderExtensions
    {
        /**
         * Read a double field without risking rounding errors
         */ 
        public static double GetDoubleFieldWithoutRounding(this CsvReader row, int column)
        {
            string asString = row.GetField<string>(column);
            if (Double.TryParse(asString, out double value))
            {
                return value;
            }

            return 0;
        }
    }
}
