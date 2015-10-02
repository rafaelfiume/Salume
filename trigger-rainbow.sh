#!/bin/bash

echo -e "Triggering Rainbow...\n"

# Get last child project build number
BUILD_NUM=$(curl -s 'https://api.travis-ci.org/repos/rafaelfiume/Rainbow/builds' | grep -o '^\[{"id":[0-9]*,' | grep -o '[0-9]' | tr -d '\n')

echo -e "Rainbow build number is $BUILD_NUM\n"

# Restart last child project build
curl -i \
     -X POST \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -H "Authorization: token $ACCESS_TOKEN" \
     https://api.travis-ci.org/builds/$BUILD_NUM/restart
