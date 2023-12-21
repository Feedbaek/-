#!/bin/sh

chmod +x gradlew
./gradlew build -x test
java -jar build/libs/AutoEveryDay-0.0.1-SNAPSHOT.jar
