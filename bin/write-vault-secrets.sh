#!/bin/bash
export APP_UUID=89b7d58a-41d2-4c20-ac04-8d14c53e322d
export VAULT_TOKEN=`grep 'Initial Root Token'  /var/vault/config/init.keys | awk '{print $NF}'`
#echo "Adding secrets for the application use"
writesecret $APP_UUID oracle password123