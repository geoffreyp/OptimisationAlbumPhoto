#!/bin/bash
######################################
#	Param 1 : Album Named            #
#	Param 2 : Number of execution    #
######################################

if [ -z "$1" -o -z "$2" -o -z "$3" ]
	then
		echo Error : required parameters are missing
		echo The first param is the type of evaluation
		echo The second is the algorithm name
		echo The third is the number of execution of the algorithm
		echo The fourth is the number of iteration for ILS, not use for Hill Climber
		echo The last is to use the debug mode
		exit

fi
echo === compile java...
rm -rf bin/
mkdir bin/
javac -cp lib/json-simple-1.1.1.jar:src src/Run.java -d bin/

echo === execute $2 algorithm $3 times...
java -cp bin:lib/json-simple-1.1.1.jar Run $1 $2 $3 $4 $5

echo === execute python... 
python src/buildAlbum.py

echo The new website is generated. Have Fun !
