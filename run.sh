#!/bin/bash
# This script is used to run the program
mvn clean package -DskipTests
java -jar target/task3-0.0.1-SNAPSHOT.jar
