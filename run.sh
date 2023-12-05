#!/bin/bash
# This script is used to run the program in dev mode(you may want to add -Pprod to run in prod mode)
mvn clean package -DskipTests
java -jar target/serverApp.jar
