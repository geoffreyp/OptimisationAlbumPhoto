#!/bin/bash
######################################
#	Param 1 : Album Named            #
#	Param 2 : Number of execution    #
######################################

if [ -z "$1" -o -z "$2" ]
	then
		echo The first param is Album named and the second is the number of execution of the Hill Climber First Improvement Algorithm
		exit

fi
echo === compile java...
rm -rf bin/
mkdir bin/
javac -cp lib/json-simple-1.1.1.jar:src src/Run.java -d bin/

echo === execute Hill Climber First Improvement $2 times...
java -cp bin:lib/json-simple-1.1.1.jar Run $1 $2 $3

echo === execute python... 
echo The new website is generated. Have Fun !
