#!/bin/bash

echo -e "Updating build number...\n"

curl -n -s \
     -X PATCH \
     -H "Content-Type: application/json" \
     -H "Accept: application/vnd.heroku+json; version=3" \
     -H "Authorization: Bearer $HEROKU_API_TOKEN" \
     -d "{ \"BUILD_NUMBER\" : $TRAVIS_BUILD_NUMBER }" \
     https://api.heroku.com/apps/salume-prod/config-vars > /dev/null

echo -e "Build number updated \n"
