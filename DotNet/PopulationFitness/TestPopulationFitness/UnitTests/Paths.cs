using System.IO;

namespace TestPopulationFitness.UnitTests
{
    public static class Paths
    {
        private static readonly string ProjectRoot = Directory.GetParent(Directory.GetCurrentDirectory()).Parent.Parent.Parent.FullName;

        public static string PathOf(string path)
        {
            return ProjectRoot + "\\" + path;
        }
    }
}
