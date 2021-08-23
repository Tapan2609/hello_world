Locust Documentation: https://docs.locust.io/en/stable/

How to run performance tests for greeting service

Copy the greeting.py to one of the Jenkins slave and run below command

docker run --volume $PWD:/mnt/locust -e LOCUSTFILE_PATH=/mnt/locust/greeting.py -e TARGET_URL=https://qa-ms.cobalt.ariba.com/sampleapp-java-V-e4669a1-902-1574918948 -e LOCUST_OPTS="--clients=10 --no-web --run-time=50" locustio/locust

Update --run-time to your requirement
