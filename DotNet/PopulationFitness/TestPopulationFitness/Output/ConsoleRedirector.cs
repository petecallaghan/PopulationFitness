using System;
using System.Diagnostics;
using System.IO;
using System.Text;

namespace TestPopulationFitness.Output
{
    public class ConsoleRedirector : TextWriter
    {
        public override void Write(string value)
        {
            Debug.Write(value);
            base.Write(value);
        }

        public override void WriteLine(string value)
        {
            Debug.WriteLine(value);
            base.WriteLine(value);
        }

        public override Encoding Encoding
        {
            get { return Encoding.Default; }
        }

        public static void Redirect()
        {
            Console.SetOut(new ConsoleRedirector());
        }
    }
}
