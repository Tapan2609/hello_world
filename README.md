[![Join the chat](https://img.shields.io/badge/Cobalt--onboarding-Join-green.svg)](https://sap-ariba.slack.com/messages/C976BQEDA/)

## GreetingService
Sample SpringBoot Greeting Service Application

## Supported Features
* Sample springboot app
* Sample support for Vault based secrets
* Sample support for dc based configuration files
* Sample Selenium test cases
* Sample FTP dev container usage
* Sample NFS dev container usage
* Sample test container usage for container level tests
* Sample Split.io usage
* Sample proxy usage
* Sample external access
* Provision to do latency test with Core stack
* Provision to simulate out-of-memory scenarios
* Provision to simulate container process death


Note:
* This integrates with the new Ariba Jenkins instance https://ci.cobalt.ariba.com/

## Overview
This is an example implementation of micro service using spring-boot with the recommended layout for the source and related resources.

## Getting Started



Instructions to try this Sample Project is given below:
* Vagrant installation - Follow the README instructions at [Vagrant Project](https://github.wdf.sap.corp/Ariba-cobalt/vagrant) for installing vagrant.
* Install vagrant plugins as described here: [Ariba Wiki](https://wiki.ariba.com/display/CDP/Development+Environment+-+Part+2)
* Clone this repository locally
* Setup the development infrastructure using vagrant
```
cd sampleapp-java/
vagrant up
```
* Login to vagrant created environment after `vagrant up` is complete by running `vagrant ssh` from the `SampleApp` directory
* The building of the components are added into the Vagrantfile and will be executed as part of the box bring up. This builds the `nginx` docker image and maven build of `SampleApp` along with its docker image.
* Verify that the service `consul` is listed and running
 * Open browser and navigate to:  http://localhost:8500/ui/#/dc1/services to check the Consul UI
 * Verify that `sampleapp-java-dev` service is running
* Verify that greeting service works
 * Open a browser and navigate to:  https://localhost/sampleapp-java-dev/greeting
 * The following greeting message should be displayed `Hello, greeting-service!`

## Rebuilding the service
_Note: Note, this step will stop and delete previous sample-app-* containers to make sure no manual intervention is required._
Execute the following commands after doing a `vagrant ssh` to login to the box.
```
cd sampleapp-java
./bin/buildAll.sh
```

## References
* For more details refer https://wiki.ariba.com/display/CDP
