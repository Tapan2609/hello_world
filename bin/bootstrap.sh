#!/bin/bash
APP_UUID=2ef6193d-5750-45b9-8098-f98f4232dd3c
bootstrap $APP_UUID

echo "Adding secrets for the application use"
writesecret $APP_UUID oracle password123
oauthSecret="Basic dummy"
writesecret $APP_UUID oauthAuthorization $oauthSecret 
