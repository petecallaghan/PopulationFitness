'-----------------------------------------------------------------------
'Non-stationary population Genetic Algorithm.

'David A. Coley

'email d.a.coley@bath.ac.uk


'Version = April 2020


'-----------------------------------------------------------------------
'PURPOSE
'This program was written to study if by using a GA, scaled to size of the human genone, with a population equal to the UK's, then let loose
'on a variety of standard fitness functions one might be able to say something interesting if the population was exposed to a change in the
'fitness function such that the drop in the population witnessed druring the black death was resultant, then in 2020 a shock of exactly the
'same size was applied. The reason we think that the two shocks might lead to different reductions in population is that since around 1700 the
'UK's population has grown, but the birthrate fallen. This growth this therefore due to a reduction in the morality rate, particularly amongst children.
'One could therefore argue that natural selection pressures have reduced since around 1700; the question is whether over such a short time this might make a
'material difference.

'It is impossible to study the true genome for this purpose, as the mapping between true germone and fitness in the real world is unknown, hence we argue that
'the best approaximation we can make is to use a number of bits in a GA that is represenative of the useful bits in the human genome, roughly the same number
'unknowns as the human genome and a similar mutation rate, poulation size and bith rate. Then use a wide variety of fitness fucntions from the GA test function
'literature, and see if a similar answer is true for all test fucntions. If so, one might therefore conclude that is might also be true of the much more complex
'fitness fucntions of the biological world.

'If it turns out that the crash in the population is greater in 2020 than during the black death we can conclude that such articfical genomes of this scale do
'materially decay over such a period in populations with a lessening of selection pressure, for these test fuctions. This could be worrying for modern humans,
'but might also point to a greater resitance in populations that have had a shorter time exposed to less selection, for example places were infectious deasese
'etc. still play a role in mortality before old age. So, given a similar level of health care etc. access to food and other services that might get a population
'through a crisis, one might expect the UK to do less well than some developing world locations when it comes to Covid-19.

'The model assumes a return in 2020 to a dark age fitness function. i.e. no modern healthcare or the ability to make use of our modern knowldge of the human body
'of desease transmission etc. It is therefore a worse case senario during a period when health systems are completely overwellomed or when desease occurs in parts
'of the world where such sytems do not exist.


'METHOD

'The method is as follows:
'Start by creating a population of binary strings, for example one string might be 1000110111 (in the end our strings will we thousands of digits). Each string is an
'individual person (and is also called a member of the population).
'Each string can be sub-divided into substrings (which are analogous to genes), with the number of substrings being equal to the number of unknowns in the problem.
'In a problem with only 2 unknowns and the string given above, the substrings would be 10001 and 10111, because each substring is the same length.
'For this program we consider each human gene to be an unknown. So, if there are 25,000 genes in a person we will need 25,000 sub-strings (i.e. 25,000 unknowns).
'Having formed the substrings we need to decode these into real numbers. This requires knowing the range the numbers might take. For example in a normal GA
'we might be trying to find x1 and x2 such that sin(x1) * sin(x2) takes its maximum value (under the assumption that x1 and x2 can take values from 0 to pi).
'So in this case the range is 0 to pi. (In general, there is no need for the substrings to all be the same length, although the coding is more difficult it it isn't,
'but the range in general can be very different for each variable.)
'Having obtained the binary substrings we can easily turn these into decimal integers. In this program the normal relationship between binary and
'decimal integer is used, i.e. 00011 = 3. However, it is also common for genetic algorithms to use a grey coding for this step. Having got the
'integers these are mapped to reals over the range. In this program we use a simple liner mapping, so for the problem just mentioned 00000=0 and
'11111=pi=3.14159 with all values between being given by linear interpolation.
'Having got the reals, these are inserted into the fitness function (i.e. the problem). The fitness of an individual is given by the value of the function.

'Example, if:
'string = 1111100000
'therefore substrings = 11111 and 00000
'therefore reals = 3.14159 and 0.0
'therefore fitness of individual = sin(3.14159) * sin(0) = 0

'In a normal GA we would use the fitness to decide if an individual gets to breed. And then replace some of the population with these new individuals.
'And the total number of individuals (the population) would stay the same. Here we don't fix the population size, nor do we decide who breeds. We simply decide who
'lives. This means rather than the main loop in the GA being over generations, it is a loop over years. If more babies are born than people die the
'population increases. We don't need to preferentially breed from the fittest individuals, because the fitter individuals live longer and hence have more
'babies. However we do need a function that decides who dies.

'In this version of the program this decision is made in the KILLS subroutine, as follows.
'The program starts in the year -50 (i.e. 51BC, not 50BC as there is a year 0 between 1BC and 1AD; meaning year 10 for example is 10AD)
'The population in any year, except the two plague periods, is decided directly by the historic record, although not exactly, as the program is stocastic in who dies
'and who gives birth. For years where the poulation is not found in the literature, linear interpolation has been used. The birth rate is reported in the
'litertaure at fewer temporal points than the population, with only seven values to span 2000+ years.
'Pior to the black death individuals are picked to die from the set of those with a fitness of less than half the mean fitness. And only enough are killed to
'ensure the population is still about the histoically observed one.
'During the black death ALL those with a fitness less than a set value are killed. This set value (MinBlackDeathFitness) is found by trial and error, and is
'discussed below in the next section. Note, during the black death there is no min or max number of individuals chosen to die.
'After the black death, the previous selection method is used until 1700.
'From 1700 it is assumed that the link between fitness and living is weak and individuals are killed at random.
'At year 2020 a second black death is applied by using the same heuristic as for the first black death. And importantly the same value of MinBlackDeathFitness.
'After 2020 we return to the post-1700 heuristic.

'The exact rules for killing indivuduals can be found in the KILLS subroutine, these might need to be subtly changed for other fitness functions. Please take a
'look.

'In addition to the  above rules, when an indidual reaches MAXAGE it is also killed regardless of its fitness.

'FINDING MinBlackDeathFitness
'I have been using trial and error. The black death killed around 35% of the population. Run the program until just after the black death (i.e. just past 1350) and
'look in the file fitness.csv. This contains a list of the fitness of all individuals. Sort these in excel to find the 35th percentile. This fitness will be
'MinBlackDeathFitness. Insert this value into the program (where the varibles are declared) and re-run the program.
'From my inital tests it would seem the value of MinBlackDeathFitness is somewhat varible. Hence there might be the need in the very final runs to run the
'program several times to find MinBlachDeathFitness, then take the mean. Then run the whole simulation several times and take the mean of any time series produced.
'The value of MinBlackDeathFitness will depend on the fitness function used. I would guess the value might also depend on the number of bits per unknown and the
'number of unknowns.


'FITNESS FUNCTIONS ETC.
'ALL FITNESS FUCNTIONS must have a max value of 1. I have not tested the code with a fintess function that gives negative numbers in part of its landscape.
'WhichFitnessFunction decides which of how ever many fitness lanscapes you have coded is used.
'The subroutine DefineRange is used to set the range of each unknown (for example 0 to 3.14159) for each fitness function.
'At the moment there are 2 fitness functions, to add more, code another one, call it fitness3, set WhichFitnessFunction to 3 (assuming you want to use
'this new finction), and put the range of each unknown in DefineRange.

'THE HISTORIC POPULATION
'The time series of the histortic population is read in by the HISTORY subroutine from the file pop.csv. The units in this file are millions. Hence the values
'read need to be multiplied by 1,000,000. This is also done inside HISTORY. However to speed the simulation up, and because I have been using my little notebook
'computer, I have only been multiplying by 1000. For most tests this might be fine.


'PROGRAM OUTLINE:

'declare subroutines
'define all global constants
'dimension all arrays - many of these will be redimensioned as the population grows
'open the output files
'create initial population of strings.
'decode the iniital population into substrings, integers and reals
'find the fitness of each member of the initial population
'(Note. As this population is random, the fitness will be low and will take many subsequent generations to become fit, hence
'the fitness might be climbing thoughtout much of the simulation. With 4 unknowns, 10 bits per unknown and the sum of sines function it takes a
'few hundred years. With other functions, much higher numbers of bits and unknowns I have no idea. So we might well need to start the program with a non-random
'population.)

'loop (over all years of the simulation)
'   kill off any individuals that fail the fitness test.
'   the array of individuals now has many missing slots where individuals have been killed - we need to shuffle the still alive individuals up to one end
'   we do this with a temporary set of arrays (NewString, NewFitness, etc.) to hold the individuals still alive
'   Now refill the original arrays that hold the population so all slots are used.
'   Loop (over the whole population choosing pairs to mate)
'       each pair produces one offspring via crossover (i.e. a baby uses some of the binary string from each parent
'   end (loop)
'   (Note. the fraction of the population that mate depends on the historic reprodcution rate.)
'   apply mutation to the babies
'   insert the babies into the population (using subroutine Replace)
'   decode the new population into substrings, integers and reals
'   find the fitness of the individuals
'   print various statistics about the population to the output files
'end (loop)


'EXPERIMENTAL OUTLINE
'Create a wide range of fitness functions and discover if our hypothesis is true or false for all of them.
'This means two runs per fitness function, one where the population in the modern (post 1700) is exposed to selection pressure, and one where it is not. Then seeing
'if there is any difference in the death rate during the second black death in 2020 between the two runs. The existance, or not, of this second black death is
'controlled by SECONDBLACKDEATH.

'RUNNING THE PROGRAM
'Scroll down to the area titled "'------- SET ALL THE IMPORTANT FIXED PARAMETERS. ------- " and change the values of anything that needs changing, the compile and run



'------- DECLARE ALL THE SUBROUTINES (PROCEDURES) USED BY THE PROGRAM -------

DECLARE SUB OpenFiles () 'opens the output files, but also the input file containing the historic record of population (and when get get it, the reproduction rate)
DECLARE SUB CrossOver (Mate1!, Mate2!, NewIndividual!)  'mates two strings
DECLARE SUB FindFitness () 'this calls the particular fitness function required
declare sub Fitness1 (Individual) ' a particular fitness function from De Long's or other set
declare sub Fitness2 (Individual) 'ditto
DECLARE SUB PrintGeneration (FittestIndividual!) 'print the results
DECLARE SUB DefineRange ()  'set the range of each unknown in each fitness function.
DECLARE SUB FindIntegers ()   'decode the strings to integers using the normal binary system
DECLARE SUB FindUnknowns ()   'decode the integers to reals over the range of each unknown
DECLARE SUB InitialPopulation ()  'create the initial, random, population
DECLARE SUB Mutate () 'apply the mutation rate found in the literature to any babies
DECLARE SUB Replace ()   'replace missing slots in the population with babies
DECLARE SUB Statistics (FittestIndividual!) 'find the mean of the population fitness and other stats
DECLARE SUB Kills   'kill those that fail the fitness test
declare sub History 'read in the time series of historic population (and when we have it, the historic reprodcution rate)



'------- SET ALL THE IMPORTANT FIXED PARAMETERS. -------

'These should be set by the user.

CONST NumberOfUnknowns = 8 ' the fitness function itself is in the FindFitness subroutine and this will need to be changed if the number of unknowns changes.
' if the number of unknown is changed you will also need to alter the DefineRange subroutine to include the range of these all unknowns.
CONST SubstringLength = 10 'All sub-strings (genes) have the same length (i.e. number of bits).
CONST TotalStringLength = NumberOfUnknowns * SubstringLength
CONST MaxYear = 2150 'Number of years in the simulation. Run stops when this is reached. MaxYear is not a date. i.e. if MaxYear=1500 it means run the simulation
CONST STARTYEAR = -50
'for 1500 years, not run it until the year 1500.
CONST MutationProbability = 1 / TotalStringLength '>=0 and <1. Needs to be set by the suggested value in the literature
CONST MAXAGE = 85 'if an individual reaches this age it is killed.
CONST MAXBREEDAGE = 400 '35 ' if an individual is older than this it cannot breed. CURRENTLY OVER WRITTEN OTHERIWSE NOT ENOGH BABIES DUE TO THE WALL MATES ARE PICKED
CONST MINBREEDAGE = -16 ' 16 ' if an individual is younger than this is cannot breed        DITT0
'As individuals breed every year, MAXBREEDAGE-MINBREEDAGE gives the number of children a couple have. Both the mother and father have to be in this age range to breed
CONST WhichFitnessFunction = 2 'defines which of De Long's or other fitness function is used
CONST SECONDBLACKDEATH = 1 '1=yes, have a second black death in 2020; 0=no, don't have a second black death
CONST MaxPopulationSize = 80000000 'if population bigger than this we have become unrealistic and the program will terminate to save processing time

'------DECLARE ALL SHARED (IE. GLOBAL) VARIABLES----------

'The arrays that hold the individuals within the current population.
DIM SHARED PopulationSize AS LONG 'Must be even
PopulationSize = 4000 'the initial population size
DIM SHARED Unknowns(PopulationSize, NumberOfUnknowns) AS SINGLE
DIM SHARED Integers(PopulationSize, NumberOfUnknowns) AS LONG
DIM SHARED Strings(PopulationSize, TotalStringLength) AS INTEGER
DIM SHARED Fitness(PopulationSize) AS SINGLE
DIM SHARED Age(PopulationSize) AS INTEGER
DIM SHARED HistoricPopulation(-50 TO 2100) AS SINGLE

'other shared arrays and numbers

DIM SHARED Year 'the date
DIM SHARED MeanHalfFitness AS SINGLE ' not only do we calcualte the overall mean of the population, we calculate the mean of the first half of the population
'this will not contain babies, some of whom will have very low fitness due to the randomness of crossover and mutation
DIM SHARED NKilled 'keeps track of the number killed so we stay on track with the historic population
DIM SHARED NTooOld 'the number killed that reached MAXAGE rather than died from poor fitness
DIM SHARED MeanFitness 'mean fitness of the current population
DIM SHARED MaxFitness 'fitness of the fitest individual in the current population
DIM SHARED MinBlackDeathFitness AS SINGLE 'set this by trial and error to control the fraction of the population that die in the first black death.
MinBlackDeathFitness = 0.99268 'THIS NEEDS TO BE SET FOR EACH FITNESS FUNCTION AND POSSIBLY CHANGES WITH NUMBER OF UNKNOWNS TOO**

'The new population of babies.
DIM SHARED BabyStrings(PopulationSize, TotalStringLength) AS INTEGER
DIM SHARED NBabies AS LONG 'the number of babies

'The array that defines the range of the unknowns.  Values are assigned to this array in the DefineRange subroutine
DIM SHARED Range(2, NumberOfUnknowns) AS SINGLE 'For example, Range(1,3) contains the lower bound of the 3rd unknown and Range(2,3) the upper bound.

CLS 'Clear the screen.

CALL DefineRange 'Define the range of each unknown. These should also be set by the user. THESE NEED TO CHANGE WITH EACH FITNESS FUNCTION

'Set the random number generator so it produces a different set of numbers
'each time the program is run.
RANDOMIZE TIMER

CALL OpenFiles 'Open files used to store results.

'------- START OF THE GENETIC ALGORITHM -------

'------- CREATE AN INITIAL POPULATION

CALL History 'read the time series of the histroic population from the input file

CALL InitialPopulation 'Build a population of strings at random.

CALL FindFitness 'Find the fitness of each member of the population.

CALL Statistics(FittestIndividual) 'Find the mean fitness and the fittest individual.

'CALL PrintGeneration(FittestIndividual) 'Print generation to file.


'------- LOOP OVER ALL THE YEARS IN THE SIMULATION -------

FOR Year = STARTYEAR TO STARTYEAR + MaxYear
    UnadjustedMeanFitness = 0
    IF PopulationSize > MaxPopulationSize THEN ' stops the simulation if population too big to make sense
        CLOSE
        STOP
    END IF

    CALL Kills 'kill off the least fit individuals and the very old

    'extract individuals who didnt die and hold them in a temporary population
    REDIM SHARED NewUnknowns(PopulationSize - NKilled, NumberOfUnknowns) AS SINGLE
    REDIM SHARED NewIntegers(PopulationSize - NKilled, NumberOfUnknowns) AS LONG
    REDIM SHARED NewStrings(PopulationSize - NKilled, TotalStringLength) AS INTEGER
    REDIM SHARED NewFitness(PopulationSize - NKilled) AS SINGLE
    REDIM SHARED NewAge(PopulationSize - NKilled) AS INTEGER

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
            NEXT j
            NewFitness(i) = Fitness(Individual)
            NewAge(i) = Age(Individual) + 1
            i = i + 1
        END IF
    NEXT Individual

    'now make replace old pop with new population
    PopulationSize = PopulationSize - NKilled 'PROBLEM IF ALL INDIVIDUALS KILLED
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
 
    'Population now breeds.
    REDIM SHARED BabyStrings(PopulationSize, TotalStringLength) AS INTEGER 'The maximum number of offspring is half the population size so this array is plenty big enough
    NBabies = 0

    DIM BreadingProbability AS SINGLE 'these 8 lines will be replaced once we have a better time series of reprodcution rates
    BreadingProbability = 2 * 35.0 / 1000.0 'might have to double this as these numbers are from a graph of bithrates, i.e. only females count
    IF Year > 1890 THEN BreadingProbability = 30.0 / 1000
    IF Year > 1900 THEN BreadingProbability = 28.7 / 1000
    IF Year > 1910 THEN BreadingProbability = 25.1 / 1000
    IF Year > 1920 THEN BreadingProbability = 20.0 / 1000
    IF Year > 1930 THEN BreadingProbability = 16.3 / 1000
    IF Year > 1940 THEN BreadingProbability = 14.1 / 1000


    FOR Individual = 1 TO PopulationSize - 1 STEP 2 'Loop over the population choosing pairs of mates - it population size is odd the last individual does not mate.
        'adjacent (not random) pairs are picked - this means people tend to mate with people roughly their age and not their children.
        'HOWEVER THE USE OF A MIN AND MAX BREEDING AGE IS CURRENTLY IGNORED (SEE GLOBAL CONSTANT AREA ABOVE, BECAUSE THE DEMAND FOR BOTH PARENTS TO BE ADJACENT AND
        'OF THE RIGHT AGE MEANS TOO FEW BREED. I THINK WE CAN JUST LEAVE THIS AS IS FOR NOW, AND LET ANY AGE BREED.

        'Make babies with crossover.
        IF (Age(Individual) >= MINBREEDAGE) AND (Age(Individual) <= MAXBREEDAGE) THEN
            IF (Age(Individual + 1) >= MINBREEDAGE) AND (Age(Individual + 1) <= MAXBREEDAGE) THEN

                IF RND < BreadingProbability THEN
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

    CALL Statistics(FittestIndividual) 'Find the mean fitness and the fittest individual.

    CALL PrintGeneration(FittestIndividual) 'Print generation to file.

    'CALL Scaling(ScalingConstant, FittestIndividual, SumFitness, MeanFitness) 'If linear fitness scaling is "on" then scale population prior to selection.

NEXT Year 'Process the next year.

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
    'The assumption is that all unknowns have the same range, but with a little bit of extra coding this could be changed.
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





SUB FindFitness
    'The problem at hand is used to assign a positive (or zero) fitness to each individual in turn.
    'The fitness function or the kill function will vary with time once fully implemented
    'If the FindFitness subroutine or the number of unknowns is altered, you will need to also alter the Range subroutine if the range of the
    'unknowns is different than currently.

    FOR Individual = 1 TO PopulationSize
        Fitness(Individual) = 0
        SELECT CASE WhichFitnessFunction
            CASE 1
                CALL Fitness1(Individual)
            CASE 2
                CALL Fitness2(Individual)
            CASE ELSE
                CLOSE
                STOP
        END SELECT
        IF Fitness(Individual) < 0 THEN Fitness(Individual) = 0
    NEXT Individual



END SUB

SUB Fitness1 (Individual)
    'The problem at hand is used to assign a positive (or zero) fitness to each individual in turn.
    'The fitness function or the kill function will vary with time once fully implemented
    'If the FindFitness subroutine or the number of unknowns is altered, you will need to also alter the Range subroutine if the range of the
    'unknowns is different than currently.

    'The problem is f = [x1 + x2 +.....xn]/n     where xn ranges from 0 to 1
    Fitness(Individual) = 0
    FOR unknown = 1 TO NumberOfUnknowns
        Fitness(Individual) = Fitness(Individual) + SIN(Unknowns(Individual, unknown)) / NumberOfUnknowns 'a simple hump with a height of 1.
    NEXT


END SUB

SUB Fitness2 (Individual)

    'The problem at hand is used to assign a positive (or zero) fitness to each individual in turn.
    'The fitness function or the kill function will vary with time once fully implemented
    'If the FindFitness subroutine or the number of unknowns is altered, you will need to also alter the Range subroutine if the range of the
    'unknowns is different than currently.

    'The problem is f = [sin(x1) + sin(x2) + ......+ sin(xn)]/n
    Fitness(Individual) = 0
    FOR unknown = 1 TO NumberOfUnknowns
        Fitness(Individual) = Fitness(Individual) + SIN(Unknowns(Individual, unknown)) / NumberOfUnknowns 'a simple hump with a height of 1.
    NEXT

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

SUB History
    DIM i AS INTEGER
    'Data on UK population. This is just grabbed from the web and better data will be required
    'This data is used twice, once to adjust the size of the population so it does fun out of control, and then when plotting the results
    'to although a compression with historic reality.

    FOR i = -50 TO 2100
        INPUT #3, i, HistoricPopulation(i) 'in the file the historic population is in millions
        HistoricPopulation(i) = HistoricPopulation(i) * 1000 'assumes we are running the simulation where 1 person in the simulation equals 1000 in the real world
        'so only multiplying by 1000, not 1,000,000
        PRINT HistoricPopulation(i)
    NEXT i

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


SUB Kills
    'This is where selection to live or die takes place based on the fitness of the individual.
    'They are also get die if they are older than the maximum allowable age.

    NKilled = 0 'total number killed this year
    NTooOld = 0 'number that died this year due to old age


    FOR individual = 1 TO PopulationSize
        SELECT CASE Year 'AT THE MOMENT WE KILL IN STRICT ORDER THROUGH THE POPULATION ARRAY, THIS SHOULD REALLY BE RANDOM I GUESS

            CASE IS < 1349 'pre black death
                IF Fitness(individual) < MeanHalfFitness OR Age(individual) > MAXAGE THEN '  THE USE OF <MeanHalfFitness  MIGHT BE FITNESS FUNCTION DEPENDENT, ONE
                    'MIGHT FOR EXAMPLE USE <1.5*MEANHALFFITNESS IF YOU NEED FITTER INDIVIDUALS KILLED TOO. DITTO DURING OTHER PERIODS
                    Fitness(individual) = 0
                    NKilled = NKilled + 1
                    IF Age(individual) > MAXAGE THEN NTooOld = NTooOld + 1
                    IF PopulationSize - NKilled < HistoricPopulation(Year + 1) THEN
                        GOTO enoughkilled
                    END IF
                END IF

            CASE 1349 'the black death
                IF Fitness(individual) < MinBlackDeathFitness OR Age(individual) > MAXAGE THEN
                    Fitness(individual) = 0
                    NKilled = NKilled + 1
                    IF Age(individual) > MAXAGE THEN NTooOld = NTooOld + 1
                END IF

            CASE 1350 TO 1700 'post black death but pre modern era
                IF Fitness(individual) < MeanHalfFitness OR Age(individual) > MAXAGE THEN
                    Fitness(individual) = 0
                    NKilled = NKilled + 1
                    IF Age(individual) > MAXAGE THEN NTooOld = NTooOld + 1
                    IF PopulationSize - NKilled < HistoricPopulation(Year + 1) THEN 'NOT SURE IF THESE SHOULD BE (YEAR) OR (YEAR+1) in all these, might not matter much
                        GOTO enoughkilled
                    END IF
                END IF

            CASE 1701 TO 2019 ' the modern era but pre second black death
                IF RND > .9 OR Age(individual) > MAXAGE THEN 'THE 0.9 MIGHT BE FITNESS FUNCTION DEPENDENT. DITTO DURING OTHER PERIODS
                    Fitness(individual) = 0
                    NKilled = NKilled + 1
                    IF Age(individual) > MAXAGE THEN NTooOld = NTooOld + 1
                    IF PopulationSize - NKilled < HistoricPopulation(Year + 1) THEN
                        GOTO enoughkilled
                    END IF
                END IF

            CASE 2020 'second black death
                IF SECONDBLACKDEATH = 1 THEN
                    IF Fitness(individual) < MinBlackDeathFitness OR Age(individual) > MAXAGE THEN
                        Fitness(individual) = 0
                        NKilled = NKilled + 1
                        IF Age(individual) > MAXAGE THEN NTooOld = NTooOld + 1
                    END IF
                ELSE 'no second black death
                    IF RND > .9 OR Age(individual) > MAXAGE THEN 'THE 0.9 MIGHT BE FITNESS FUNCTION DEPENDENT
                        Fitness(individual) = 0
                        NKilled = NKilled + 1
                        IF Age(individual) > MAXAGE THEN NTooOld = NTooOld + 1
                        IF PopulationSize - NKilled < HistoricPopulation(Year + 1) THEN
                            GOTO enoughkilled
                        END IF
                    END IF
                END IF

            CASE IS > 2020 ' post second black death
                IF RND > 0.9 OR Age(individual) > MAXAGE THEN 'i.e. conditions with respect to fitness are the same as the modern era, with the collaspe of society
                    'it might be better if this went back the heuristic used before the modern era.
                    Fitness(individual) = 0
                    NKilled = NKilled + 1
                    IF Age(individual) > MAXAGE THEN NTooOld = NTooOld + 1
                    IF PopulationSize - NKilled < HistoricPopulation(Year) THEN
                        GOTO enoughkilled
                    END IF
                END IF
        END SELECT

    NEXT individual
    enoughkilled:

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
    'Open input and result files.

    OPEN "ebola_mean.csv" FOR OUTPUT AS #1
    OPEN "ebola_more.csv" FOR OUTPUT AS #2
    OPEN "pop.csv" FOR INPUT AS #3 'contains the historic UK population from various sources
    'add another file here for input once we have a good time series of reproduction rates
    OPEN "fitness.csv" FOR OUTPUT AS #4 'this contains the fitness of every individual in the year before the black death and is used to find the mimimum fitness
    'individuals require to survive the black death. (See the comments at the top of the program.)
END SUB

SUB PrintGeneration (FittestIndividual)
    'Print results to the screen and the files.

    'find the mean age of the population - not used by the program, but nice to report
    meanage = 0

    FOR i = 1 TO PopulationSize
        meanage = meanage + Age(i)
    NEXT i
    meanage = meanage / PopulationSize

    PRINT #2, 'Carriage return.

    'print the results of every thenth year to screen
    IF Year / 10 = CINT(Year / 10) THEN PRINT Year; PopulationSize; HistoricPopulation(Year); meanage; Fitness(FittestIndividual); MeanFitness;

    'print results of each year to file
    IF Year = STARTYEAR THEN PRINT #1, "Year"; ","; "PopulationSize"; ","; "HistoricPopulation"; ","; "NBabies"; ","; "Mean age"; ","; "%population killed"; ","; "NTooOld"; ","; "Fitness(FittestIndividual)"; ","; "MeanFitness"
    PRINT #1, Year; ","; PopulationSize; ","; HistoricPopulation(Year); ","; NBabies; ","; meanage; ","; 100 * NKilled / HistoricPopulation(Year); ","; NTooOld; ","; Fitness(FittestIndividual); ","; MeanFitness; ",";

    IF Year = 1347 THEN 'YEAR BEFORE BLACK DEATH

        FOR i = 1 TO PopulationSize
            PRINT #4, Fitness(i)
        NEXT i
        CLOSE #4
    END IF


    IF Year / 10 = CINT(Year / 10) THEN PRINT 'Carriage return on screen.
    PRINT #1, 'Carriage return.

    GOTO dontprintgen 'comment out if you want to print the whole generation
    FOR individual = 1 TO 1 'PopulationSize

        PRINT #2, Year; ","; Fitness(individual); ",";

        FOR Unknown = 1 TO NumberOfUnknowns
            PRINT #2, Unknowns(individual, Unknown); ",";
        NEXT Unknown

        FOR bit = 1 TO TotalStringLength
            PRINT #2, RIGHT$(STR$(Strings(individual, bit)), 1); ",";
        NEXT bit

        PRINT #2, 'Carriage return

    NEXT individual
    dontprintgen:

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


SUB Statistics (FittestIndividual)
    'Calculate the sum of fitness across the population and find the best individual,

    DIM SumHalfFitness AS SINGLE

    FittestIndividual = 0
    MaxFitness = 0

    FOR Individual = 1 TO PopulationSize
        IF Fitness(Individual) > MaxFitness THEN
            MaxFitness = Fitness(Individual)
            FittestIndividual = Individual
        END IF
    NEXT Individual

    SumFitness = 0 'Sum the fitness.
    SumHalfFitness = 0
    FOR Individual = 1 TO PopulationSize
        SumFitness = SumFitness + Fitness(Individual)
        IF Individual <= PopulationSize / 2 THEN SumHalfFitness = SumHalfFitness + Fitness(Individual) 'i.e. Also find the fitness of the first half of the population.
        'this is because the latter part of the populartion has a lot of very low fit
        'indivuals, due to crossover and mutation
    NEXT Individual

    'Find the average fitness of the population.
    MeanFitness = SumFitness / PopulationSize
    MeanHalfFitness = SumHalfFitness / (PopulationSize / 2)

END SUB



