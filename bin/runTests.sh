#!/bin/bash
export jobId=$(nomad status | grep sampleapp | awk -F ' ' '{ print $1}')
echo Checking if the test container is up...

while  response=$( curl --silent --connect-timeout 5 http://127.0.0.1:9090/$jobId/test/v1/message) ||  [ -z "${response}" ] ; do
    if echo "$response"| grep -q  "Greetings from Test Container"
     then
      echo "$response"
      break;
    fi
    echo -n .
    sleep 5;
done
echo Starting test execution...
SUITEID=`curl http://127.0.0.1:9090/$jobId-test/v1/start | jq '.["id"]' | sed -e  's/^"//' -e 's/"$//'`

echo Waiting for tests to complete
while  response=$( curl --silent  http://127.0.0.1:9090/$jobId/test/v1/status?suiteId=$SUITEID) ||  [ -z "${response}" ] ; do
    if echo "$response"| grep -q  "stopped"
     then
      echo "$response"
      break;
    fi
		echo -n .
    sleep 5;
done
echo Completed.
