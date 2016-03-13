#!/bin/bash 
ant

COUNTER=0
while [  $COUNTER -lt 10 ]; do
    java -cp ABAGAIL.jar opt.test.LendingClubTest
    let COUNTER=COUNTER+1 
done
