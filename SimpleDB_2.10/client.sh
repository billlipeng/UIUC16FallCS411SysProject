javac -cp . simpledb/*/*.java simpledb/*/*/*.java
javac -cp . studentClient/simpledb/*.java
jar cf simpledb.jar simpledb/*/*.class simpledb/*/*/*.class
cp simpledb.jar studentClient/simpledb/
cd studentClient/simpledb 
#java -cp simpledb.jar:. FindMajors
java -cp simpledb.jar:. CreateStudentDB
