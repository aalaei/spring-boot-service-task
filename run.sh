#!/bin/bash
# This script is used to run the program
mvn clean package -DskipTests
java -jar target/serverApp.jar
