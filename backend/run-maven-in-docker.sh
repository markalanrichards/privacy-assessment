#!/bin/bash
dockerd-entrypoint.sh &
ls -al
PID=$!
sleep 10
mvn clean package -s settings.xml
EXIT_CODE=$?
kill $PID
sleep 10
exit $EXIT_CODE
