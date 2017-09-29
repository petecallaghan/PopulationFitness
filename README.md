# PopulationFitness

## Java Version

1. Install the latest JDK: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. Install Maven (to build the Java project): https://maven.apache.org/install.html
3. To build the project run maven in the same directory as pom.xml (mvn)

To run the main Java app:
#####java -jar out/populationfitness.jar -s [seed] -t [csv file containing tuning] -e [csv file containing epochs] 

The *epoch* files are CSV format and can be generated by running methods from *uk.edu.populationfitness.tuning.TuneFunctionsTest*. An example is *functionepochs-SumOfPowers-100.csv*

The *tuning* files are CSV format, hand generated. Examples of tuning files include *sumsquarestuning.csv*

### Tuning 
To find out the fitness factor range for a fitness function, use *uk.edu.populationfitness.tuning.DiscoverFunctionRangeTest*. 

The fitness range is part of the tuning provided in the tuning CSV.  



## Python Version

###This version is now deprecated and should not be used. 

Install Python 3.x +

run 'pip install bitarray'

To run the tests:
1. Install pytest with the command: 'pip install pytest'
2. Set the root python path to current directory: 'set PYTHONPATH=.'
3. Run tests from the folder containing pytest.ini, eg: 'pytest test\\test_genes.py'

To run the main Python app:
1. py main.py
2. To add profiling run 'py -m cProfile main.py'


