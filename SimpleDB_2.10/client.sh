#!/bin/bash
jar cf simpledb.jar simpledb/*/*.class simpledb/*/*/*.class
cp simpledb.jar studentClient/simpledb/
cd studentClient/simpledb 
java -cp simpledb.jar:. CreateStudentDB