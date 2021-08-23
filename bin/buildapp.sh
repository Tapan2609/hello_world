#!/bin/bash
set -e

export BASE_IMAGE_VERSION=400-4-ga2958eb
# Run maven builds
cd greeting; mvn clean package test; cd -
cd test; mvn clean package test; cd -

# Run docker builds
cd greeting; dockerbuild  -t ${DOCKER_REGISTRY}/ariba-sampleapp-java/greeting:${POD_ID} . ; cd -

cd test; dockerbuild  -t ${DOCKER_REGISTRY}/ariba-sampleapp-java/test:${POD_ID} . ; cd -

cd nginx; dockerbuild  -t ${DOCKER_REGISTRY}/ariba-sampleapp-java/nginx:${POD_ID} . ; cd -

cd ftp; dockerbuild  -t ${DOCKER_REGISTRY}/ariba-sampleapp-java/ftp:${POD_ID} . ; cd -

cd nfs; dockerbuild  -t ${DOCKER_REGISTRY}/ariba-sampleapp-java/nfs:${POD_ID} .; cd -


