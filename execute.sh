#!/bin/bash

echo === compile java...
rm -rf bin/
mkdir bin/
javac -cp lib/json-simple-1.1.1.jar:src src/Run.java -d bin/

echo === execute java...
java -cp bin:lib/json-simple-1.1.1.jar Run rand 20

echo === execute python
