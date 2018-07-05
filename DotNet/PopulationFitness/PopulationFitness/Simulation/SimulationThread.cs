using PopulationFitness.Models;
using PopulationFitness.Models.Genes.Cache;
using PopulationFitness.Output;
using System;
using System.Threading;

namespace PopulationFitness.Simulation
{
    class SimulationThread : Simulation
    {
        private readonly Config _config;
        private readonly Epochs _epochs;
        private readonly Tuning _tuning;
        private readonly Thread _thread;

        public SimulationThread(Config config, Epochs epochs, Tuning tuning, int run) : base(run)
        {
            _config = config;
            _epochs = epochs;
            _tuning = tuning;
            Generations = null;

            _thread = new Thread(() => { RunAllInSeries(); });
        }

        public override void Start()
        {
            _thread.Start();
        }

        public override void Join()
        {
            _thread.Join();
        }

        private void RunAllInSeries()
        {
            Generations total = null;

            for (int series_run = 1; series_run <= _tuning.SeriesRuns; series_run++)
            {
                Generations current = RunSimulation(series_run);
                try
                {
                    total = GenerationsWriter.CombineGenerationsAndWriteResult(RunNumber, series_run, current, total, _tuning);
                }
                catch (Exception e)
                {
                    Console.Write(e.StackTrace);
                }
                SharedCache.Cache.Close();
            }

            Generations = total;
        }

        private Generations RunSimulation(int series_run)
        {
            Generations current = new Generations(new Population(_config), RunNumber, series_run);
            current.CreateForAllEpochs(_epochs);
            return current;
        }
    }
}
