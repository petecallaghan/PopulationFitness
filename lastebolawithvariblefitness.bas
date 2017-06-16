'-----------------------------------------------------------------------
' non-stationary population Genetic Algorithm.

'David A. Coley

'email d.a.coley@bath.ac.uk


'Version = Feb 2016


'-----------------------------------------------------------------------

'The method is as follows:
'Start by creating a population of binary strings, for example 1000110111 (in the end our string will we thousands of digits). Each string is an individual person (and is also called a member of the population).
'Each string can be sub-divided into substrings, with the number of substrings being equal to the number of unknowns in the problem.
'In a problem with only 2 unknowns and the string given above, the substrings would be 10001 and 10111, because each substring is the same length.
'For this program we consider each human gene to be an unknown. So, if there are 25,000 genes in a person we will need 25,000 sub-strings (i.e. 25,000 unknowns).
'Having formed the substrings we need to decode these into real numbers. This requires knowing the range the numbers might take. For example in a normal GA
'we might be trying to find x1 and x2 such that sin(x1)*sin(x2) takes its maximum value when x1 and x2 can take values from 0 to pi. So in this case
'the range is 0 to pi. (In general, there is no need for the substring to be the same length, although the coding is more difficult it it isn't,
'but the range in general can be very different for each variable.)
'Having obtained the binary substrings we can easily turn these into decimal integers. In this program the normal relationship between binary and
'decimal integer is used, i.e. 00011 = 3. However, it is also common for genetic algorithms to use a grey coding for this step. Having got the
'integers these are mapped to reals over the range. In this program we use a simple liner mapping, so for the problem just mentioned 00000=0 and
'11111=pi=3.14159 with all values between being given by linear interpolation.
'Having got the reals, these are inserted into the fitness function. The fitness of an individual is given by the value of the function.

'Example if:
'string = 1111100000
'substrings = 11111 and 00000
'reals = 3.14159 and 0.0
'fitness of individual = sin(3.14159)*sin(0) = 0

'In a normal GA we would use the fitness to decide if an individual gets to breed. And then replace some of the population with these new individuals.
'And the total number of individuals (the population) would stay the same. Here we don't fix the population, nor do we decide who breeds. We simply decide who
'lives. This means rather than the main loop in the GA being over generations, it is a loop over years. If more babies are born than people die the
'population increases. We don't need to preferentially breed from the fittest individuals, because the fitter individuals live longer and hence have more
'babies. However we do need a function that decides who dies. We could use the roulette wheel selection often seen in a GA. But as we know the value of the
'peak of the fitness function we have other simpler possibilities. For example: IF fitness < 0.3 kill. Or IF fitness < RND kill.

'Program outline:

'declare subroutines
'define all global constants
'dimension all arrays - many of these will be redimensioned as the population grows
'open the output files
'create initial population of strings.
'decode the population into substrings, integers and reals
'find the fitness of each member of the population - there are a series of base fitness functions to choose from each with a slightly different
'version during each epoch, with note: these cannot be negative

'loop (over all years of the simulation)
'   kill off any individuals that fail the fitness test.
'   the array of individuals now has many missing slots where individuals have been killed - we need to shuffle the still alive individuals up to one end
'   we do this with a temporary set of arrays (NewString, NewFitness, etc.) to hold the individuals still alive
'   Now refill the original arrays that hold the population so all slots are used.
'   Loop (over the whole population choosing pairs to mate)
'       each pair produces one offspring via crossover (i.e. a baby uses some of the binary string from each parent
'   end (loop)
'   apply mutation to the babies
'   insert the babies into the population (using subroutine Replace)
'   decode the new population into substrings, integers and reals
'   find the fitness of the individuals
'   print various statistics about the population to the output files
'end (loop)


'Experimental outline
'create a wide range of fitness functions and prove our hypothesis is true for all of them
'create a wide range of changes in the fitness test and show our hypothesis is true for all of them
'For example if fitness = sin(x1)+sin(x2) we might after a time change this to fitness in such a way that only a high
'value is generated for very high values of x2. If sin(x2) was the ability to deal with dehydration due to impact of diarrhea
'caused by a virus, then this would kill all but the finest. Although intellectually easy to think about in this
'way, it is hard to think about how to change the fitness function in just the right way. For example, it must
'continue to generate numbers over the same range as before (for example, 0 to 1).
'The alternative approach would be to leave the fitness function static, but change the kill function.
'For example, this might change from
'   kill if fitness < RND
'to
'   kill if fitness < RND or x2 <0.99
'this would easily kill off those unable to deal with the virus.

'The program is currently split into MaxEpoch possible epochs. In each boundary the fitness function can be different (this could be changed to the kill function
'being different in each epoch. The bounadries are held in the epoch() array.

'



'------- DECLARE ALL THE SUBROUTINES (PROCEDURES) USED BY THE PROGRAM -------

DECLARE SUB OpenFiles ()
DECLARE SUB Scaling (ScalingConstant!, FittestIndividual!, SumFitness!, MeanFitness!)
DECLARE SUB Elite (SumFitness!, FittestIndividual!)
DECLARE SUB Selection (mate!, SumFitness!, MeanFitness!)
DECLARE SUB CrossOver (Mate1!, Mate2!, NewIndividual!)
DECLARE SUB FindFitness () 'this calls the particular fitness function required
declare sub Fitness1 (Individual) ' a particular fitness function from De Long's or other set
declare sub Fitness2 (Individual)
DECLARE SUB PrintGeneration (Generation, MeanFitness!, FittestIndividual!)
DECLARE SUB DefineRange ()
DECLARE SUB FindIntegers ()
DECLARE SUB FindUnknowns ()
DECLARE SUB InitialPopulation ()
DECLARE SUB NoCrossover (Mate1!, Mate2!, NewIndividual!)
DECLARE SUB Mutate ()
DECLARE SUB Replace ()
DECLARE SUB Statistics (MeanFitness!, SumFitness!, FittestIndividual!, Generation)
DECLARE SUB Kills (NKilled!)
declare sub printstring
declare sub History (HistoricPopulation)

'------- SET ALL THE IMPORTANT FIXED PARAMETERS. -------

'These should be set by the user.

CONST NumberOfUnknowns = 4 ' the fitness function itself is in the FindFitness subroutine and this will need to be changed if the number of unknowns changes.
' if the number of unknown is changed you will also need to alter the DefineRange subroutine to include the range of these all unknowns.
CONST SubstringLength = 10 'All sub-strings (genes) have the same length (i.e. number of bits).
CONST TotalStringLength = NumberOfUnknowns * SubstringLength
CONST MaxYear = 2150 'Number of years in the simulation. Run stops when this is reached. MaxYear is not a date. i.e. if MaxYear=1500 it means run the simulation for 1500
'years, not run it until the year 1500.
'CONST MaxFitness = 1.0 ' the maximum fitness an individual can have (i.e. the global maximum) - NOT SURE IF THIS IS USED NOW
CONST CrossOverProbability = .6 'Pc. >=0 and <=1. I don't think this is being used
CONST MutationProbability = 1 / TotalStringLength 'Pm, >=0 and <1.
CONST Elitism = "off" '"on" or "off". NOT SURE IF THIS CONCEPT MAKES SENSE NOW.
'CONST ScalingConstant = 1.2 'A value of 0 implies no scaling.
CONST MAXAGE = 50 'if an individual reaches this age it is killed.
CONST MAXBREEDAGE = 35 ' if an individual is older than this it cannot breed
CONST MINBREEDAGE = 16 ' if an individual is younger than this is cannot breed
'As individuals breed every year, MAXBREEDAGE-MINBREEDAGE gives the number of children a couple have. Both the mother and father have to be in this age range to breed
CONST MaxEpoch = 10 'the maximum number of epochs (this is the number of times in history the fitness function or kill function changes
CONST WhichFitnessFunction = 1 'defines which of De Long's or other fitness function is used
CONST KillConstant = 1.0255
'------DECLARE ALL SHARED (IE. GLOBAL) VARIABLES----------

'The arrays that hold the individuals within the current population.
DIM SHARED PopulationSize AS LONG 'Must be even
PopulationSize = 4000 'the initial population size
DIM SHARED Unknowns(PopulationSize, NumberOfUnknowns) AS SINGLE
DIM SHARED Integers(PopulationSize, NumberOfUnknowns) AS LONG
DIM SHARED Strings(PopulationSize, TotalStringLength) AS INTEGER
DIM SHARED Fitness(PopulationSize) AS SINGLE
DIM SHARED Age(PopulationSize) AS INTEGER

'other shared arrays and numbers
DIM SHARED epoch(MaxEpoch) AS INTEGER
epoch(1) = -50 ' The first epoch and the first year of the simulation
epoch(2) = 400
epoch(3) = 550
epoch(4) = 1086
epoch(5) = 1300
epoch(6) = 1348
epoch(7) = 1400
epoch(8) = 2016
epoch(9) = 2068


DIM SHARED year
DIM SHARED nkilled
DIM SHARED FitnessBins(10) ' ten bins to hold the number individual with a fitness or 0-0.1 (bin(1)); 0.1-0.2 (bin(2))....etc. Used to check how diverse the population is.
DIM SHARED AgeBins(MAXAGE + 1) '43 4e6
'550 2e6 partly due to romans leaving, party plague in 6th century
'1086 2e6
'1300 4e6
'1348 4e6
'1400 2.5e6
'1530 3e6
'1600 4e6
'1700 5.5e6 england and wales; 1million additional in scotland
'1750 6.5e6
'1801 9e6
'1901 41e6 (15e6 migrated away between 1815 and 1914)
'2001 60e6
'2015 65e6




'The new population of babies.
DIM SHARED BabyStrings(PopulationSize, TotalStringLength) AS INTEGER
DIM SHARED NBabies AS LONG 'the number of babies

'The array that defines the range of the unknowns.
DIM SHARED Range(2, NumberOfUnknowns) AS SINGLE

CLS 'Clear the screen.

CALL DefineRange 'Define the range of each unknown. These should also be set by the user.

'Set the random number generator so it produces a different set of numbers
'each time the program is run.
RANDOMIZE TIMER

CALL OpenFiles 'Open files used to store results.

'------- START OF THE GENETIC ALGORITHM -------

'------- CREATE AN INITIAL POPULATION

'year = 1

CALL InitialPopulation 'Build a population of strings at random.

CALL FindFitness 'Find the fitness of each member of the population.

CALL Statistics(MeanFitness, SumFitness, FittestIndividual, Generation) 'Find the mean fitness and the fittest individual.

CALL PrintGeneration(year, MeanFitness, FittestIndividual) 'Print generation to file.

'CALL Scaling(ScalingConstant, FittestIndividual, SumFitness, MeanFitness)   'If linear fitness scaling is "on" then scale population prior to selection.

'------- LOOP OVER ALL THE YEARS IN THE SIMULATION -------

FOR year = epoch(1) TO epoch(1) + MaxYear
    IF PopulationSize > 800000 THEN ' stops the simulation if population too big to make sense
        CLOSE
        STOP
    END IF
    'PRINT #1, "year", year
    CALL Kills(nkilled) 'kill off individuals with a probability inversely proportional to their fitness

    'extract individuals who didnt die and hold them in a temporary population NEED TO CHECK IF REDIM ALSO ERASES THE OLD NUMBERS
    REDIM SHARED NewUnknowns(PopulationSize - nkilled, NumberOfUnknowns) AS SINGLE
    REDIM SHARED NewIntegers(PopulationSize - nkilled, NumberOfUnknowns) AS LONG
    REDIM SHARED NewStrings(PopulationSize - nkilled, TotalStringLength) AS INTEGER
    REDIM SHARED NewFitness(PopulationSize - nkilled) AS SINGLE
    REDIM SHARED NewAge(PopulationSize - nkilled) AS INTEGER

    i = 1 'i is the index of the new population individuals
    FOR Individual = 1 TO PopulationSize

        IF Fitness(Individual) > 0 THEN
            'PRINT "beep"

            FOR j = 1 TO NumberOfUnknows
                NewUnknows(i, j) = Unknowns(Individual, j)
                NewIntegers(i, j) = Integers(Individual, j)
            NEXT j
            FOR j = 1 TO TotalStringLength
                NewStrings(i, j) = Strings(Individual, j)
                'PRINT #2, i, Individual, NewStrings(i, j), Strings(Individual, j)
            NEXT j
            NewFitness(i) = Fitness(Individual)
            NewAge(i) = Age(Individual) + 1
            i = i + 1
        END IF
    NEXT Individual

    'now make replace old pop with new population
    PopulationSize = PopulationSize - nkilled 'PROBLEM IF ALL INDIVIDUALS KILLED
    IF PopulationSize < 2 THEN
        CLOSE
        STOP
    END IF

    REDIM SHARED Unknowns(PopulationSize + PopulationSize / 2, NumberOfUnknowns) AS SINGLE 'we redim these to be big enough to hold the current population and the babies
    REDIM SHARED Integers(PopulationSize + PopulationSize / 2, NumberOfUnknowns) AS LONG
    REDIM SHARED Strings(PopulationSize + PopulationSize / 2, TotalStringLength) AS INTEGER
    REDIM SHARED Fitness(PopulationSize + PopulationSize / 2) AS SINGLE
    REDIM SHARED Age(PopulationSize + PopulationSize / 2) AS INTEGER

    FOR Individual = 1 TO PopulationSize
        FOR j = 1 TO NumberOfUnknows
            Unknowns(Individual, j) = NewUnknowns(Individual, j)
            Integers(Individual, j) = NewIntegers(Individual, j)
        NEXT j
        FOR j = 1 TO TotalStringLength
            Strings(Individual, j) = NewStrings(Individual, j)
        NEXT j
        Fitness(Individual) = NewFitness(Individual)
        Age(Individual) = NewAge(Individual)
    NEXT Individual
 
    'Population now breeds. Everyone breeds every year - I need at at some point to set a minimum breeding age
    REDIM SHARED BabyStrings(PopulationSize, TotalStringLength) AS INTEGER 'The maximum number of offspring is half the population size to this array is plenty big enough
    NBabies = 0

    FOR Individual = 1 TO PopulationSize - 1 STEP 2 'Loop over the population choosing pairs of mates - it population size is odd the last individual doesn’t mate.
        'adjacent (not random) pairs are picked - this means people tend to mate with people roughly their age and not their children.

        'Make babies with crossover.
        IF (Age(Individual) >= MINBREEDAGE) AND (Age(Individual) <= MAXBREEDAGE) THEN
            IF (Age(Individual + 1) >= MINBREEDAGE) AND (Age(Individual + 1) <= MAXBREEDAGE) THEN
                IF RND > 0.6 THEN 'i.e. 25% chance you give birth in any year
                    NBabies = NBabies + 1
                    CALL CrossOver(Individual, Individual + 1)
                END IF
            END IF
        END IF

    NEXT Individual

    CALL Mutate 'Mutate the babies.

    CALL Replace 'add the babies onto the new population

    PopulationSize = PopulationSize + NBabies

    CALL FindUnknowns 'De-code the new population to integers then real numbers.

    CALL FindFitness 'Find the fitness of each member of the population.
    'PRINT "f"
    'CALL printstring

    CALL Statistics(MeanFitness, SumFitness, FittestIndividual, Generation) 'Find the mean fitness and the fittest individual.

    CALL PrintGeneration(year, MeanFitness, FittestIndividual) 'Print generation to file.

    'CALL Scaling(ScalingConstant, FittestIndividual, SumFitness, MeanFitness) 'If linear fitness scaling is "on" then scale population prior to selection.

NEXT year 'Process the next year.

CLOSE 'Close all files

'------------------------------------------- END OF MAIN PROGRAM -------------------------------------------




SUB CrossOver (Mother, Father)
'Perform single point crossover.

CrossSite = INT((TotalStringLength - 1) * RND + 1) 'Pick the cross-site at random.

FOR bit = 1 TO CrossSite 'use the bits to the left of the cross-site from the mother.
    BabyStrings(NBabies, bit) = Strings(Mother, bit)
NEXT bit

FOR bit = CrossSite + 1 TO TotalStringLength 'use the bits to the right of the cross-site from the father.
    BabyStrings(NBabies, bit) = Strings(Father, bit)
NEXT bit

END SUB





SUB DefineRange
'Defines the upper and lower bounds of each unknown.
'The assumption is that all unknowns have the same range.
SELECT CASE WhichFitnessFunction
    CASE 1
        FOR Unknown = 1 TO NumberOfUnknowns
            Range(1, Unknown) = 0 'The lower bound.
            Range(2, Unknown) = 3.14159 'The upper bound.
        NEXT Unknown
    CASE 2
        FOR Unknown = 1 TO NumberOfUnknowns
            Range(1, Unknown) = 0 'The lower bound.
            Range(2, Unknown) = 3.14159 'The upper bound.
        NEXT Unknown
        'add further cases here
    CASE ELSE
        CLOSE
        STOP
END SELECT

END SUB





SUB Elite (SumFitness, FittestIndividual)
'Applies elitism by replacing a randomly chosen individual by the elite member
'from the previous population if the new max fitness is less then the previous value.

IF Fitness(FittestIndividual) < EliteFitness THEN

    Individual = INT(PopulationSize * RND + 1) 'Chosen individual to be replaced.

    FOR bit = 1 TO TotalStringLength
        Strings(Individual, bit) = EliteString(bit)
    NEXT bit

    Fitness(Individual) = EliteFitness

    FOR Unknown = 1 TO NumberOfUnknowns
        Integers(Individual, Unknown) = EliteIntegers(Unknown)
        Unknowns(Individual, Unknown) = EliteUnknowns(Unknown)
    NEXT Unknown

    FittestIndividual = Individual

END IF

FOR bit = 1 TO TotalStringLength
    EliteString(bit) = Strings(FittestIndividual, bit)
NEXT bit

EliteFitness = Fitness(FittestIndividual)
                      
FOR Unknown = 1 TO NumberOfUnknowns
    EliteIntegers(Unknown) = Integers(FittestIndividual, Unknown)
    EliteUnknowns(Unknown) = Unknowns(FittestIndividual, Unknown)
NEXT Unknown

END SUB




SUB FindFitness
'The problem at hand is used to assign a positive (or zero) fitness to each individual in turn.
'The fitness function or the kill function will vary with time once fully implemented
'If the FindFitness subroutine or the number of unknowns is altered, you will need to also alter the Range subroutine if the range of the
'unknowns is different than currently.
FOR i = 1 TO 10
    FitnessBins(i) = 0
NEXT i

FOR Individual = 1 TO PopulationSize
    Fitness(Individual) = 0
    'PRINT #2, "fitness", Fitness(Individual)
    SELECT CASE WhichFitnessFunction
        CASE 1
            CALL Fitness1(Individual)
            'PRINT #1, "fitness", Fitness(Individual)
        CASE 2
            CALL Fitness2(Individual)
        CASE ELSE
            CLOSE
            STOP
    END SELECT
    IF Fitness(Individual) < 0 THEN Fitness(Individual) = 0
    'FitnessBins(CINT(10 * Fitness(Individual))) = FitnessBins(CINT(10 * Fitness(Individual))) + 1
NEXT Individual



END SUB

SUB Fitness1 (Individual)
'The problem at hand is used to assign a positive (or zero) fitness to each individual in turn.
'The fitness function or the kill function will vary with time once fully implemented
'If the FindFitness subroutine or the number of unknowns is altered, you will need to also alter the Range subroutine if the range of the
'unknowns is different than currently.
'PRINT #1, "fitness1"
'The problem is f = sin(x1) * sin(x2) *.....*sin(xn)



SELECT CASE year
    CASE IS < epoch(2) 'steady at 4milllion due to the holding capacity of the environment
        Fitness(Individual) = SIN(Unknowns(Individual, 1))
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) 'a simple hump with a height of 1.
        NEXT
        Fitness(Individual) = Fitness(Individual) * 4000 / PopulationSize 'scale the fitness based on the ratio between the current number of individuals and the natural holding capacity of the environment
    CASE IS < epoch(3) 'romans leave then 6th century plague
        Fitness(Individual) = SIN(Unknowns(Individual, 1)) ^ 1.5
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) ^ 1.5 'a simple hump with a height of 1.
        NEXT
    CASE IS < epoch(4) 'switch to a holding capacity of 2million
        Fitness(Individual) = SIN(Unknowns(Individual, 1))
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) 'a simple hump with a height of 1.
        NEXT
        Fitness(Individual) = Fitness(Individual) * 2000 / PopulationSize 'scale the fitness based on the ratio between the current number of individuals and the natural holding capacity of the environment

    CASE IS < epoch(5)
        Fitness(Individual) = SIN(Unknowns(Individual, 1)) ^ 1.1
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) ^ 1.1 'a simple hump with a height of 1.
        NEXT
        'Fitness(Individual) = Fitness(Individual) * 4000 / PopulationSize 'scale the fitness based on the ratio between the current number of individuals and the natural holding capacity of the environment
    CASE IS < epoch(6) 'return to the holding capacity of 4million
        Fitness(Individual) = SIN(Unknowns(Individual, 1)) ^ 1.1
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) ^ 1.1 'a simple hump with a height of 1.
        NEXT
        'Fitness(Individual) = Fitness(Individual) * 4000 / PopulationSize 'scale the fitness based on the ratio between the current number of individuals and the natural holding capacity of the environment

    CASE IS < epoch(7) '12th century plague
        Fitness(Individual) = SIN(Unknowns(Individual, 1)) ^ 3
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) ^ 3 'a simple hump with a height of 1.
        NEXT
    CASE IS < epoch(8)
        Fitness(Individual) = SIN(Unknowns(Individual, 1))
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) 'a simple hump with a height of 1.
        NEXT
        'Fitness(Individual) = Fitness(Individual) * 4000 / PopulationSize 'scale the fitness based on the ratio between the current number of individuals and the natural holding capacity of the environment
    CASE IS < epoch(9) '21st century plague
        Fitness(Individual) = SIN(Unknowns(Individual, 1)) ^ 3
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * SIN(Unknowns(Individual, unknown)) ^ 3 'a simple hump with a height of 1.
        NEXT

    CASE ELSE 'modern day expoential growth of population
        Fitness(Individual) = (SIN(Unknowns(Individual, 1)))
        FOR unknown = 2 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) * (SIN(Unknowns(Individual, unknown)))
            'a simple hump with a height of 1.
        NEXT
END SELECT
'PRINT #1, "<2", (Individual)

END SUB

SUB Fitness2 (Individual)
'The problem at hand is used to assign a positive (or zero) fitness to each individual in turn.
'The fitness function or the kill function will vary with time once fully implemented
'If the FindFitness subroutine or the number of unknowns is altered, you will need to also alter the Range subroutine if the range of the
'unknowns is different than currently.

'The problem is f = sin(x1) + sin(x2) + ......+ sin(xn)

SELECT CASE year
    CASE IS < epoch(2)
        FOR unknown = 1 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) + SIN(Unknowns(Individual, unknown)) / NumberOfUnknowns 'a simple hump with a height of 1.
        NEXT
    CASE ELSE
        FOR unknown = 1 TO NumberOfUnknowns
            Fitness(Individual) = Fitness(Individual) + SIN(Unknowns(Individual, unknown)) / NumberofUnkowns 'a simple hump with a height of 1.
        NEXT
END SELECT


END SUB



SUB FindIntegers
'Decode the strings to sets of decimal integers.

DIM bit AS INTEGER

FOR Individual = 1 TO PopulationSize

    bit = TotalStringLength + 1
    FOR Unknown = NumberOfUnknowns TO 1 STEP -1
        Integers(Individual, Unknown) = 0
        FOR StringBit = 1 TO SubstringLength

            bit = bit - 1
            IF Strings(Individual, bit) = 1 THEN
                Integers(Individual, Unknown) = Integers(Individual, Unknown) + 2 ^ (StringBit - 1)
            END IF

        NEXT StringBit

    NEXT Unknown

NEXT Individual
    
END SUB





SUB FindUnknowns
'Decode the strings to real numbers.

CALL FindIntegers 'First decode the strings to sets of decimal integers.

'Now convert these integers to reals.
FOR Individual = 1 TO PopulationSize
    FOR Unknown = 1 TO NumberOfUnknowns
        Unknowns(Individual, Unknown) = Range(1, Unknown) + Integers(Individual, Unknown) * (Range(2, Unknown) - Range(1, Unknown)) / (2 ^ SubstringLength - 1)
    NEXT Unknown
NEXT Individual
   
END SUB

SUB History (HistoricPopulation)
'Data on UK population. This is just grabbed from the web and better data will be required
'This data is not used by the population, but is used to provide numbers for comparison to the results the program produces.
'43 4e6
'550 2e6 partly due to romans leaving, party plague in 6th century
'1086 2e6
'1300 4e6
'1348 4e6
'1400 2.5e6
'1530 3e6
'1600 4e6
'1700 5.5e6 england and wales; 1million additional in scotland
'1750 6.5e6
'1801 9e6
'1901 41e6 (15e6 migrated away between 1815 and 1914)
'2001 60e6
'2015 65e6

'to get the following we are assuming one year of simulation time is ten year of real time
year10 = year
IF year10 <= 400 THEN HistoricPopulation = 4E6
IF year10 > 400 THEN HistoricPopulation = 4E6 + (year10 - 400) * (2E6 - 4E6) / (550 - 400)
IF year10 > 550 THEN HistoricPopulation = 2E6
IF year10 > 1086 THEN HistoricPopulation = 2E6 + (year10 - 1086) * (4E6 - 2E6) / (1300 - 1086)
IF year10 > 1300 THEN HistoricPopulation = 4E6 + (year10 - 1300) * (4E6 - 4E6) / (1348 - 1300)
IF year10 > 1348 THEN HistoricPopulation = 4E6 + (year10 - 1348) * (2.5E6 - 4E6) / (1400 - 1348)
IF year10 > 1400 THEN HistoricPopulation = 2.5E6 + (year10 - 1400) * (3E6 - 2.5E6) / (1530 - 1400)
IF year10 > 1530 THEN HistoricPopulation = 3E6 + (year10 - 1530) * (4E6 - 3E6) / (1600 - 1530)
IF year10 > 1600 THEN HistoricPopulation = 4E6 + (year10 - 1600) * (5.5E6 - 4E6) / (1700 - 1600)
IF year10 > 1700 THEN HistoricPopulation = 5.5E6 + (year10 - 1700) * (6.5E6 - 5.5E6) / (1750 - 1700)
IF year10 > 1750 THEN HistoricPopulation = 6.5E6 + (year10 - 1750) * (9E6 - 6.5E6) / (1801 - 1750)
IF year10 > 1801 THEN HistoricPopulation = 9E6 + (year10 - 1801) * (41E6 - 9E6) / (1901 - 1801)
IF year10 > 1901 THEN HistoricPopulation = 41E6 + (year10 - 1901) * (60E6 - 41E6) / (2001 - 1901)
IF year10 > 2001 THEN HistoricPopulation = 60E6 + (year10 - 2001) * (65E6 - 60E6) / (2015 - 2001)
HistoricPopulation = HistoricPopulation / 1000 'assumes we are running the simulation where 1 person in the simulation equals 1000 in the real world

END SUB


SUB InitialPopulation
'Create the initial random population.

FOR Individual = 1 TO PopulationSize
    FOR bit = 1 TO TotalStringLength
        IF RND > .5 THEN
            Strings(Individual, bit) = 1
        ELSE
            Strings(Individual, bit) = 0
        END IF
    NEXT bit

    Age(Individual) = MINBREEDAGE

NEXT Individual

CALL FindUnknowns 'Decode strings to real numbers.

END SUB


SUB Kills (NKilled)
'This is where selection to live or die takes place based on the fitness of the individual.
' at the moment selection is determined by throwing a random number. If they are fitter than this, they get to live.
'They are also get die if they are older than the maximum allowable age.

NKilled = 0
FOR Individual = 1 TO PopulationSize
    IF year < epoch(2) THEN
        IF Fitness(Individual) < RND OR Age(Individual) > MAXAGE THEN
            Fitness(Individual) = 0
            NKilled = NKilled + 1
        END IF

    ELSE

        IF Fitness(Individual) < (KillConstant * RND) OR Age(Individual) > MAXAGE THEN
            Fitness(Individual) = 0
            NKilled = NKilled + 1
        END IF
    END IF
NEXT Individual
        
END SUB




SUB Mutate
'Visit each bit of each string very occasionally flipping a "1" to a "0" or vice versa.

FOR Individual = 1 TO NBabies
    FOR bit = 1 TO TotalStringLength

        'Throw a random number and see if it is less than or equal to the mutation probability.
        IF RND <= MutationProbability THEN

            'Mutate.
            IF BabyStrings(Individual, bit) = 1 THEN
                BabyStrings(Individual, bit) = 0
            ELSE
                BabyStrings(Individual, bit) = 1
            END IF

        END IF

    NEXT bit

NEXT Individual
             
END SUB




SUB OpenFiles
'Open result files. See Chapter 2 for a description of their contents.

OPEN "ebola_mean.csv" FOR OUTPUT AS #1
OPEN "ebola_more.csv" FOR OUTPUT AS #2

END SUB




SUB PrintGeneration (Year, MeanFitness, FittestIndividual)
'Print results to the screen and the files.

'Calculate the true histroic population
CALL History(HistoricPopulation)

'find the mean age of the population
meanage = 0
FOR i = 1 TO MAXAGE + 1
    AgeBins(i) = 0
NEXT i

FOR i = 1 TO PopulationSize
    meanage = meanage + Age(i)
    AgeBins(Age(i)) = AgeBins(Age(i)) + 1
NEXT i
meanage = meanage / PopulationSize

PRINT #2, 'Carriage return.
FOR i = 1 TO MAXAGE + 1
    PRINT #2, AgeBins(i); ",";
NEXT i

IF Year / 10 = CINT(Year / 10) THEN PRINT Year; PopulationSize; HistoricPopulation; meanage; Fitness(FittestIndividual); MeanFitness; 'Screen.
PRINT #1, Year; ","; PopulationSize; ","; HistoricPopulation; ","; NBabies; ","; nkilled; ","; FittestIndividual; ","; Fitness(FittestIndividual); ","; MeanFitness; ","; 'File LGADOS.RES.

'FOR Unknown = 1 TO NumberOfUnknowns
'PRINT Unknowns(FittestIndividual, Unknown); 'Screen.
'PRINT #1, ","; Unknowns(FittestIndividual, Unknown); ","; 'File LGADOS.RES
'NEXT Unknown

FOR i = 1 TO 10
    PRINT #1, FitnessBins(i); ",";
NEXT i
IF Year / 10 = CINT(Year / 10) THEN PRINT 'Carriage return.
PRINT #1, 'Carriage return.

GOTO dontprintgen 'comment out if you want to print the whole generation
FOR Individual = 1 TO 1 'PopulationSize

    PRINT #2, Year; ","; Fitness(Individual); ","; 'File LGADOS.ALL

    FOR Unknown = 1 TO NumberOfUnknowns
        PRINT #2, Unknowns(Individual, Unknown); ","; 'File LGADOS.ALL
    NEXT Unknown

    FOR bit = 1 TO TotalStringLength
        PRINT #2, RIGHT$(STR$(Strings(Individual, bit)), 1); ","; 'File LGADOS.ALL
    NEXT bit

    PRINT #2, 'Carriage return

NEXT Individual
dontprintgen:

END SUB




SUB printstring

FOR bit = 1 TO TotalStringLength
    'PRINT RIGHT$(STR$(Strings(3, bit)), 1); ","; 'File LGADOS.ALL
NEXT bit

END SUB


SUB Replace
'Add the babies onto the end of the population.

i = 0 'indivdual in baby string
FOR Individual = PopulationSize + 1 TO PopulationSize + NBabies
    i = i + 1
    FOR bit = 1 TO TotalStringLength
        Strings(Individual, bit) = BabyStrings(i, bit)
    NEXT bit

    Age(indiviual) = 0

NEXT Individual

END SUB





SUB Scaling (ScalingConstant, FittestIndividual, SumFitness, MeanFitness)

'Apply Linear Fitness Scaling,
'      scaledfitness = a * fitness + b.
'Subject to,
'      meanscaledfitness = meanfitness
'and
'      bestscaledfitness = c * meanfitness,
'where c, the scaling constant, is set by the user.

'If the scaling constant is set to zero, or all individuals have the same
'fitness, scaling is not applied.
IF ScalingConstant <> 0 AND Fitness(FittestIndividual) - MeanFitness > 0 THEN
    'Find a and b.

    A = (ScalingConstant - 1) * MeanFitness / (Fitness(FittestIndividual) - MeanFitness)

    b = (1 - A) * MeanFitness

    'Adjust the fitness of all members of the population.
    SumFitness = 0
    FOR Individual = 1 TO PopulationSize
        Fitness(Individual) = A * Fitness(Individual) + b
        IF Fitness(Individual) < 0 THEN Fitness(Individual) = 0 'Avoid negative values near the end of a run.
        SumFitness = SumFitness + Fitness(Individual) 'Adjust the sum of all the fitnesses.
    NEXT Individual

    'Adjust the mean of all the fitnesses.
    MeanFitness = SumFitness / PopulationSize
END IF

END SUB




SUB Statistics (MeanFitness, SumFitness, FittestIndividual, Generation)
'Calculate the sum of fitness across the population and find the best individual,
'then apply elitism if required.

FittestIndividual = 0
MaxFitness = 0

FOR Individual = 1 TO PopulationSize
    IF Fitness(Individual) > MaxFitness THEN
        MaxFitness = Fitness(Individual)
        FittestIndividual = Individual
    END IF
NEXT Individual

'IF Elitism = "on" THEN 'Apply elitism.
'    CALL Elite(SumFitness, FittestIndividual)
'END IF

SumFitness = 0 'Sum the fitness.
FOR Individual = 1 TO PopulationSize
    SumFitness = SumFitness + Fitness(Individual)
NEXT Individual

'Find the average fitness of the population.
MeanFitness = SumFitness / PopulationSize

END SUB

