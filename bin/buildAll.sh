#!/bin/bash
set -e

#stopdeployment sampleapp-java

chmod +x ./bin/*.sh
if [ "$TARGET" == "k8s" ]; then
    export CLUSTER_BOOTSTRAP_STATUS_FILE=/tmp/cluster_bootstrap.txt
    export FAMILY_BOOTSTRAP_STATUS_FILE=/tmp/family_bootstrap.txt
    export APP_BOOTSTRAP_STATUS_FILE=/tmp/app_bootstrap.txt

    if [ ! -e $CLUSTER_BOOTSTRAP_STATUS_FILE ]; then
        echo "bootstrapping cluster"
        ./bin/bootstrap-k8s-cluster.sh
        touch $CLUSTER_BOOTSTRAP_STATUS_FILE
    else
        echo "Cluster bootstrapped already. Skipping cluster bootstrap"
    fi

    if [ ! -e $FAMILY_BOOTSTRAP_STATUS_FILE ]; then
        echo "bootstrapping family"
        ./bin/bootstrap-k8s-app-family.sh
        touch $FAMILY_BOOTSTRAP_STATUS_FILE
    else
        echo "App family bootstrapped already. Skipping App family bootstrap"
    fi

    if [ ! -e $APP_BOOTSTRAP_STATUS_FILE ]; then
        echo "Bootstrapping Application"
        ./bin/bootstrap-k8s-app.sh
        touch $APP_BOOTSTRAP_STATUS_FILE
    else
        echo "Application bootstrapped already. Skipping application bootstrap"
    fi

else
  echo "Performing Hashi bootstrap"
  ./bin/bootstrap.sh
fi

# writ application secrets to vault
./bin/write-vault-secrets.sh

./bin/buildapp.sh

cd cobalt; deploy ariba-sampleapp-java sampleapp-java; cd -

# ./bin/runTests.sh