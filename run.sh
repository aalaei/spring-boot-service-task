#!/bin/bash
# This script is used to run the program
mvn clean package -DskipTests
java -jar target/assignment-0.0.1-SNAPSHOT.jar
