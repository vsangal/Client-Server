#!/bin/bash

java -jar server/build/libs/server-1.0.jar $1 &

process_id=`/bin/ps -fu $USER| grep "server-1.0.jar" | grep -v "grep" | awk '{print $2}'`

#echo $process_id

java -jar client/build/libs/client-1.0.jar<cmd.txt

kill -9 $process_id
