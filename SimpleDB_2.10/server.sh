#!/bin/bash
javac -cp . simpledb/*/*.java simpledb/*/*/*.java
javac -cp . studentClient/simpledb/*.java
java simpledb.server.Startup studentdb
